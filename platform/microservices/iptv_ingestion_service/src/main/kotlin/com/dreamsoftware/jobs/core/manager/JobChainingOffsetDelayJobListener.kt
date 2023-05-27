package com.dreamsoftware.jobs.core.manager

import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.createNewTrigger
import org.quartz.*
import org.quartz.listeners.JobListenerSupport
import java.util.*
import java.util.Calendar


data class ChainJobLink(
    val jobDetail: JobDetail,
    val intervalOffsetInMinutes: Int,
    val executeAfter: Iterable<JobKey>
)

class JobChainingOffsetDelayJobListener: JobListenerSupport() {

    private companion object {
        const val JOB_LISTENER_NAME = "JobChainingOffsetDelay"
    }

    private val jobInProgressMap: MutableMap<JobKey, Boolean> = mutableMapOf()
    private val jobExecutedMap: MutableMap<JobKey, Boolean> = mutableMapOf()

    private val chainLinks: MutableList<ChainJobLink> = mutableListOf()

    override fun getName(): String = JOB_LISTENER_NAME

    fun addJobChainLink(chainJobLink: ChainJobLink) {
        chainLinks.add(chainJobLink)
    }

    override fun jobToBeExecuted(context: JobExecutionContext?) {
        super.jobToBeExecuted(context)
        context?.jobDetail?.key?.let { jobKey ->
            jobExecutedMap[jobKey] = false
        }
    }

    override fun jobWasExecuted(context: JobExecutionContext?, jobException: JobExecutionException?) {
        context?.let {
            log.info("jobWasExecuted - Job ${it.jobDetail.key.name} CALLED!")
            jobExecutedMap[it.jobDetail.key] = true
            jobInProgressMap[it.jobDetail.key] = false
            rescheduleJobs(it.scheduler, it.jobDetail.key)
        }
    }

    private fun rescheduleJobs(scheduler: Scheduler, lastJobWasExecuted: JobKey) {
        chainLinks.filter { it.executeAfter.contains(lastJobWasExecuted) }.forEach { chainJob ->
            with(chainJob) {
                if(!jobInProgressMap.getOrDefault(jobDetail.key, false) && executeAfter.all { jobExecutedMap.getOrDefault(it, false) }) {
                    log.info("reschedule chainJobLink  ${jobDetail.key.name} with interval offset $intervalOffsetInMinutes CALLED!")
                    rescheduleJob(
                        scheduler = scheduler,
                        jobDetail = jobDetail,
                        intervalOffsetInMinutes = intervalOffsetInMinutes
                    )
                    jobInProgressMap[jobDetail.key] = true
                }
            }
        }
    }

    private fun rescheduleJob(scheduler: Scheduler, jobDetail: JobDetail, intervalOffsetInMinutes: Int) {
        with(scheduler) {
            getTriggersOfJob(jobDetail.key).takeIf { it.isNotEmpty() }?.forEach {
                log.info("rescheduleJob ${jobDetail.key.name}")
                rescheduleJob(it.key, buildTrigger(jobDetail.key, intervalOffsetInMinutes))
            } ?: run {
                log.info("scheduleJob ${jobDetail.key.name}")
                deleteJob(jobDetail.key)
                scheduleJob(jobDetail, buildTrigger(jobDetail.key, intervalOffsetInMinutes))
            }
        }
    }

    private fun buildTrigger(jobKey: JobKey, intervalOffsetInMinutes: Int) = TriggerBuilder.newTrigger()
        .withIdentity("trigger_${jobKey.name}_${System.currentTimeMillis()}", IJobBuilder.WATCH_JOB_GROUP)
        .startAt(Calendar.getInstance().apply {
            add(Calendar.MINUTE, intervalOffsetInMinutes)
        }.time)
        .build()

}
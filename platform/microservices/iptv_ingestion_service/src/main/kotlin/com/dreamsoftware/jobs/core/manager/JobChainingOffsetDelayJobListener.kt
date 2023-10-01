package com.dreamsoftware.jobs.core.manager

import com.dreamsoftware.jobs.core.IJobBuilder
import org.quartz.*
import org.quartz.listeners.JobListenerSupport
import java.util.Calendar

/**
 * The `JobChainingOffsetDelayJobListener` class is a Quartz job listener that manages job chaining and offset delays.
 * It tracks the execution of chained jobs and schedules them with a specified delay based on job dependencies.
 */
data class ChainJobLink(
    val jobDetail: JobDetail,
    val intervalOffsetInMinutes: Int,
    val executeAfter: Iterable<JobKey>
)

class JobChainingOffsetDelayJobListener : JobListenerSupport() {

    private companion object {
        const val JOB_LISTENER_NAME = "JobChainingOffsetDelay"
    }

    private val jobInProgressMap: MutableMap<JobKey, Boolean> = mutableMapOf()
    private val jobExecutedMap: MutableMap<JobKey, Boolean> = mutableMapOf()

    private val chainLinks: MutableList<ChainJobLink> = mutableListOf()

    override fun getName(): String = JOB_LISTENER_NAME

    /**
     * Add a job chain link to the listener.
     *
     * @param chainJobLink The ChainJobLink containing job details, offset, and dependencies.
     */
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

    /**
     * Reschedule jobs based on dependencies and offset delays.
     *
     * @param scheduler The Quartz scheduler instance.
     * @param lastJobWasExecuted The key of the last job that was executed.
     */
    private fun rescheduleJobs(scheduler: Scheduler, lastJobWasExecuted: JobKey) {
        chainLinks.filter { it.executeAfter.contains(lastJobWasExecuted) }.forEach { chainJob ->
            with(chainJob) {
                if (!jobInProgressMap.getOrDefault(jobDetail.key, false) && executeAfter.all { jobExecutedMap.getOrDefault(it, false) }) {
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

    /**
     * Reschedule a job with an offset delay.
     *
     * @param scheduler The Quartz scheduler instance.
     * @param jobDetail The JobDetail of the job to be rescheduled.
     * @param intervalOffsetInMinutes The interval offset in minutes.
     */
    private fun rescheduleJob(scheduler: Scheduler, jobDetail: JobDetail, intervalOffsetInMinutes: Int) {
        with(scheduler) {
            getTriggersOfJob(jobDetail.key).takeIf { it.isNotEmpty() }?.forEach {
                log.info("rescheduleJob ${jobDetail.key.name}")
                rescheduleJob(it.key, buildTrigger(jobDetail.key, intervalOffsetInMinutes))
            } ?: run {
                if (!checkExists(jobDetail.key)) {
                    // Tell Quartz to schedule the job using a trigger
                    scheduleJob(jobDetail, buildTrigger(jobDetail.key, intervalOffsetInMinutes))
                } else {
                    log.debug("Job ${jobDetail.key.name} already exists in Quartz.")
                }
            }
        }
    }

    /**
     * Build a Quartz Trigger instance with an offset delay.
     *
     * @param jobKey The JobKey associated with the job.
     * @param intervalOffsetInMinutes The interval offset in minutes.
     * @return A configured Trigger instance.
     */
    private fun buildTrigger(jobKey: JobKey, intervalOffsetInMinutes: Int) = TriggerBuilder.newTrigger()
        .withIdentity("trigger_${jobKey.name}_${System.currentTimeMillis()}", IJobBuilder.WATCH_JOB_GROUP)
        .startAt(Calendar.getInstance().apply {
            add(Calendar.MINUTE, intervalOffsetInMinutes)
        }.time)
        .build()
}
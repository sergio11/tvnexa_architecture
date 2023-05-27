package com.dreamsoftware.jobs.core.manager.impl

import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.manager.ChainJobLink
import com.dreamsoftware.jobs.core.manager.IJobSchedulerManager
import com.dreamsoftware.jobs.core.manager.JobChainingOffsetDelayJobListener
import org.quartz.Scheduler
import org.slf4j.LoggerFactory


internal class JobSchedulerManagerImpl(
    private val scheduler: Scheduler
) : IJobSchedulerManager {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun scheduleJobsAndStart(jobBuilderList: Iterable<IJobBuilder>) {
        with(scheduler) {
            if (!isStarted) {
                val jobChainingListener = JobChainingOffsetDelayJobListener()
                jobBuilderList.forEach { jobBuilder ->
                    with(jobBuilder) {
                        log.debug("JobSchedulerManager - scheduleJob - job ${getJobKey().name}")
                        // If a job exists, delete it!
                        deleteJob(getJobKey())
                        (getParentJobKeys() ?: getParentJobKey()?.let { listOf(it) })?.takeIf {
                            it.toList().isNotEmpty()
                        }?.let { parentJobKeys ->
                            // Add Chain link to parent job
                            jobChainingListener.addJobChainLink(
                                ChainJobLink(
                                    jobDetail = buildJob(),
                                    intervalOffsetInMinutes = getIntervalInMinutes(),
                                    executeAfter = parentJobKeys
                                )
                            )
                        } ?: run {
                            log.debug("Tell quartz to schedule the job using trigger ${getJobKey().name}")
                            // Tell quartz to schedule the job using trigger
                            scheduleJob(buildJob(), buildTrigger())
                        }
                    }
                }
                listenerManager.addJobListener(jobChainingListener)
                start()
            }
        }
    }
}
package com.dreamsoftware.jobs.core.manager.impl

import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.manager.ChainJobLink
import com.dreamsoftware.jobs.core.manager.IJobSchedulerManager
import com.dreamsoftware.jobs.core.manager.JobChainingOffsetDelayJobListener
import org.quartz.Scheduler
import org.slf4j.LoggerFactory

/**
 * The `JobSchedulerManagerImpl` class is an implementation of the [IJobSchedulerManager] interface.
 * It is responsible for scheduling and starting jobs based on a list of job builders, and it supports job chaining with offset delays.
 *
 * @param scheduler The Quartz scheduler instance used for job scheduling.
 */
internal class JobSchedulerManagerImpl(
    private val scheduler: Scheduler
) : IJobSchedulerManager {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Schedule and start jobs based on a list of job builders.
     *
     * @param jobBuilderList An iterable list of job builders that define the jobs to be scheduled.
     */
    override fun scheduleJobsAndStart(jobBuilderList: Iterable<IJobBuilder>) {
        with(scheduler) {
            if (!isStarted) {
                val jobChainingListener = JobChainingOffsetDelayJobListener()
                jobBuilderList.forEach { jobBuilder ->
                    with(jobBuilder) {
                        val jobKey = getJobKey()
                        log.debug("JobSchedulerManager - scheduleJob - job ${jobKey.name}")
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
                            log.debug("Tell quartz to schedule the job using trigger ${jobKey.name}")
                            if (!checkExists(jobKey)) {
                                runCatching {
                                    // Tell quartz to schedule the job using trigger
                                    scheduleJob(buildJob(), buildTrigger())
                                }
                            } else {
                                log.debug("Job ${jobKey.name} already exists in Quartz.")
                            }
                        }
                    }
                }
                listenerManager.addJobListener(jobChainingListener)
                start()
            }
        }
    }
}
package com.dreamsoftware.jobs.core.manager

import com.dreamsoftware.jobs.core.IJobBuilder
import org.quartz.Scheduler
import org.slf4j.LoggerFactory

internal class IJobSchedulerManagerImpl(
    private val scheduler: Scheduler
): IJobSchedulerManager {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun scheduleJob(jobBuilder: IJobBuilder) {
        with(jobBuilder) {
            log.debug("JobSchedulerManager - scheduleJob - job ${getJobKey().name}")
            with(scheduler) {
                // If a job exists, delete it!
                deleteJob(getJobKey())
                // Tell quartz to schedule the job using trigger
                scheduleJob(buildJob(), buildTrigger())
            }
        }
    }

    override fun start() {
        with(scheduler) {
            if(!isStarted) {
                log.debug("JobSchedulerManager - starting")
                start()
            }
        }
    }
}
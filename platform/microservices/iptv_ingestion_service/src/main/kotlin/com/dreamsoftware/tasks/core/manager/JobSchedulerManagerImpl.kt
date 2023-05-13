package com.dreamsoftware.tasks.core.manager

import com.dreamsoftware.tasks.core.IJobBuilder
import org.quartz.Scheduler

internal class JobSchedulerManagerImpl(
    private val scheduler: Scheduler
): JobSchedulerManager {

    override fun scheduleJob(jobBuilder: IJobBuilder) {
        with(jobBuilder) {
            with(scheduler) {
                // If a job exists, delete it!
                deleteJob(getJobKey())
                // Tell quartz to schedule the job using trigger
                scheduleJob(buildJob(), buildTrigger())
            }
        }
    }
}
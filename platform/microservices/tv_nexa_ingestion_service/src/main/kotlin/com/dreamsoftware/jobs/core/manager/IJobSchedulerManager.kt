package com.dreamsoftware.jobs.core.manager

import com.dreamsoftware.jobs.core.IJobBuilder

/**
 * The `IJobSchedulerManager` interface defines a contract for a job scheduler manager.
 * Implementations of this interface are responsible for scheduling and starting jobs based on the provided job builders.
 */
interface IJobSchedulerManager {

    /**
     * Schedule and start jobs based on a list of job builders.
     *
     * @param jobBuilderList An iterable list of job builders that define the jobs to be scheduled.
     */
    fun scheduleJobsAndStart(jobBuilderList: Iterable<IJobBuilder>)
}
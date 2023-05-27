package com.dreamsoftware.jobs.core.manager

import com.dreamsoftware.jobs.core.IJobBuilder

interface IJobSchedulerManager {
    fun scheduleJobsAndStart(jobBuilderList: Iterable<IJobBuilder>)
}
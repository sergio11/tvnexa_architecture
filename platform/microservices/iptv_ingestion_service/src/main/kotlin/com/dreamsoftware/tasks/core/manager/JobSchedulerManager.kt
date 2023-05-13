package com.dreamsoftware.tasks.core.manager

import com.dreamsoftware.tasks.core.IJobBuilder

interface JobSchedulerManager {

    fun scheduleJob(jobBuilder: IJobBuilder)
}
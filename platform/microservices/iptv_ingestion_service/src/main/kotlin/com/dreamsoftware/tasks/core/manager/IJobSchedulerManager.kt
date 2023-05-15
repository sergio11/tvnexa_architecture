package com.dreamsoftware.tasks.core.manager

import com.dreamsoftware.tasks.core.IJobBuilder

interface IJobSchedulerManager {
    fun scheduleJob(jobBuilder: IJobBuilder)
    fun start()
}
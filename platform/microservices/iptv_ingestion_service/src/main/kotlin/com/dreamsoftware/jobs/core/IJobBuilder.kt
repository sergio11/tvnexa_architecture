package com.dreamsoftware.jobs.core

import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger

interface IJobBuilder {

    companion object {
        const val JOB_MAP_NAME_ID_KEY = "name"
        const val WATCH_JOB_GROUP = "WatchJob"
    }

    fun buildJob(): JobDetail
    fun buildTrigger(): Trigger
    fun getJobKey(): JobKey
}
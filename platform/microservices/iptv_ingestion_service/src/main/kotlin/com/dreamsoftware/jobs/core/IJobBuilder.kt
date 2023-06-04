package com.dreamsoftware.jobs.core

import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger

interface IJobBuilder {

    companion object {
        const val JOB_MAP_NAME_ID_KEY = "name"
        const val WATCH_JOB_GROUP = "WatchJob"
    }

    fun buildJob(jobId: String? = null, data: Map<String, String>? = null): JobDetail
    fun buildTrigger(triggerId: String? = null): Trigger? = null
    fun getJobKey(jobId: String? = null): JobKey
    fun getIntervalInMinutes(): Int
    fun getParentJobKey(): JobKey? = null
    fun getParentJobKeys(): Iterable<JobKey>? = null
}
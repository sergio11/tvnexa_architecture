package com.dreamsoftware.jobs.core

import org.quartz.*

inline fun <reified T: Job> createNewJob(jobId: String) = JobBuilder.newJob(T::class.java)
    .withIdentity(jobId, IJobBuilder.WATCH_JOB_GROUP)
    .usingJobData(IJobBuilder.JOB_MAP_NAME_ID_KEY, jobId)
    .build()


fun createNewTrigger(triggerId: String, intervalInMinutes: Int) = TriggerBuilder.newTrigger()
    .withIdentity(triggerId, IJobBuilder.WATCH_JOB_GROUP)
    .withSchedule(
        SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMinutes(intervalInMinutes)
            .repeatForever()
    )
    .build()

fun createJobKey(jobId: String) = JobKey.jobKey(jobId, IJobBuilder.WATCH_JOB_GROUP)
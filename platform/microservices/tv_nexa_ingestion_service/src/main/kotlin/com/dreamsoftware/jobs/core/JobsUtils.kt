package com.dreamsoftware.jobs.core

import org.quartz.*

/**
 * Creates a new Quartz JobDetail for a specified job type with optional job data.
 *
 * @param jobId The unique job ID.
 * @param data Optional job data as a map of key-value pairs.
 * @return A configured JobDetail instance.
 */
inline fun <reified T: Job> createNewJob( jobId: String, data: Map<String, String>? = null): JobDetail =
    JobBuilder.newJob(T::class.java)
        .withIdentity(jobId, IJobBuilder.WATCH_JOB_GROUP)
        .storeDurably(true)
        .usingJobData(JobDataMap().apply {
            put(IJobBuilder.JOB_MAP_NAME_ID_KEY, jobId)
            data?.let { putAll(it) }
        })
        .build()
/**
 * Creates a new Quartz Trigger with specified trigger ID and interval settings.
 *
 * @param triggerId The unique trigger ID.
 * @param intervalInMinutes The interval between trigger executions (in minutes).
 * @param repeatForever Flag indicating if the trigger should repeat forever (default is true).
 * @return A configured Trigger instance.
 */
fun createNewTrigger(triggerId: String, intervalInMinutes: Int, repeatForever: Boolean = true): Trigger {
    return TriggerBuilder.newTrigger()
        .withIdentity(triggerId, IJobBuilder.WATCH_JOB_GROUP)
        .withSchedule(
            SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(intervalInMinutes).let {
                    if (repeatForever) {
                        it.repeatForever()
                    } else {
                        it
                    }
                }
        )
        .build()
}


/**
 * Creates a Quartz JobKey with the specified job ID.
 *
 * @param jobId The unique job ID.
 * @return A JobKey instance.
 */
fun createJobKey(jobId: String): JobKey {
    return JobKey.jobKey(jobId, IJobBuilder.WATCH_JOB_GROUP)
}
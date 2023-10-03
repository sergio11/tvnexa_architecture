package com.dreamsoftware.jobs.core

import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger

/**
 * The `IJobBuilder` interface defines a set of methods for building Quartz jobs, triggers, and related components.
 * It provides a common structure for job and trigger creation in a Quartz-based scheduling system.
 */
interface IJobBuilder {

    companion object {
        const val JOB_MAP_NAME_ID_KEY = "name"
        const val WATCH_JOB_GROUP = "WatchJob"
        const val JOB_MAP_ONE_SHOT_KEY = "isOneShotJob"
    }

    /**
     * Build a Quartz JobDetail instance with optional job ID and job data.
     *
     * @param jobId The unique job ID.
     * @param data Optional job data as a map of key-value pairs.
     * @return A configured JobDetail instance.
     */
    fun buildJob(jobId: String? = null, data: Map<String, String>? = null): JobDetail

    /**
     * Build a Quartz Trigger instance with an optional trigger ID.
     *
     * @param triggerId The unique trigger ID.
     * @return A configured Trigger instance or null if not applicable.
     */
    fun buildTrigger(triggerId: String? = null): Trigger? = null

    /**
     * Get a Quartz JobKey based on the provided job ID.
     *
     * @param jobId The unique job ID.
     * @return A JobKey instance.
     */
    fun getJobKey(jobId: String? = null): JobKey

    /**
     * Get the interval in minutes for job execution.
     *
     * @return The interval in minutes.
     */
    fun getIntervalInMinutes(): Int

    /**
     * Get the parent job key if applicable.
     *
     * @return A JobKey instance or null if not applicable.
     */
    fun getParentJobKey(): JobKey? = null

    /**
     * Get a collection of parent job keys if applicable.
     *
     * @return An iterable collection of JobKey instances or null if not applicable.
     */
    fun getParentJobKeys(): Iterable<JobKey>? = null
}
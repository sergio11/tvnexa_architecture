package com.dreamsoftware.jobs.core

import kotlinx.coroutines.*
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

/**
 * SupportJob is an abstract base class for Quartz jobs that provides enhanced coroutine handling and logging capabilities.
 *
 * It extends the [Job] interface and includes coroutine functionality for more efficient asynchronous execution.
 *
 * @property coroutineContext The coroutine context for job execution, using Dispatchers.IO and a SupervisorJob for error handling.
 */
abstract class SupportJob : Job, CoroutineScope {

    /**
     * Logger for the current job class.
     */
    protected val log = LoggerFactory.getLogger(this::class.java)

    /**
     * The coroutine context for job execution.
     */
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    /**
     * The main execution method for the job. It executes the job using coroutines and logs the start and completion.
     *
     * @param context The [JobExecutionContext] containing job-specific data.
     */
    override fun execute(context: JobExecutionContext?) {
        log.debug("${context?.jobDetail?.key?.name} executed CALLED!")

        // Execute the job asynchronously using coroutines
        runBlocking { onStartExecution(context?.jobDetail?.jobDataMap, context?.scheduler) }

        log.debug("${context?.jobDetail?.key?.name} finished CALLED!")
    }

    /**
     * The abstract method to be implemented by subclasses. It represents the main logic of the job.
     *
     * @param jobData The job-specific data provided in the [JobDataMap].
     * @param scheduler The Quartz scheduler instance.
     */
    protected abstract suspend fun onStartExecution(jobData: JobDataMap?, scheduler: Scheduler?)
}
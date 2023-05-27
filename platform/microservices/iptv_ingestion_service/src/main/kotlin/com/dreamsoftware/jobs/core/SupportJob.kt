package com.dreamsoftware.jobs.core

import kotlinx.coroutines.*
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

abstract class SupportJob : Job, CoroutineScope {

    protected val log = LoggerFactory.getLogger(this::class.java)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    override fun execute(context: JobExecutionContext?) {
        log.debug("${context?.jobDetail?.key?.name} executed CALLED!")
        runBlocking { onStartExecution() }
        log.debug("${context?.jobDetail?.key?.name} finished CALLED!")
    }

    protected abstract suspend fun onStartExecution()

}
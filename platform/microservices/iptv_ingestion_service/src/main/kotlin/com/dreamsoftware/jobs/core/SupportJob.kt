package com.dreamsoftware.jobs.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

abstract class SupportJob : Job, CoroutineScope {

    protected val log = LoggerFactory.getLogger(this::class.java)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    override fun execute(context: JobExecutionContext?) {
        log.debug("${context?.jobDetail?.key?.name} execute")
        launch { onStartExecution() }
        log.debug("${context?.jobDetail?.key?.name} finish")
    }

    protected abstract suspend fun onStartExecution()

}
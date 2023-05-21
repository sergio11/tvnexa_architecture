package com.dreamsoftware.jobs.core.manager.impl

import com.dreamsoftware.jobs.core.IJobBuilder
import com.dreamsoftware.jobs.core.manager.IJobSchedulerManager
import org.quartz.Scheduler
import org.slf4j.LoggerFactory
import org.quartz.listeners.JobChainingJobListener


internal class JobSchedulerManagerImpl(
    private val scheduler: Scheduler
): IJobSchedulerManager {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val jobChainingListener by lazy {
        JobChainingJobListener("ChainListener")
    }

    override fun scheduleJob(jobBuilder: IJobBuilder) {
        with(jobBuilder) {
            log.debug("JobSchedulerManager - scheduleJob - job ${getJobKey().name}")
            with(scheduler) {
                // If a job exists, delete it!
                deleteJob(getJobKey())
                (getParentJobKeys() ?: getParentJobKey()?.let { listOf(it) })?.forEach {
                    addJob(buildJob(), true)
                    // Add Chain link to parent job
                    jobChainingListener.addJobChainLink(it, getJobKey())
                } ?: run {
                    // Tell quartz to schedule the job using trigger
                    scheduleJob(buildJob(), buildTrigger())
                }
            }
        }
    }

    override fun scheduleJob(jobBuilderList: Iterable<IJobBuilder>) {
        jobBuilderList.map(::scheduleJob)
    }

    override fun start() {
        with(scheduler) {
            if(!isStarted) {
                listenerManager.addJobListener(jobChainingListener)
                log.debug("JobSchedulerManager - starting")
                start()
            }
        }
    }
}
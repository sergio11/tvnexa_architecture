package com.dreamsoftware.jobs.core

import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.get
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle

class JobFactoryImpl: JobFactory, KoinComponent {
    override fun newJob(bundle: TriggerFiredBundle?, scheduler: Scheduler?): Job =
        bundle?.let { get<Job>(it.jobDetail.jobClass) } ?: throw NotImplementedError("Job Factory error")
}
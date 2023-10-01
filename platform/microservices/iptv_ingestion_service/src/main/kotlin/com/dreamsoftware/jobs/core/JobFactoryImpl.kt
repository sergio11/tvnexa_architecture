package com.dreamsoftware.jobs.core

import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.get
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle

/**
 * JobFactoryImpl is an implementation of the Quartz JobFactory interface.
 *
 * It is responsible for creating instances of Quartz jobs using the Koin dependency injection framework.
 */
class JobFactoryImpl : JobFactory, KoinComponent {

    /**
     * Create a new instance of a Quartz job using Koin dependency injection.
     *
     * @param bundle The TriggerFiredBundle containing job detail and scheduler information.
     * @param scheduler The Quartz scheduler instance.
     * @return A new instance of the Quartz job.
     * @throws NotImplementedError if the job creation process encounters an error.
     */
    override fun newJob(bundle: TriggerFiredBundle?, scheduler: Scheduler?): Job {
        return bundle?.let { get<Job>(it.jobDetail.jobClass) }
            ?: throw NotImplementedError("Job Factory error")
    }
}
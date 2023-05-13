package com.dreamsoftware.tasks

import com.dreamsoftware.tasks.core.IJobBuilder
import com.dreamsoftware.tasks.core.IJobBuilder.Companion.JOB_MAP_NAME_ID_KEY
import com.dreamsoftware.tasks.core.IJobBuilder.Companion.WATCH_JOB_GROUP
import org.quartz.*

class TestJob(): Job {

    override fun execute(context: JobExecutionContext?) {

    }

    companion object: IJobBuilder {

        private const val JOB_ID = ""
        private const val TRIGGER_ID = ""

        override fun buildJob(): JobDetail = JobBuilder.newJob(TestJob::class.java)
            .withIdentity(JOB_ID, WATCH_JOB_GROUP)
            .usingJobData(JOB_MAP_NAME_ID_KEY, "test")
            .build()

        override fun buildTrigger(): Trigger = TriggerBuilder.newTrigger()
            .withIdentity(TRIGGER_ID, WATCH_JOB_GROUP)
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    // every minute
                    .withIntervalInMinutes(1)
                    .repeatForever()
            )
            .build()

        override fun getJobKey(): JobKey = JobKey.jobKey(JOB_ID, WATCH_JOB_GROUP)

    }
}
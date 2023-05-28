package com.dreamsoftware.jobs

import com.dreamsoftware.jobs.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger

@DisallowConcurrentExecution
class ChannelEpgGrabbingJob(): SupportJob() {



    override suspend fun onStartExecution() {

        val javaVersionCmd = async(Dispatchers.IO) {
            ProcessBuilder("java", "-version")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()
        }


        val result = javaVersionCmd.await()

        log.debug("Java Version - EPG Grabbing -> $result")
    }

    companion object: IJobBuilder {

        private const val JOB_ID = "channel_epg_grabbing_job"
        private const val TRIGGER_ID = "channel_epg_grabbing_job_trigger"
        private const val INTERVAL_IN_MINUTES = 1

        override fun buildJob(): JobDetail = createNewJob<ChannelEpgGrabbingJob>(JOB_ID)
        override fun buildTrigger(): Trigger = createNewTrigger(TRIGGER_ID, INTERVAL_IN_MINUTES)
        override fun getJobKey(): JobKey = createJobKey(JOB_ID)
        override fun getIntervalInMinutes(): Int = INTERVAL_IN_MINUTES
    }
}
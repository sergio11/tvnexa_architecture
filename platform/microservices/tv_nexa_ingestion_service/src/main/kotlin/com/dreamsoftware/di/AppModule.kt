package com.dreamsoftware.di

import com.dreamsoftware.data.api.di.dataModule
import com.dreamsoftware.data.epg.di.epgDataSources
import org.koin.dsl.module

/**
 * The `appModule` is a Koin module that includes other modules required for dependency injection.
 * It combines the modules defined in the 'dataModule', 'jobsModule', and 'epgDataSources'.
 */
val appModule = module {
    // Includes other modules for dependency injection
    includes(dataModule, jobsModule, epgDataSources)
}
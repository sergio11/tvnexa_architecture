package com.dreamsoftware.api.domain.di

import com.dreamsoftware.api.rest.controllers.*
import com.dreamsoftware.api.rest.controllers.impl.*
import com.dreamsoftware.api.rest.controllers.impl.CategoryControllerImpl
import com.dreamsoftware.api.rest.controllers.impl.ChannelControllerImpl
import com.dreamsoftware.api.rest.controllers.impl.CountryControllerImpl
import com.dreamsoftware.api.rest.controllers.impl.EpgChannelProgrammeControllerImpl
import com.dreamsoftware.api.rest.controllers.impl.RegionControllerImpl
import com.dreamsoftware.core.getMapper
import org.koin.dsl.module

val serviceModule = module {
    single<ICategoryController> { CategoryControllerImpl(get(), getMapper()) }
    single<ICountryController> { CountryControllerImpl(get(), getMapper()) }
    single<IRegionController> {  RegionControllerImpl(get(), getMapper()) }
    single<ISubdivisionController> { SubdivisionControllerImpl(get(), getMapper()) }
    single<IChannelController> { ChannelControllerImpl(get(), getMapper(), getMapper()) }
    single<IEpgChannelProgrammeController> { EpgChannelProgrammeControllerImpl(get(), get(), getMapper()) }
    single<IUserController> { UserControllerImpl(get(), get(), get(), getMapper(), getMapper(), getMapper(), get(), getMapper(), getMapper(), getMapper(), getMapper()) }
}
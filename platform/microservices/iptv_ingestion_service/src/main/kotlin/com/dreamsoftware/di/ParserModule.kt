package com.dreamsoftware.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.koin.dsl.module
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val parserModule = module {
    single {
        XmlMapper(JacksonXmlModule().apply {
            setDefaultUseWrapper(false)
        }).registerKotlinModule().also {
            it.setConfig(
                it.deserializationConfig
                    .with(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            )
        }
    }
}
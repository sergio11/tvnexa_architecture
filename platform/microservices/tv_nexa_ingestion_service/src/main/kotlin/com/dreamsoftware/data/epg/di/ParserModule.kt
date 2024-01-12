package com.dreamsoftware.data.epg.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.koin.dsl.module
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

/**
 * Koin module for configuring and providing dependencies related to XML parsing and object mapping using Jackson XmlMapper.
 */
val parserModule = module {
    // Define a single instance of XmlMapper for XML parsing and object mapping
    single {
        // Create a new instance of XmlMapper with configuration settings
        XmlMapper(JacksonXmlModule().apply {
            // Disable default use of XML wrappers for collections
            setDefaultUseWrapper(false)
        }).registerKotlinModule().also {
            // Configure the XmlMapper instance with custom settings
            it.setConfig(
                it.deserializationConfig
                    // Enable case-insensitive property name acceptance
                    .with(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    // Disable failing on unknown properties during deserialization
                    .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            )
        }
    }
}
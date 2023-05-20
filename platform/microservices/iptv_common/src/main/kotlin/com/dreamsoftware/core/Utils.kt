package com.dreamsoftware.core

val isDevelopmentMode
    get() = System.getProperty("development.mode").toBoolean()
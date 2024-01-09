package com.dreamsoftware.utils

import com.dreamsoftware.model.EpgGrabbingConfig
import java.io.File
import java.security.MessageDigest

fun EpgGrabbingConfig.getFinalOutputGuidesPath(languageId: String, site: String) =
    sitesBaseFolder + File.separator + outputGuidesPath.replace("{lang}", languageId)
        .replace("{site}", site)

fun String.hash256() =
    MessageDigest.getInstance("SHA-256").digest(toByteArray())
        .joinToString("") { "%02x".format(it) }
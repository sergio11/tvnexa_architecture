package com.dreamsoftware.utils

import com.dreamsoftware.model.EpgGrabbingConfig
import java.io.File

fun EpgGrabbingConfig.getFinalOutputGuidesPath(languageId: String, site: String) =
    sitesBaseFolder + File.separator + outputGuidesPath.replace("{lang}", languageId)
        .replace("{site}", site)
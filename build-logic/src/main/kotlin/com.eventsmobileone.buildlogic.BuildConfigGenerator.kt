package com.eventsmobileone.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import java.io.File

/**
 * Task to generate BuildConfig for different environments
 * Following Kotlin API guidelines for testability and maintainability
 */
abstract class BuildConfigGenerator : DefaultTask() {
    
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty
    
    @get:OutputFile
    abstract val outputFile: RegularFileProperty
    
    init {
        outputDir.set(project.layout.buildDirectory.dir("generated/buildConfig"))
        outputFile.set(outputDir.file("BuildConfig.kt"))
    }
    
    @TaskAction
    fun generateBuildConfig() {
        val environment = project.findProperty("environment") as String? ?: "dev"
        val apiBaseUrl = when (environment) {
            "dev" -> "https://dev-api.event.tkhtech.com"
            "staging" -> "https://staging-api.event.tkhtech.com"
            "prod" -> "https://api.event.tkhtech.com"
            else -> "https://api.event.tkhtech.com"
        }
        
        val isDebug = environment != "prod"
        val versionName = project.findProperty("versionName") as String? ?: "1.0.0"
        val versionCode = (project.findProperty("versionCode") as String?)?.toIntOrNull() ?: 1
        
        val buildConfigContent = """
package com.eventsmobileone

/**
 * Auto-generated BuildConfig for environment: $environment
 * Generated at build time - do not modify manually
 */
object BuildConfig {
    /**
     * API base URL for the current build variant
     */
    const val API_BASE_URL: String = "$apiBaseUrl"
    
    /**
     * Environment name for the current build variant
     */
    const val ENVIRONMENT: String = "$environment"
    
    /**
     * Whether this is a debug build
     */
    const val IS_DEBUG: Boolean = $isDebug
    
    /**
     * App version name
     */
    const val VERSION_NAME: String = "$versionName"
    
    /**
     * App version code
     */
    const val VERSION_CODE: Int = $versionCode
}
""".trimIndent()
        
        outputDir.get().asFile.mkdirs()
        outputFile.get().asFile.writeText(buildConfigContent)
        
        logger.lifecycle("Generated BuildConfig for environment: $environment")
        logger.lifecycle("API Base URL: $apiBaseUrl")
        logger.lifecycle("Debug: $isDebug")
    }
}

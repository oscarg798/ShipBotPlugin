/*
 * Copyright (c) 2021 Oscar David Gallon Rosero
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oscarg798.plugin

import com.oscarg798.plugin.extension.ShipBotPluginExtension
import com.oscarg798.plugin.tasks.factory.ShipTaskFactory
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ShipBotPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = configureExtension(project)
        val taskFactory = ShipTaskFactory(project.properties, extension)

        project.task(TASK_NAME).dependsOn(project.tasks.findByName(CLEAN_TASK_NAME))
            .doFirst {
                taskFactory.createTasks().forEach { it.run() }
            }
    }

    private fun configureExtension(project: Project) = project.extensions.create(
        EXTENSION_NAME,
        ShipBotPluginExtension::class.java
    )
}

private const val TASK_NAME = "ship"
private const val EXTENSION_NAME = "releasePlugin"
private const val CLEAN_TASK_NAME = "clean"

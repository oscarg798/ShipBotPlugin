/*
 * Copyright (c) 2021 Oscar David Gallon Rosero
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oscarg798.plugin.tasks.factory

import com.oscarg798.plugin.commandexecutrors.ProcessShipCommandExecutor
import com.oscarg798.plugin.commandexecutrors.ShipCommandExecutor
import com.oscarg798.plugin.extension.ShipBotPluginExtension
import com.oscarg798.plugin.tasks.ShipTask
import com.oscarg798.plugin.tasks.factory.taksbuilders.BuildGeneratorTaskBuilder
import com.oscarg798.plugin.tasks.factory.taksbuilders.FirebasePublisherTaskBuilder
import com.oscarg798.plugin.tasks.factory.taksbuilders.UnitTestRunnerTaskBuilder
import com.oscarg798.plugin.tasks.factory.taksbuilders.paramfinders.BuildTypeParamFinder
import com.oscarg798.plugin.tasks.factory.taksbuilders.paramfinders.ParamFinder
import com.oscarg798.plugin.utils.BuildType
import com.oscarg798.plugin.utils.Flavor
import java.util.*

internal class ShipTaskFactory(
    private val projectName: String,
    private val properties: Map<String, *>,
    private val extension: ShipBotPluginExtension,
    private val buildTypeParamFinder: ParamFinder<BuildType>,
    private val flavorTypeParamFinder: ParamFinder<Flavor?>

) {

    private val shipCommandExecutor: ShipCommandExecutor = ProcessShipCommandExecutor()

    fun createTasks(): Collection<ShipTask> {


        val buildGeneratorTask = BuildGeneratorTaskBuilder(
            shipCommandExecutor,
            buildTypeParamFinder,
            flavorTypeParamFinder
        ).build(properties)
        val firebasePublisherTask =
            FirebasePublisherTaskBuilder(
                projectName,
                extension,
                shipCommandExecutor,
                buildTypeParamFinder,
                flavorTypeParamFinder
            ).build(properties)

        val tasks = LinkedList<ShipTask>()
        tasks.add(buildGeneratorTask)
        tasks.add(firebasePublisherTask)


        return if (extension.unitTestRequired) {
            tasks.apply {
                addFirst(
                    UnitTestRunnerTaskBuilder(shipCommandExecutor, BuildTypeParamFinder(extension)).build(
                        properties
                    )
                )
            }
        } else {
            tasks
        }
    }
}
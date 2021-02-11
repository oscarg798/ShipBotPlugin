/*
 * Copyright (c) 2021 Oscar David Gallon Rosero
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oscarg798.plugin.tasks.factory.taksbuilders

import com.oscarg798.plugin.commandexecutrors.ShipCommandExecutor
import com.oscarg798.plugin.extension.ShipBotPluginExtension
import com.oscarg798.plugin.tasks.plublisher.FirebasePublisher
import com.oscarg798.plugin.tasks.plublisher.FirebasePublisherParams

internal class FirebasePublisherTaskBuilder(
    private val extension: ShipBotPluginExtension,
    private val shipCommandExecutor: ShipCommandExecutor,
    private val buildTypeParamFinder: BuildTypeParamFinder
) : TaskBuilder<FirebasePublisher> {

    override fun build(
        properties: Map<String, *>
    ): FirebasePublisher {
        val buildType = buildTypeParamFinder.getBuildType(properties)

        val firebaseToken = extension.firebaseToken ?: error("You should provide a firebase token")

        val firebaseProjectId = properties[FIREBASE_PROJECT_ID_PARAM_NAME]?.toString()
            ?: error("You should provide a firebase project id as param")


        val distributionGroup = properties[DISTRIBUTION_GROUP_PARAM_NAME]?.toString()

        val notes = properties[NOTES_PARAM_NAME]?.toString()

        return FirebasePublisher(
            shipCommandExecutor,
            FirebasePublisherParams(buildType, firebaseToken, firebaseProjectId, distributionGroup, notes)
        )
    }
}

private const val FIREBASE_PROJECT_ID_PARAM_NAME = "firebaseProjectId"
private const val DISTRIBUTION_GROUP_PARAM_NAME = "distributionGroup"
private const val NOTES_PARAM_NAME = "notes"
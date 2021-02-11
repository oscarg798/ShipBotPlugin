/*
 * Copyright (c) 2021 Oscar David Gallon Rosero
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oscarg798.plugin.tasks.plublisher

import com.oscarg798.plugin.commandexecutrors.ShipCommandExecutor
import com.oscarg798.plugin.tasks.ShipTask
import com.oscarg798.plugin.utils.BuildType
import com.oscarg798.plugin.utils.ShipTaskArguments


internal class FirebasePublisher(
    shipCommandExecutor: ShipCommandExecutor,
    private val firebasePublisherParams: FirebasePublisherParams,
) : ShipTask(shipCommandExecutor) {

    override fun getCommand(): String = FIREBASE_COMMAND_NAME

    override fun getParams(): List<String> = addApk()
        .addProjectId()
        .addToken()
        .addDistributionGroups()
        .addReleaseNotes()

    private fun addApk() =
        "appdistribution:distribute ${firebasePublisherParams.buildType.getReleasePathFromBuildType()}".toCommandArguments()

    private fun ShipTaskArguments.addProjectId() = toMutableList().apply {
        addAll("--app ${firebasePublisherParams.firebaseProjectId}".toCommandArguments())
    }

    private fun ShipTaskArguments.addToken() = toMutableList().apply {
        addAll("--token ${firebasePublisherParams.firebaseToken}".toCommandArguments())
    }

    private fun ShipTaskArguments.addDistributionGroups(): ShipTaskArguments =
        when (val groups = firebasePublisherParams.distributionGroup) {
            null -> this
            else -> toMutableList().apply {
                addAll("--groups $groups".toCommandArguments())
            }
        }

    /**
     * We do not support spaces on the notes, so we need to replace those
     * for snake case string
     */
    private fun ShipTaskArguments.addReleaseNotes(): ShipTaskArguments {
        val notes = firebasePublisherParams.notes?.replace(SPACE, SNAKE) ?: return this

        return toMutableList().apply {
            addAll("--release-notes $notes".toCommandArguments())
        }
    }

    private fun BuildType.getReleasePathFromBuildType() =
        "$PATH_FIRST_PATH${toLowerCase()}/app-${toLowerCase()}.$BUILD_EXTENSION"
}

private const val SNAKE = "_"
private const val SPACE = " "
private const val BUILD_EXTENSION = "apk"
private const val PATH_FIRST_PATH = "app/build/outputs/apk/"
private const val FIREBASE_COMMAND_NAME = "firebase"
private const val PROPERTIES_FILE = "local.properties"

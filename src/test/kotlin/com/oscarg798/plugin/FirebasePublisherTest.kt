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

import com.oscarg798.plugin.commandexecutrors.ShipCommandExecutor
import com.oscarg798.plugin.tasks.plublisher.FirebasePublisher
import com.oscarg798.plugin.tasks.plublisher.FirebasePublisherParams
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldEqual
import org.junit.Test

class FirebasePublisherTest {


    private lateinit var publisher: FirebasePublisher
    private val shipCommandExecutor = mockk<ShipCommandExecutor>(relaxed = true)

    @Test
    fun `given params with all the values when get arguments is called then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS)
        publisher.getParams() shouldEqual ARGUMENTS_WITH_ALL_PARAMS.split(" ")
    }

    @Test
    fun `given params without distribution group when get arguments invoke then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(distributionGroup = null))
        publisher.getParams() shouldEqual ARGUMENTS_WITH_NOTES_BUT_WITHOUT_DISTRIBUTION_GROUP.toArguments()
    }

    @Test
    fun `given params without notes when get arguments invoke then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(notes = null))
        publisher.getParams() shouldEqual ARGUMENTS_WITH_DISTRIBUTION_GROUP_WITHOUT_NOTES.toArguments()
    }

    @Test
    fun `given params without notes and distribution group when get arguments invoke then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(notes = null, distributionGroup = null))
        publisher.getParams() shouldEqual ARGUMENTS_WITHOUT_NOTES_AND_DISTRIBUTION_GROUP.toArguments()
    }

    @Test
    fun `given params when run is invoke then it should execute the command`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS)

        publisher.run()
        verify { shipCommandExecutor.execute("firebase", ARGUMENTS_WITH_ALL_PARAMS.toArguments(), 10) }
    }


    @Test
    fun `given params with all the values and flavor when get arguments is called then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(flavor = FLAVOR))
        publisher.getParams() shouldEqual ARGUMENTS_WITH_ALL_PARAMS_AND_FLAVOR.split(" ")
    }

    @Test
    fun `given params without distribution group but with flavor and flavor when get arguments is called then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(flavor = FLAVOR, distributionGroup = null))
        publisher.getParams() shouldEqual ARGUMENTS_WITH_NOTES_BUT_WITHOUT_DISTRIBUTION_GROUP_BUT_WITH_FLAVOR.split(" ")
    }

    @Test
    fun `given params without notes but with flavor and flavor when get arguments is called then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(flavor = FLAVOR, notes = null))
        publisher.getParams() shouldEqual ARGUMENTS_WITH_DISTRIBUTION_GROUP_WITHOUT_NOTES_BUT_WITH_FLAVOR.split(" ")
    }

    @Test
    fun `given params without notes and distribution group but with flavor and flavor when get arguments is called then it should output the right params`() {
        publisher = FirebasePublisher(shipCommandExecutor, PARAMS.copy(flavor = FLAVOR, notes = null, distributionGroup = null))
        publisher.getParams() shouldEqual ARGUMENTS_WITHOUT_NOTES_AND_DISTRIBUTION_GROUP_BUT_WITH_FLAVOR.split(" ")
    }


    private fun String.toArguments() = split(" ")
}

private const val FLAVOR = "fLavoR"
private const val PROJECT_NAME = "Good_Name"

//ARGUMENTS WITHOUT FLAVOR
private const val ARGUMENTS_WITH_ALL_PARAMS =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/debug/$PROJECT_NAME-debug.apk --app 2 --token 1 --groups 3 --release-notes 4_4"
private const val ARGUMENTS_WITH_NOTES_BUT_WITHOUT_DISTRIBUTION_GROUP =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/debug/$PROJECT_NAME-debug.apk --app 2 --token 1 --release-notes 4_4"
private const val ARGUMENTS_WITH_DISTRIBUTION_GROUP_WITHOUT_NOTES =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/debug/$PROJECT_NAME-debug.apk --app 2 --token 1 --groups 3"
private const val ARGUMENTS_WITHOUT_NOTES_AND_DISTRIBUTION_GROUP =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/debug/$PROJECT_NAME-debug.apk --app 2 --token 1"

//ARGUMENTS WITH FLAVOR
private const val ARGUMENTS_WITH_ALL_PARAMS_AND_FLAVOR =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/flavor/debug/$PROJECT_NAME-flavor-debug.apk --app 2 --token 1 --groups 3 --release-notes 4_4"
private const val ARGUMENTS_WITH_NOTES_BUT_WITHOUT_DISTRIBUTION_GROUP_BUT_WITH_FLAVOR =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/flavor/debug/$PROJECT_NAME-flavor-debug.apk --app 2 --token 1 --release-notes 4_4"
private const val ARGUMENTS_WITH_DISTRIBUTION_GROUP_WITHOUT_NOTES_BUT_WITH_FLAVOR =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/flavor/debug/$PROJECT_NAME-flavor-debug.apk --app 2 --token 1 --groups 3"
private const val ARGUMENTS_WITHOUT_NOTES_AND_DISTRIBUTION_GROUP_BUT_WITH_FLAVOR =
    "appdistribution:distribute $PROJECT_NAME/build/outputs/apk/flavor/debug/$PROJECT_NAME-flavor-debug.apk --app 2 --token 1"

private val PARAMS = FirebasePublisherParams(
    projectName = PROJECT_NAME,
    buildType = "debug",
    firebaseToken = "1",
    firebaseProjectId = "2",
    distributionGroup = "3",
    notes = "4 4"
)
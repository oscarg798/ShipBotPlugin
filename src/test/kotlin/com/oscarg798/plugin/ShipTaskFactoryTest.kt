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
import com.oscarg798.plugin.tasks.buildgenerator.BuildGenerator
import com.oscarg798.plugin.tasks.factory.ShipTaskFactory
import com.oscarg798.plugin.tasks.factory.taksbuilders.paramfinders.BuildTypeParamFinder
import com.oscarg798.plugin.tasks.factory.taksbuilders.paramfinders.FlavorParamFinder
import com.oscarg798.plugin.tasks.plublisher.FirebasePublisher
import com.oscarg798.plugin.tasks.unittestrunner.UnitTestRunner
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

internal class ShipTaskFactoryTest {

    private lateinit var factory: ShipTaskFactory
    private val properties: Map<String, *> = mockk()
    private val extension: ShipBotPluginExtension = mockk()
    private val buildTypeFinder = BuildTypeParamFinder(extension)
    private val flavorFinder = FlavorParamFinder(extension)

    @Before
    fun setup() {
        every { extension.flavors } answers { SUPPORTED_FLAVORS }
        every { extension.unitTestRequired } answers { true }
        every { extension.firebaseToken } answers { TOKEN }
        every { extension.buildTypes } answers { SUPPORTED_BUILD_TYPES }
        every { properties["flavor"] } answers { null }
        every { properties["builtType"] } answers { SUPPORTED_BUILD_TYPES.first() }
        every { properties["firebaseProjectId"] } answers { PROJECT_ID }
        every { properties["distributionGroup"] } answers { DISTRIBUTION_GROUP }
        every { properties["notes"] } answers { NOTES }
        factory = ShipTaskFactory(PROJECT_NAME, properties, extension, buildTypeFinder, flavorFinder)

    }

    @Test
    fun `when its executed with test then it should return a tasks containing tests, build generation and publisher `() {
        val tasks = factory.createTasks().toList()

        tasks.size shouldEqual 3
        (tasks.first() is UnitTestRunner) shouldEqual true
        (tasks[1] is BuildGenerator) shouldEqual true
        (tasks[2] is FirebasePublisher) shouldEqual true
    }

    @Test
    fun `when its executed without tests then it should return a tasks containing build generation and publisher`() {
        every { extension.unitTestRequired } answers { false }
        val tasks = factory.createTasks().toList()

        tasks.size shouldEqual 2
        (tasks.first() is BuildGenerator) shouldEqual true
        (tasks[1] is FirebasePublisher) shouldEqual true
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when no buildType is provided then it should crash`() {
        every { properties["builtType"] } answers { null }

        factory.createTasks()
    }

    @Test(expected = IllegalStateException::class)
    fun `when firebase project id  is  not provided then it should crash`() {
        every { properties["firebaseProjectId"] } answers { null }

        factory.createTasks()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when firebase  token is  not provided then it should crash`() {
        every { extension.firebaseToken } answers { null }

        factory.createTasks()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when wrong build type is provided then it should crash`() {
        every { properties["builtType"] } answers { "feo" }

        factory.createTasks()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when no build type is provided then it should crash`() {
        every { properties["builtType"] } answers { null }

        factory.createTasks()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when wrong flavor is provided then it should crash`() {
        every { properties["flavor"] } answers { "feo" }

        factory.createTasks()
    }
}

private const val PROJECT_NAME = "app"
private const val PROJECT_ID = "1"
private const val TOKEN = "token"
private const val BUILD_TYPE = "debug"
private const val DISTRIBUTION_GROUP = "1"
private const val NOTES = "1"
private val SUPPORTED_BUILD_TYPES = listOf(BUILD_TYPE)
private val SUPPORTED_FLAVORS = listOf("flavor")

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
import com.oscarg798.plugin.tasks.buildgenerator.BuildGenerator
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

internal class BuildGeneratorTest {

    private lateinit var buildGenerator: BuildGenerator
    private val shipCommandExecutor = mockk<ShipCommandExecutor>(relaxed = true)

    @Before
    fun setup() {
        buildGenerator = BuildGenerator(shipCommandExecutor, BUILD_TYPE)
    }

    @Test
    fun `given a build type when get arguments is invoke then it should return the right output`() {
        buildGenerator.getParams() shouldEqual ARGUMENTS
    }

    @Test
    fun `when run is invoke then it should call with the right commands`() {
        buildGenerator.run()

        verify {
            shipCommandExecutor.execute("./gradlew",ARGUMENTS)
        }
    }
}

private val ARGUMENTS = listOf("assembleMytype")
private const val BUILD_TYPE = "mYtYpE"
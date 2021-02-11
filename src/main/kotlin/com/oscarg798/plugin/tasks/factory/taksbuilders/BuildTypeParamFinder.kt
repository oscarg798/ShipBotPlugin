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

import com.oscarg798.plugin.extension.ShipBotPluginExtension
import com.oscarg798.plugin.utils.BuildType

internal class BuildTypeParamFinder(private val shipBotPluginExtension: ShipBotPluginExtension) {

    fun getBuildType(properties: Map<String, *>): BuildType {
        val buildType = properties[BUILD_TYPE_PARAM_NAME]?.toString()
            ?: error("To run this task you should provide a builtType param to the task")

        if (shipBotPluginExtension.buildTypes.contains(buildType)) {
            return buildType
        }

        throw IllegalArgumentException(
            "Build type is not valid, valid build type: ${
                shipBotPluginExtension.buildTypes.joinToString(
                    BUILD_TYPE_SEPARATOR
                )
            }"
        )
    }

}

private const val BUILD_TYPE_SEPARATOR = ","
private const val BUILD_TYPE_PARAM_NAME = "builtType"
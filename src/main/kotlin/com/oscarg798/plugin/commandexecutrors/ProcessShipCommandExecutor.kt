/*
 * Copyright (c) 2021 Oscar David Gallon Rosero
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oscarg798.plugin.commandexecutrors

import com.oscarg798.plugin.utils.Command
import com.oscarg798.plugin.utils.ProcessStreamer
import com.oscarg798.plugin.utils.ShipTaskArguments
import java.util.concurrent.TimeUnit

internal class ProcessShipCommandExecutor : ShipCommandExecutor {

    private val processBuilder = ProcessBuilder(listOf())
        .directory(null)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)

    override fun execute(command: Command, arguments: ShipTaskArguments, timeOutIntMinutes: Long) {
        val splitCommand = listOf(command) + arguments

        val process = getProcess(splitCommand)

        streamCommandOutPut(process)

        process.waitFor(timeOutIntMinutes, TimeUnit.MINUTES)
        if (process.exitValue() != NORMAL_EXIT_VALUE) throw RuntimeException()
    }

    private fun getProcess(splitCommand: List<Command>) = processBuilder
        .command(splitCommand)
        .start()

    private fun streamCommandOutPut(process: Process) {
        val stream = ProcessStreamer(process.inputStream)
        val errorStream = ProcessStreamer(process.errorStream)

        stream.start()
        errorStream.start()
    }

}

private const val NORMAL_EXIT_VALUE = 0
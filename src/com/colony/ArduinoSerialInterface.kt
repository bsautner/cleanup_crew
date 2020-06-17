package com.colony

import com.colony.model.Command
import com.colony.model.CommandType
import com.colony.serial.SerialPortIO
import com.colony.serial.SerialPortListener
import com.google.gson.GsonBuilder
import java.io.IOException
import java.lang.Exception
import java.util.*

/**
 * get serial data from arduino connected to usb port and post feedback to main program
 */
class ArduinoSerialInterface {
    private var serial: SerialPortIO? = null
    private val gson = GsonBuilder().create()
    private val random = Random()

    private var moved = false

    fun startListening() {
        serial = SerialPortIO(object : SerialPortListener {
            override fun onRead(rawData: String) {
//                println("read: $rawData")

                if (rawData.length > 0) {
                    val data = rawData.split('\n')
                    for (s in data) {
                        val cleanString = s.trim('\r')
                        when {
                            cleanString.isEmpty() -> {
                                return
                            }
                            cleanString.startsWith("DEBUG::") -> {
                               // println(cleanString)
                            }
                            else -> {
                                try {

                                    val command = gson.fromJson(cleanString, Command::class.java)


                                    val commandType: CommandType? = CommandType.lookup(command.id)

                                    when (commandType) {
                                        CommandType.SONAR -> {
                                           // println("Sonar Range: ${command.value}")
                                            if (!moved && command.value < 10) {
                                                val r = random.nextInt(160)
                                                println("obstacle detected moving to $r")
                                                moved = true
                                                sendCommand(Command(CommandType.SEEK.id, "", r))
                                            } else if (command.value > 10) {
                                                moved = false
                                            }
                                        }
                                        CommandType.PING -> {
                                            println("PONG $cleanString")
                                        }
                                        CommandType.SEEK -> TODO()
                                        CommandType.CALIBRATE -> TODO()
                                        null -> TODO()
                                    }


                                } catch (ex : Exception) {
                                    println("error processing input: $cleanString")
                                }
                            }
                        }
                    }
                }







            }

            override fun onError(throwable: Throwable) {
                throwable.printStackTrace()
            }
        })
        serial!!.start()
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }



    @Throws(IOException::class)
    fun sendCommand(command: Command) {
        val gson = GsonBuilder().create()
        val cmd = gson.toJson(command)
        val commandType = CommandType.lookup(command.id)
      //  println("sending $commandType command $cmd")
        serial!!.write(cmd)
        serial!!.write("\n")
        Thread.sleep(1000)

    }
}
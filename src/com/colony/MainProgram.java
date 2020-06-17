package com.colony;

import com.colony.model.Command;
import com.colony.model.CommandType;
import com.pi4j.util.Console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainProgram {


    private static MainProgram mainProgram;
    private static Console console;

    private ArduinoSerialInterface arduinoSerialInterface;


    private Map<Integer, Servo> servos = new HashMap<>();



    public void initHardware() {
        arduinoSerialInterface = new ArduinoSerialInterface();
    }


    public static void main(String... args) throws InterruptedException, IOException {

        console = new Console();
        mainProgram = new MainProgram();



        console.promptForExit();


        console.box("Starting Up Main Program");
        mainProgram.initHardware();
        mainProgram.start();


        while (console.isRunning()) {
            Thread.sleep(1000);
            mainProgram.arduinoSerialInterface.sendCommand(new Command(CommandType.PING.getId(), "", 0));

        }






    }


    private void start() throws IOException, InterruptedException {

        arduinoSerialInterface.startListening();
        Thread.sleep(1000);
        arduinoSerialInterface.sendCommand(new Command(CommandType.CALIBRATE.getId(), "", 0));

       }



















}

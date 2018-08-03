package com.colony.serial;// START SNIPPET: serial-snippet




import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

import java.io.IOException;


public class SerialTest {

    public static void main(String args[]) throws InterruptedException, IOException {

        final Console console = new Console();


        console.promptForExit();

        final Serial serial = SerialFactory.createInstance();

        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                try {
                    console.println(event.getAsciiString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            SerialConfig config = new SerialConfig();


            config.device("/dev/ttyACM0")
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            if(args.length > 0){
                config = CommandArgumentParser.getSerialConfig(config, args);
            }

            console.box(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serial port every 1 second.",
                    " Data received on serial port will be displayed below.");

            serial.open(config);

           while(console.isRunning()) {
                Thread.sleep(1000);
            }

        }
        catch(IOException ex) {
            console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
        }
    }
}

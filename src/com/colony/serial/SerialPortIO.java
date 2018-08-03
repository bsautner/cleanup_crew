package com.colony.serial;

import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;

import java.io.IOException;

public class SerialPortIO {

    private final SerialPortListener listener;
    private final String tty;

    public SerialPortIO(String tty, SerialPortListener listener) {
        this.listener = listener;
        this.tty = tty;
    }


    public void start() {
        final Serial serial = SerialFactory.createInstance();



        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                try {
                    listener.onRead(event.getAsciiString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            SerialConfig config = new SerialConfig();


            config.device("/dev/" + tty)
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

//            if(args.length > 0){
//                config = CommandArgumentParser.getSerialConfig(config, args);
//            }

//            console.box(" Connecting to: " + config.toString(),
//                    " We are sending ASCII data on the serial port every 1 second.",
//                    " Data received on serial port will be displayed below.");

            serial.open(config);

//            while(console.isRunning()) {
//                Thread.sleep(1000);
//            }

        }
        catch(IOException ex) {
          listener.onError(ex);
        }
    }

}

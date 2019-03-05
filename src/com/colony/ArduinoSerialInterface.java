package com.colony;


import com.colony.sensor.SensorEvent;
import com.colony.sensor.Sonar;
import com.colony.serial.SerialPortIO;
import com.colony.serial.SerialPortListener;
import com.colony.servo.Devices;
import io.reactivex.Observer;

/**
 * get serial data from arduino connected to usb port and post feedback to main program
 */

public class ArduinoSerialInterface {





    public void startListening(Observer<SensorEvent> observer) {

        SerialPortIO serialPortIO = new SerialPortIO("ttyACM0", new SerialPortListener() {
            @Override
            public void onRead(String rawData) {


                String[] lines = rawData.split("\n");


                for (String line : lines) {

                    try {

                        line = line.replace("\r\n", "").trim();
                        if (line.startsWith("[") && line.endsWith("]")) {
                            line = line.substring(1, line.length() - 1);

                            String[] device = line.split(":");
                            switch (device[0]) {

                                case "sonarA0": {
                                    String cm = device[1]
                                            .replace("cm", "");
                                    int newReading = Integer.valueOf(cm);

                                    if (newReading != Devices.sonarA0.getDistanceToObject()) {
                                        Devices.sonarA0.setDistanceToObject(newReading);
                                        observer.onNext(SensorEvent.sonarDistanceMeasured);
                                    }
                                    break;
                                }
                                case ("servoA0"): {
                                    int newReading = Integer.valueOf(device[1]);
                                    if (Devices.servoA0.getCurrentPosition() != newReading) {
                                        Devices.servoA0.setCurrentPosition(newReading);
                                        observer.onNext(SensorEvent.servoMoved);
                                    }
                                    break;
                                }
                                case("servoB0") :
                                {
                                    int newReading = Integer.valueOf(device[1]);
                                    if (Devices.servoB0.getCurrentPosition() != newReading) {
                                        Devices.servoB0.setCurrentPosition(newReading);
                                        observer.onNext(SensorEvent.servoMoved);
                                    }

                                    break;
                                }





                            }


                        } else {
                            System.out.println("ignoring malformed line: " + line);
                        }


                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {

                throwable.printStackTrace();
            }
        });

        serialPortIO.start();

    }


    //BEN TODO - don't delete this - it has some logic in getting the alalog feedback from servos
//    SerialPortListener serialPortListener = new SerialPortListener() {
//        @Override
//        public void onRead(String rawData) {
//
//            String[] parts = rawData.split(",");
//            //   console.print(new Date().toString() +  " - serial data in - " + rawData);
//
//            for (String s : parts) {
//                s = s.replace("[", "").replace("]", "");
//                //     console.println(s);
//                String[] m = s.split(":");
//                if (m.length == 2) {
//
//                    if (m[0].equals("X")) {
//                        double d = Double.valueOf(m[1]) * 100;
//
//                        Integer v = (int) d;
//                        console.println("change in x " + v);
//                        if (! v.equals(lastX)) {
//
//                            lastX = v;
//
//                            int newPos = lastXPos + v;
//                            if (newPos < -90) {
//                                newPos = -90;
//                            }
//                            else if (newPos > 90) {
//                                newPos = 90;
//                            }
//
//                            lastXPos= newPos;
//                            console.println("moving on X " + lastXPos);
//                            servoController.angle(LF_Y, newPos).subscribe();
//                        }
//                    }
//
//                }
//            }
//
//
//        }
//
//        @Override
//        public void onError(Throwable throwable) {
//            console.print(throwable.getMessage());
//        }
//    };


}

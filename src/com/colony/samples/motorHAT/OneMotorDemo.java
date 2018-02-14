package com.colony.samples.motorHAT;

import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import static com.colony.utils.TimeUtil.delay;

/**
 * DC Motor demo
 *
 * I used motors like https://www.adafruit.com/product/2941
 * or https://www.adafruit.com/product/711
 */
public class OneMotorDemo {
	// The I2C address of the motor HAT, default is 0x60.
	private int addr = 0x60;
	// The ID of the left motor, default is 1.
	private static AdafruitMotorHAT.Motor motorID = AdafruitMotorHAT.Motor.M3;
	// Amount to offset the speed of the left motor, can be positive or negative and use
	// useful for matching the speed of both motors.  Default is 0.
	private int trim = 0;

	private AdafruitMotorHAT mh;
	private AdafruitMotorHAT.AdafruitDCMotor motor;

	public OneMotorDemo() throws I2CFactory.UnsupportedBusNumberException {
		this.mh = new AdafruitMotorHAT();
		this.motor = mh.getMotor(motorID);
		try {
			this.motor.run(AdafruitMotorHAT.ServoCommand.RELEASE);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		// if stopOnExit...
	}

	public void stop() {
		try {
			this.motor.run(AdafruitMotorHAT.ServoCommand.RELEASE);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void setSpeed(int speed) throws IllegalArgumentException, IOException {
		if (speed < 0 || speed > 255) {
			throw new IllegalArgumentException("Speed must be an int belonging to [0, 255]");
		}
		int leftSpeed = speed + this.trim;
		leftSpeed = Math.max(0, Math.min(255, leftSpeed));
		this.motor.setSpeed(leftSpeed);
	}


	public void forward(int speed) throws IOException {
		forward(speed, 0);
	}

	public void forward(int speed, long seconds) throws IOException {
		this.motor.setSpeed(speed);
		this.motor.run(AdafruitMotorHAT.ServoCommand.FORWARD);
		if (seconds > 0) {
			delay(seconds);
			this.stop();
		}
	}

	public void backward(int speed) throws IOException {
		backward(speed, 0);
	}

	public void backward(int speed, long seconds) throws IOException {
		this.motor.setSpeed(speed);
		this.motor.run(AdafruitMotorHAT.ServoCommand.BACKWARD);
		if (seconds > 0) {
			delay(seconds);
			this.stop();
		}
	}

	public static void main(String... args) throws Exception {

		if (args.length > 0){
			try {
				int motorNum = Integer.parseInt(args[0]);
				switch (motorNum) {
					case 1:
						System.out.println("Using M1");
						OneMotorDemo.motorID = AdafruitMotorHAT.Motor.M1;
						break;
					case 2:
						System.out.println("Using M2");
						OneMotorDemo.motorID = AdafruitMotorHAT.Motor.M2;
						break;
					case 3:
						System.out.println("Using M3");
						OneMotorDemo.motorID = AdafruitMotorHAT.Motor.M3;
						break;
					case 4:
						OneMotorDemo.motorID = AdafruitMotorHAT.Motor.M4;
						System.out.println("Using M4");
						break;
					default:
						System.out.println("Between 1 and 4 only... Keeping default (1).");
						break;
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}

		OneMotorDemo omd = new OneMotorDemo();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Oops!");
			omd.stop();
		}));

		int speed = 100; // 0..255
		System.out.println("Forward 100...");
		omd.forward(speed, 5);
		System.out.println("Backward 100...");
		omd.backward(speed, 5);

		speed = 50;
		System.out.println("Forward 50...");
		omd.forward(speed, 5);
		System.out.println("Backward 50...");
		omd.backward(speed, 5);

		// Speed variation test
		System.out.println("Accelerating...");
		for (speed=0; speed<=255; speed++) {
			System.out.println(String.format("Speed %d", speed));
			omd.forward(speed);
			delay(1);
		}
		System.out.println("De-celarating...");
		for (speed=255; speed>=0; speed--) {
			System.out.println(String.format("Speed %d", speed));
			omd.forward(speed);
			delay(1);
		}
		omd.stop();

		System.out.println("Done.");
	}
}

///**
// * 
// */
//package ca.hapke.gyro;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//
//import jpigpio.PigpioException;
//import jpigpio.PigpioSocket;
//import jpigpio.Utils;
//
///*
// * From MPU6050.h:
// * 
//#define MPU6050_RA_ACCEL_XOUT_H     0x3B
//#define MPU6050_RA_ACCEL_XOUT_L     0x3C
//#define MPU6050_RA_ACCEL_YOUT_H     0x3D
//#define MPU6050_RA_ACCEL_YOUT_L     0x3E
//#define MPU6050_RA_ACCEL_ZOUT_H     0x3F
//#define MPU6050_RA_ACCEL_ZOUT_L     0x40
//#define MPU6050_RA_TEMP_OUT_H       0x41
//#define MPU6050_RA_TEMP_OUT_L       0x42
//#define MPU6050_RA_GYRO_XOUT_H      0x43
//#define MPU6050_RA_GYRO_XOUT_L      0x44
//#define MPU6050_RA_GYRO_YOUT_H      0x45
//#define MPU6050_RA_GYRO_YOUT_L      0x46
//#define MPU6050_RA_GYRO_ZOUT_H      0x47
//#define MPU6050_RA_GYRO_ZOUT_L      0x48
// */
//
///**
// * @author Mr. Hapke
// */
//public class GyroHelloWorld {
//
//	private static final int DEVICE_ID = 0x68;
//	private static final int BUS_ID = 1;
//
//	public static void main(String[] args) throws PigpioException {
//		new GyroHelloWorld();
//
//	}
//
//	private PigpioSocket pig;
//
//	public GyroHelloWorld() throws PigpioException {
//		pig = new PigpioSocket("127.0.0.1", 8888);
//		pig.gpioInitialize();
//		Utils.addShutdown(pig);
//
//		int i2cHandle = pig.i2cOpen(BUS_ID, DEVICE_ID);
//		System.out.println("Open: " + i2cHandle);
//
//		byte[] data = new byte[256];
//		for (int i = 0; i < 100; i++) {
//			System.out.println(i);
//			System.out.println();
//			int read = pig.i2cReadDevice(i2cHandle, data);
//
//			ByteBuffer bb = ByteBuffer.allocate(7 * 2);
//			bb.order(ByteOrder.LITTLE_ENDIAN);
//			// bb.order(ByteOrder.BIG_ENDIAN);
//			for (int j = 0x3B; j <= 0x47; j++) {
//				bb.put(data[j]);
//			}
//
//			short aX = bb.getShort(0);
//			short aY = bb.getShort(1);
//			short aZ = bb.getShort(2);
//
//			short gX = bb.getShort(4);
//			short gY = bb.getShort(5);
//			short gZ = bb.getShort(6);
//
//			// short t = bb.getShort(3);
//
//			System.out.println("Accel: (" + aX + "," + aY + "," + aZ + ")");
//			System.out.println("Gyro : (" + gX + "," + gY + "," + gZ + ")");
//			System.out.println();
//			System.out.println();
//
//			try {
//				Thread.sleep(250);
//			} catch (InterruptedException e) {
//				break;
//			}
//		}
//
//		pig.i2cClose(i2cHandle);
//		System.out.println("Closed" + i2cHandle);
//	}
//}

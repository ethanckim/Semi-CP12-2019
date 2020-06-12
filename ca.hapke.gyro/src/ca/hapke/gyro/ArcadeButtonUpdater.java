package ca.hapke.gyro;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import ca.hapke.gyro.data.ArcadeButtonDataType;
import ca.hapke.gyro.data.DataCluster;
import ca.hapke.gyro.data.DataType.InputType;

/**
 * @author Mr. Hapke
 *
 */
public class ArcadeButtonUpdater {
	private final GpioController gpio = GpioFactory.getInstance();
	private ArcadeButtonDataType status;
	private Pin[] pins;
	private GpioPinDigitalInput[] inputs;

	private class DigitalPinListener implements GpioPinListenerDigital {
		private final int pin;

		private DigitalPinListener(int pin) {
			this.pin = pin;
		}

		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

			System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

			PinMode mode = event.getPin().getMode();
			boolean value = mode.getValue() >= 1;

			status.updateByIndex(pin, value);
		}

	}

	public ArcadeButtonUpdater(DataCluster cluster) {
		this.status = (ArcadeButtonDataType) cluster.getData(InputType.ArcadeButton);
		pins = new Pin[] { RaspiPin.GPIO_00, RaspiPin.GPIO_11 };
		inputs = new GpioPinDigitalInput[pins.length];

		for (int i = 0; i < pins.length; i++) {
			Pin pin = pins[i];
			GpioPinDigitalInput digitalInput = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
			digitalInput.setShutdownOptions(true);
			digitalInput.addListener(new DigitalPinListener(i));
			inputs[i] = digitalInput;
		}
	}
}

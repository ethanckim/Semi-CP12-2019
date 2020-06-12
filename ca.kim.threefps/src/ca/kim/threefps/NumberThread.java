/**
 * 
 */
package ca.kim.threefps;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * @author ethanc.kim
 *
 */
public class NumberThread extends Thread {

	private static final int FRAME_RATE = 3;
	
	protected boolean killThread = false;
	private NumberMode mode;
	private int n = 0;
	private int step = 0;
	private JTextField txtValue;
	private JTextField txtSteps;
	

	/**
	 * Constructor
	 * @param txtSteps 
	 * @param txtValue 
	 */
	public NumberThread(JTextField txtValue, JTextField txtSteps, NumberMode mode) {
		super("Number Thread");
		this.txtValue = txtValue;
		this.txtSteps = txtSteps;
		this.mode = mode;
	}
//arvin is a god
	@Override
	public void run() {
		while (!killThread) {
			switch (mode) {
			case MinusTwo:
				n -= 2;
				break;
			case PlusThree:
				n += 3;
				break;
			case Randomize:
				n += (int) ((Math.random() * 41) - 21);
				break;			
			}
			step++;
			
			try {
				Thread.sleep(1000 / FRAME_RATE);
			} catch (InterruptedException e) {
				killThread = true;
			}
			
			txtValue.setText(Integer.toString(n));
			txtSteps.setText(Integer.toString(step));
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			killThread = true;
		}
		
			txtValue.setText(Integer.toString(0));
			txtSteps.setText(Integer.toString(0));
		
	}

	public void setMode(NumberMode mode) {
		this.mode = mode;
	}
	
	
	public void setKillThread(boolean killThread) {
		this.killThread = killThread;
	}

}

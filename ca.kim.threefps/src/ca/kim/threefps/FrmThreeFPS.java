package ca.kim.threefps;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;

public class FrmThreeFPS extends JFrame {

	private JPanel contentPane;
	private JTextField txtValue;
	private JTextField txtSteps;
	private NumberThread numberThread;
	private final ButtonGroup modesGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmThreeFPS frame = new FrmThreeFPS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmThreeFPS() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 306, 266);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCurrentValue = new JLabel("Current Value:");
		lblCurrentValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCurrentValue.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblCurrentValue.setBounds(39, 30, 83, 19);
		contentPane.add(lblCurrentValue);
		
		JLabel lblCurrentSteps = new JLabel("Current Steps:");
		lblCurrentSteps.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCurrentSteps.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblCurrentSteps.setBounds(39, 57, 83, 24);
		contentPane.add(lblCurrentSteps);
		
		JLabel lblCurrentMode = new JLabel("Current Mode:");
		lblCurrentMode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCurrentMode.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblCurrentMode.setBounds(39, 92, 83, 19);
		contentPane.add(lblCurrentMode);
		
		txtValue = new JTextField();
		txtValue.setBackground(Color.WHITE);
		txtValue.setEditable(false);
		txtValue.setText("0");
		txtValue.setFont(new Font("Calibri", Font.PLAIN, 14));
		txtValue.setBounds(148, 29, 86, 20);
		contentPane.add(txtValue);
		txtValue.setColumns(10);
		
		txtSteps = new JTextField();
		txtSteps.setBackground(Color.WHITE);
		txtSteps.setEditable(false);
		txtSteps.setText("0");
		txtSteps.setFont(new Font("Calibri", Font.PLAIN, 14));
		txtSteps.setBounds(148, 59, 86, 20);
		contentPane.add(txtSteps);
		txtSteps.setColumns(10);
		
		JRadioButton rdbtnPlusThree = new JRadioButton("+3");
		rdbtnPlusThree.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rdbtnPlusThree.isSelected() && numberThread != null)
					numberThread.setMode(NumberMode.PlusThree);
			}
		});
		modesGroup.add(rdbtnPlusThree);
		rdbtnPlusThree.setSelected(true);
		rdbtnPlusThree.setFont(new Font("Calibri", Font.PLAIN, 14));
		rdbtnPlusThree.setBounds(148, 92, 109, 23);
		contentPane.add(rdbtnPlusThree);
		
		JRadioButton rdbtnMinusTwo = new JRadioButton("-2");
		rdbtnMinusTwo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rdbtnMinusTwo.isSelected() && numberThread != null)
					numberThread.setMode(NumberMode.MinusTwo);
			}
		});
		modesGroup.add(rdbtnMinusTwo);
		rdbtnMinusTwo.setFont(new Font("Calibri", Font.PLAIN, 14));
		rdbtnMinusTwo.setBounds(148, 118, 109, 23);
		contentPane.add(rdbtnMinusTwo);
		
		JRadioButton rdbtnRandomize = new JRadioButton("rand(-20,20)");
		rdbtnRandomize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rdbtnRandomize.isSelected() && numberThread != null)
					numberThread.setMode(NumberMode.Randomize);
			}
		});
		modesGroup.add(rdbtnRandomize);
		rdbtnRandomize.setFont(new Font("Calibri", Font.PLAIN, 14));
		rdbtnRandomize.setBounds(148, 144, 109, 23);
		contentPane.add(rdbtnRandomize);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (numberThread == null || numberThread.killThread == true) {
					
					NumberMode mode;
					if (rdbtnMinusTwo.isSelected())
						mode = NumberMode.MinusTwo;
					else if (rdbtnPlusThree.isSelected())
						mode = NumberMode.PlusThree;
					else
						mode = NumberMode.Randomize;
					
					numberThread = new NumberThread(txtValue, txtSteps, mode);
					numberThread.start();
				}
			}
		});
		btnStart.setFont(new Font("Calibri", Font.PLAIN, 14));
		btnStart.setBounds(33, 179, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (numberThread == null) return;
				numberThread.setKillThread(true);
			}
		});
		btnStop.setFont(new Font("Calibri", Font.PLAIN, 14));
		btnStop.setBounds(168, 179, 89, 23);
		contentPane.add(btnStop);
	}
}

package ca.kim.helloworld;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class frmMain extends JFrame {

	private JPanel contentPane;
	private JTextField txtDigits;
	private JSpinner spnInput;
	private JTextArea txtSong;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmMain frame = new frmMain();
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
	public frmMain() {
		
		JTextArea textArea = new JTextArea();
		getContentPane().add(textArea, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 419);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDigits = new JLabel("Digits:");
		lblDigits.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDigits.setBounds(36, 69, 39, 14);
		contentPane.add(lblDigits);
		
		txtDigits = new JTextField();
		txtDigits.setBackground(Color.WHITE);
		txtDigits.setEditable(false);
		txtDigits.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtDigits.setBounds(85, 67, 222, 20);
		contentPane.add(txtDigits);
		txtDigits.setColumns(10);
		
		JButton btnNewButton = new JButton("Go!");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/**
				 * input from the spinner object in integer type
				 */
				int intinput = (int) (spnInput.getValue());
				
				/**
				 * input from the spinner object in string type
				 */
				String strinput = Integer.toString(intinput);
				
				//code for digits
				String digits = "";
				for (int i = 0; i < strinput.length() ; i++) {
					
					char current = strinput.charAt(i);
					switch (current) {
						case '1': digits = digits + "one";
							break;
						case '2': digits = digits + "two";
							break;
						case '3': digits = digits + "three";
							break;
						case '4': digits = digits + "four";
							break;
						case '5': digits = digits + "five";
							break;
						case '6': digits = digits + "six";
							break;
						case '7': digits = digits + "seven";
							break;
						case '8': digits = digits + "eight";
							break;
						case '9': digits = digits + "nine";
							break;
						case '0': digits = digits + "zero";
							break;
					}
					
					digits = digits + " ";
				}
				
				txtDigits.setText(digits);
				
				
				//code for song
				String song = "";
				for (int i = intinput; i > 0; i--) {
					song = song + Integer.toString(i) + " bottles of beer on the wall!" + "\n";
					txtSong.setText(song);
				}
				
			}
		});
		btnNewButton.setBounds(255, 23, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblInput = new JLabel("Input a Number:");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInput.setBounds(24, 21, 104, 25);
		contentPane.add(lblInput);
		
		spnInput = new JSpinner();
		spnInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spnInput.setBounds(134, 24, 60, 20);
		contentPane.add(spnInput);
		
		JLabel lblSong = new JLabel("Song:");
		lblSong.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSong.setBounds(36, 108, 46, 14);
		contentPane.add(lblSong);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(79, 108, 295, 192);
		contentPane.add(scrollPane);
		
		txtSong = new JTextArea();
		txtSong.setEditable(false);
		txtSong.setBackground(Color.WHITE);
		txtSong.setForeground(new Color(0, 0, 0));
		txtSong.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(txtSong);
		
		JLabel lblNewLabel = new JLabel("Hello World!");
		lblNewLabel.setForeground(Color.ORANGE);
		lblNewLabel.setFont(new Font("Cambria", Font.PLAIN, 30));
		lblNewLabel.setBounds(99, 311, 185, 54);
		contentPane.add(lblNewLabel);
	}
}

package ca.kim.notepad;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class FrmFind extends JFrame {

	private JPanel contentPane;
	private JTextField txtFind;
	private JTextField txtReplace;
	protected boolean replaceAll;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmFind frame = new FrmFind();
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
	public FrmFind() {
		setTitle("Find");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 380, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblFindWhat = new JLabel("Find What:");
		lblFindWhat.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFindWhat.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFindWhat.setBounds(10, 11, 63, 24);
		contentPane.add(lblFindWhat);

		txtFind = new JTextField();
		txtFind.setBounds(83, 12, 271, 24);
		contentPane.add(txtFind);
		txtFind.setColumns(10);

		JButton btnFindNext = new JButton("Find Next");
		btnFindNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String text = FrmNotepad.txtText.getText();
				String find = txtFind.getText();
				String replace = txtReplace.getText();

				if (replaceAll) {
					text = text.replaceAll(find, replace);
				} else {
					text = text.replaceFirst(find, replace);
				}
				FrmNotepad.txtText.setText(text);
			}
		});
		btnFindNext.setBounds(166, 87, 89, 23);
		contentPane.add(btnFindNext);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmFind.this.setVisible(false);
			}
		});
		btnClose.setBounds(265, 87, 89, 23);
		contentPane.add(btnClose);
		
		JLabel lblReplace = new JLabel("Replace:");
		lblReplace.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReplace.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblReplace.setBounds(10, 46, 63, 24);
		contentPane.add(lblReplace);
		
		txtReplace = new JTextField();
		txtReplace.setColumns(10);
		txtReplace.setBounds(83, 49, 271, 24);
		contentPane.add(txtReplace);
		
		JCheckBox chckbxReplaceAll = new JCheckBox("Replace All");
		chckbxReplaceAll.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (replaceAll) replaceAll = false;
				else replaceAll = true;
			}
		});
		chckbxReplaceAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chckbxReplaceAll.setBounds(25, 87, 97, 23);
		contentPane.add(chckbxReplaceAll);
	}
}

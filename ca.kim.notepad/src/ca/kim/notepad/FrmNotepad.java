package ca.kim.notepad;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

public class FrmNotepad extends JFrame {

	private JPanel contentPane;
	
	//Public so FrmFind can access text
	public static JTextArea txtText;
	
	//File directory to save to.
	private File lastfile;
	
	//File Changed
	private boolean fileChanged = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmNotepad frame = new FrmNotepad();
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
	public FrmNotepad() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (fileChanged) {
					int choose;
					if (lastfile == null) {
						choose = JOptionPane.showConfirmDialog(FrmNotepad.this, "Want to save current changes to New File?");
					} else {
						choose = JOptionPane.showConfirmDialog(FrmNotepad.this, "Want to save current changes to " + lastfile.getName() + "?");
					}
					if (choose == JOptionPane.YES_OPTION) {
						if (lastfile == null) {
							saveas();
						} else {
							save(lastfile);
						}
						setDefaultCloseOperation(EXIT_ON_CLOSE);
					} else if (choose == JOptionPane.NO_OPTION) {
						setDefaultCloseOperation(EXIT_ON_CLOSE);
					} else if (choose == JOptionPane.CANCEL_OPTION) {
						setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					}
				}
			}
		});
		setTitle("Notepad - New File");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 480);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtText.setText("");
				lastfile = null;
				fileChanged = false;
				setTitle("Notepad - New File");
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (fileChanged) {
					
					int choose;
					if (lastfile == null) {
						choose = JOptionPane.showConfirmDialog(FrmNotepad.this, "Want to save current changes to New File?");
					} else {
						choose = JOptionPane.showConfirmDialog(FrmNotepad.this, "Want to save current changes to " + lastfile.getName() + "?");
					}
					if (choose == JOptionPane.YES_OPTION) {
						if (lastfile == null) {
							saveas();
						} else {
							save(lastfile);
						}
						setDefaultCloseOperation(EXIT_ON_CLOSE);
					} else if (choose == JOptionPane.NO_OPTION) {
						open();
					}
					
				} else {
					open();
				}

			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lastfile == null) {
				} else {
					save(lastfile);
				}
				
			}
		});
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveas();
			}
		});
		mnFile.add(mntmSaveAs);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                    			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmFind = new JMenuItem("Find/Replace");
		mntmFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        new FrmFind().setVisible(true);
		        changeTitleSave();
			}
		});
		mnEdit.add(mntmFind);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5 , 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtText = new JTextArea();
		txtText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				changeTitleSave();
			}
		});
		txtText.setFont(new Font("Source Code Pro", Font.PLAIN, 14));
		txtText.setBounds(10, 11, 684, 398);
		txtText.setLineWrap(true);
		contentPane.add(txtText);
	}

	private void save(File f) {
		String text = txtText.getText();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));

			writer.write(text);
			writer.close();
			fileChanged = false;
			
		} catch (IOException e1) {
			txtText.setText("ERROR 2: An error happened while saving.");
		}
	}

	private void saveas() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Documents (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(FrmNotepad.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			lastfile = chooser.getSelectedFile();	
			save(lastfile);
			setTitle("Notepad - " + lastfile.getName());
		}
	}

	private void open() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Documents (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(FrmNotepad.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			lastfile = chooser.getSelectedFile();
			fileChanged = false;
			String text = "";
			String wholeFile = "";
			try {
				BufferedReader reader = new BufferedReader(new FileReader(lastfile));
				
				while (true) {
					text = reader.readLine();
					if (text == null) break;
					wholeFile = wholeFile +  text + "\r\n";	
				}
				
				reader.close();
				setTitle(lastfile.getName());
			} catch (FileNotFoundException e1) {
				wholeFile = "ERROR 404: File does not exist";
			} catch (IOException e1) {
				wholeFile = "ERROR 1: The system can't read the file, or the file is corrupted";
			}
			txtText.setText(wholeFile);
			setTitle("Notepad - " + lastfile.getName());
		}
	}

	private void changeTitleSave() {
		fileChanged = true;
		if (lastfile == null) {
			setTitle("Notepad - New File*");
		} else {
			setTitle("Notepad - " + lastfile.getName() + "*");
		}
	}
	
}

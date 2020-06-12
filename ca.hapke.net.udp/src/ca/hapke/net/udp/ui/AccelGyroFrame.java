package ca.hapke.net.udp.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.hapke.gyro.data.DataCluster;

/**
 * 
 * @author Mr. Hapke
 */
public abstract class AccelGyroFrame extends JFrame {

	private static final long serialVersionUID = 8381316893140758524L;
	protected RotatingCube pnlCube;
	protected DataCluster cluster = new DataCluster();

//	protected AngleFormatter af;

	protected JPanel contentPane;

	public AccelGyroFrame(int xBase, int yBase, int fWidth, int fHeight) {
//		af = new AngleFormatter();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, fWidth, fHeight);

	}

	public void addElements(int xBase, int yBase) {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		pnlCube = new RotatingCube(cluster);
		pnlCube.setBounds(10, 100, 780, 360);
		contentPane.add(pnlCube);

	}
}
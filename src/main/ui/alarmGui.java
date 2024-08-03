package ui;

import model.Alarmclock;
import model.Alarm;
import model.Riddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;



public class alarmGui extends JFrame {
	//private static Color PINK;
	//private static final String STATUS = "All alarms set!";
	private Alarmclock alarmClock;
	private JLabel statusLabel;
	private JTextArea alarmListArea;
	private JButton addButton;
	private JButton simulateAlarmButton;

	// sets up the frame and the title label of the frame
	// https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
	public alarmGui() {
		JFrame frame = new JFrame("AlarmClock");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setTitle("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");
		//frame.pack();
		frame.setDefaultLookAndFeelDecorated(true);
		frame.setSize(1000,800);
		frame.setVisible(true);
		frame.getContentPane().setBackground(Color.PINK);
		frame.getContentPane().createImage(500,400);
		
		JLabel heading = new JLabel("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");
		heading.setFont(new Font("Times New Roman", Font.BOLD, 25));
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add(heading, BorderLayout.NORTH);
	}

}



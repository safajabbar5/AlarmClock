package ui;

import model.Alarmclock;
import model.Alarm;
import model.Riddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class alarmGui extends JFrame {
	private static final String STATUS = "All alarms set!";
	private Alarmclock alarmClock;
	private JLabel statusLabel;
	private JTextArea alarmListArea;
	private JButton addButton;
	private JButton simulateAlarmButton;

	public alarmGui() {
		super("Alarm Clock UI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		alarmClock = new Alarmclock();
		statusLabel = new JLabel(STATUS);
		add(statusLabel, BorderLayout.NORTH);
	}

}

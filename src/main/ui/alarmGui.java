package ui;

import model.Alarmclock;
import model.Alarm;
import model.Riddle;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AlarmGui extends JFrame {
    private static final String JSON_STORE = "./data/alarmClock.json";
    private Alarmclock alarmClock;
    private JButton alarmButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton exitButton;
    private Scanner scanner;
    private Riddle riddle;

    private JFrame frame;
    private JLabel heading;
    private JLabel timeNow;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private ArrayList<Riddle> riddles;
    private RiddlesReviewer reviewer;
    boolean correctAnswer = false;
    private Clip clip;
    private Alarm currentalarm;

    // sets up the frame and the title AlarmClock
    // https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
    public AlarmGui() {
        alarmClock = new Alarmclock();
        riddles = new ArrayList<>();

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        readyRiddles();

        frame = new JFrame("AlarmClock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);

        frame.getContentPane().setBackground(Color.PINK);
        setAlarmCheckTime();
        setScreenWithTime();
        setAllButton();
        addingActiontoButton();
        // setAlarmCheckTime();

        frame.setVisible(true);
    }

    // EFFECTS: Set up the Jframe with the local time on laptop and a title
    private void setScreenWithTime() {
        JLabel heading = new JLabel("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");
        heading.setFont(new Font("Times New Roman", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(heading, BorderLayout.NORTH);

        // set up the time on the frame
        timeNow = new JLabel();
        timeNow.setFont(new Font("Times New Roman", Font.BOLD, 100));
        timeNow.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(timeNow, BorderLayout.CENTER);

        // HomeTab class PlaceButton method from the SmartHome application
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalTime now = LocalTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                timeNow.setText(now.format(timeFormatter));
            }
        });
        timer.start();

    }

    // Everything in the bottom is taken by the HomeTab java class from the
    // SmartHome project
    // EFFECTS: constructs a home tab for console with buttons and a greeting
    public void setAllButton() {
        JPanel buttoPanel = new JPanel();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        loadButton = new JButton("Load Alarm");
        saveButton = new JButton("View Current Alarms");
        exitButton = new JButton("Exit Applciation");
        alarmButton = new JButton("Set Alarm!!");

        loadButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        saveButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        exitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        alarmButton.setFont(new Font("Times New Roman", Font.BOLD, 20));

        buttoPanel.add(loadButton);
        buttoPanel.add(alarmButton);
        buttoPanel.add(saveButton);
        buttoPanel.add(exitButton);

        buttoPanel.add(Box.createHorizontalStrut(15));

        frame.add(buttoPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAlarmClock();
            }
        });

    }

    private void addingActiontoButton() {


        alarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAlarmTime();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAlarms();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

    }

    // EFFECTS: a riddle list with riddles for the user to solve and end their
    // alarm.
    public void readyRiddles() {
        riddles.add(new Riddle(
                "I speak without a mouth and hear without ears.I have no body, but I come alive with wind. What am I?",
                "echo"));
        riddles.add(new Riddle(
                "You measure my life in hours and I serve you by expiring.The wind is my enemy.",
                "candle"));
        riddles.add(new Riddle(
                "I have keys, but no locks and rooms, you can enter, but you can’t leave. What am I?",
                "keyboard"));
        riddles.add(new Riddle("This belongs to you, but everyone else uses it.", "name"));
        riddles.add(new Riddle("The more there is, the less you see.", "darkness"));
        riddles.add(new Riddle("Nobody has ever walked this way. Which way is it?", "milkyway"));
        riddles.add(new Riddle("What can go through glass without breaking it?", "light"));
        riddles.add(new Riddle("What has a face and two hands but no arms or legs?", "clock"));
        riddles.add(new Riddle("What creature is smarter than a talking parrot", "spelling bee"));
        riddles.add(new Riddle("What is orange and sounds like a parrot", "carrot"));
    }

    private void setAlarmTime() {
        boolean setMoreAlarms = true;

        while (setMoreAlarms) {
            String hourInput = JOptionPane
                    .showInputDialog("Enter the time (hour) for your alarm, please choose between (0-23)");
            int hour = Integer.parseInt(hourInput);
            String minInput = JOptionPane
                    .showInputDialog("Enter the time (minutes) for your alarm, please choose between (0-59)");
            int min = Integer.parseInt(minInput);

            currentalarm = new Alarm(hour, min);
            alarmClock.setCurrentAlarm(currentalarm);
            alarmClock.addAlarm(currentalarm);

            String response = JOptionPane.showInputDialog("Do you want to set another alarm? (yes/no)");
            if (response.equals("no")) {
                String savingAlarm = JOptionPane.showInputDialog("Do you want to save your alarms? (yes/no)");
                if (savingAlarm.equals("yes")) {
                    savecurrentAlarm();
                }
                setMoreAlarms = false;
            }
            setAlarmCheckTime();
        }
        getAlarms();
        setAlarmCheckTime();
    }

    private void alarmSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // alarmSound("data/resources/morning_flower.wav");

    }

    // EFFECTS: prints the set alarms that the user has added to the alarm clock
    private void getAlarms() {
        List<Alarm> alarms = alarmClock.getAlarms();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (Alarm alarm : alarms) {
            listModel.addElement(alarm.getHours() + ":" + alarm.getMinutes());
        }
        JList<String> alarmList = new JList<>(listModel);

        alarmList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        alarmList.setLayoutOrientation(JList.VERTICAL_WRAP);
        alarmList.setVisibleRowCount(-1);

        JScrollPane scrollPane = new JScrollPane(alarmList);
        scrollPane.setPreferredSize(new Dimension(50, 150));
        setBackground(Color.DARK_GRAY);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel text = new JLabel("Alarms waiting to ring!");
        text.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        panel.add(text, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel, BorderLayout.EAST);

    }

    private void setAlarmCheckTime() {
        Timer alarmCheckTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAlarm();
            }
        });
        alarmCheckTimer.start();
    }

    private void checkAlarm() {
        LocalTime now = LocalTime.now();
        List<Alarm> alarms = alarmClock.getAlarms();
        alarms.removeIf(alarm -> {
            if (alarm.getHours() == now.getHour() && alarm.getMinutes() == now.getMinute()) {
                Riddle riddle = getRandomRiddle();
                String riddleanswer = JOptionPane.showInputDialog(riddle.getQuestion());
                while (!correctAnswer) {
                    if (riddle.checkRiddleAnswer(riddleanswer)) {
                        JOptionPane.showMessageDialog(frame, "Correct! Alarm stopped.");
                        correctAnswer = true;
                        break;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Correct! Alarm stopped.");
                    }
                }
            }
            return false;
        });
    }

    // JOptionPane.getRootFrame().dispose();

    // JOptionPane.showMessageDialog(frame,"Solve this riddle to stop the alarm");
    // JOptionPane.getRootFrame().dispose();

    // boolean correctAnswer = false;
    // while (!correctAnswer) {
    // Riddle riddle = getRandomRiddle();
    // String riddleanswer = JOptionPane.showInputDialog(riddle.getQuestion());
    // if (riddleanswer == null) {
    // JOptionPane.showMessageDialog(frame, "Must solve the riddle to stop the
    // alarm");
    // } else if (riddle.checkRiddleAnswer(riddleanswer)) {
    // JOptionPane.showMessageDialog(frame, "Correct! Alarm stopped.");
    // correctAnswer = true;
    // return true;
    // } else {
    // JOptionPane.showMessageDialog(frame, "Incorrect! Try again.");
    // }
    // }
    // }
    // return false;
    // });
    // }

    // private void checkAlarm() {
    // LocalTime now = LocalTime.now();
    // List<Alarm> alarms = alarmClock.getAlarms();
    // alarms.removeIf(alarm -> { // lambda expression to determine which alarm to
    // remove from list
    // if (alarm.getHours() == now.getHour() && alarm.getMinutes() ==
    // now.getMinute()) {
    // Riddle riddle = getRandomRiddle();
    // boolean correctAnswer = false;

    // while (!correctAnswer) {
    // String riddleAnswer = JOptionPane.showInputDialog(frame, "Solve this riddle "
    // +
    // riddle.getQuestion(),
    // "Input",
    // JOptionPane.QUESTION_MESSAGE);
    // if (riddle.checkRiddleAnswer(riddleAnswer)) {
    // JOptionPane.showMessageDialog(frame, "Correct! Alarm stopped.");
    // correctAnswer = true;
    // } else {
    // JOptionPane.showMessageDialog(frame, "Incorrect! Try again.");
    // }
    // }
    // }
    // }
    // return false;
    // }

    // EFFECTS: randomly get a riddle from the readyriddle list of riddles
    // used diceGame from lecture lab as a reference
    Riddle getRandomRiddle() {
        Random random = new Random();
        int index = random.nextInt(riddles.size());
        return riddles.get(index);

    }

    private void stopAlarm() {
        if (!correctAnswer) {
            clip.stop();

        }
    }

    // MODIFIES: this
    // EFFECTS: saves the set alarms to file
    // used the jsonSerializationDemo as a reference
    private void savecurrentAlarm() {
        try {
            if (alarmClock.getAlarms().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No alarms to save.");
            }
            jsonWriter.open();
            jsonWriter.write(alarmClock);
            jsonWriter.close();
            JOptionPane.showMessageDialog(frame, "Saved alarm clock to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Unable to write to file: " + JSON_STORE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "Please load the file before saving it");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads set alarms from file
    // used the jsonSerializationDemo as a reference
    private void loadAlarmClock() {
        try {
            alarmClock = jsonReader.read();
            System.out.println("Loaded alarm clock from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to load alarm clock from " + JSON_STORE);
        }
    }

}

// private void displayImage(String imagePath) {
// BufferedImage img;
// try {
// img = ImageIO.read(new File(imagePath));
// ImageIcon icon = new ImageIcon(img);
// JLabel imagelabel = new JLabel(icon);
// JOptionPane.showMessageDialog(frame, imagelabel, "Alarm Ringing",
// JOptionPane.PLAIN_MESSAGE);

// } catch (IOException e) {
// e.printStackTrace();
// }

// }
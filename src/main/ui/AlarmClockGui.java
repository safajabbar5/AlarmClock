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

// A class that represents the alarm clock on a GUI
public class AlarmClockGui extends JFrame {
    private JFrame frame;
    private JLabel timeNow;
    private JLabel imagelabel;
    private JButton alarmButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton exitButton;
    private JButton removeButton;
    private Clip clip;

    private Alarm currentalarm;
    private Alarmclock alarmClock;
    private ArrayList<Riddle> riddles;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/alarmClock.json";

    boolean correctAnswer = false;
    Boolean alarmRinging = false;
    private boolean setMoreAlarms;

    // sets up the frame and the title AlarmClock
    // https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
    public AlarmClockGui() {
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

        frame.setVisible(true);
    }

    // Got help from
    // https://docs.oracle.com/javase/tutorial/uiswing/components/label.html
    // EFFECTS: Set up the Jframe with the local time on laptop and a title
    private void setScreenWithTime() {
        JLabel heading = new JLabel("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");
        heading.setFont(new Font("Times New Roman", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(heading, BorderLayout.NORTH);

        timeNow = new JLabel();
        timeNow.setFont(new Font("Times New Roman", Font.BOLD, 100));
        timeNow.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(timeNow, BorderLayout.CENTER);

        // Got help from
        // https://stackoverflow.com/questions/13366780/how-to-add-real-time-date-and-time-into-a-jframe-component-e-g-status-bar
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

    // Got help from SmartHome application in the HomeTab class, the PlaceButton
    // method
    // Got help from
    // https://docs.oracle.com/javase/tutorial/uiswing/components/button.html
    // Got help from
    // https://docs.oracle.com/javase/tutorial/uiswing/components/button.html#contents
    // EFFECTS: constructs a home tab for console with buttons and a greeting
    public void setAllButton() {
        JPanel buttoPanel = new JPanel();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        loadButton = new JButton("Load Alarm");
        saveButton = new JButton("View Current Alarms");
        exitButton = new JButton("Exit Application");
        alarmButton = new JButton("Set Alarm!!");
        removeButton = new JButton("Remove Alarms");

        loadButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        saveButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        exitButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        alarmButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        removeButton.setFont(new Font("Times New Roman", Font.BOLD, 20));

        buttoPanel.add(loadButton);
        buttoPanel.add(alarmButton);
        buttoPanel.add(saveButton);
        buttoPanel.add(exitButton);
        buttoPanel.add(removeButton);

        buttoPanel.add(Box.createHorizontalStrut(15));

        frame.add(buttoPanel, BorderLayout.SOUTH);

    }

    // EFFECTS: adds action to the buttons added on the frame
    // MODIFIES: this
    private void addingActiontoButton() {

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAlarmClock();
            }
        });

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
    }

    // EFFECTS: continue the addActionButton method
    // MODIFIES: this
    private void continueActiontoButton() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAlarm();
            }
        });
    }

    // used this for reference https://www.geeksforgeeks.org/java-joptionpane/
    // MODIFIES: this
    // EFFECTS: asks the user to set their time for their alarm,
    // then the set alarms sent to the method getAlarms()
    // and then to the setAlarmCheckTime()
    private void setAlarmTime() {
        setMoreAlarms = true;
        while (setMoreAlarms) {
            try {
                String hourInput = JOptionPane
                        .showInputDialog("Enter the time (hour) for your alarm, please choose between (0-23)");
                int hour = Integer.parseInt(hourInput); // converts string to int
                String minInput = JOptionPane
                        .showInputDialog("Enter the time (minutes) for your alarm, please choose between (0-59)");
                int min = Integer.parseInt(minInput);

                Alarm currentalarm = new Alarm(hour, min);
                alarmClock.setCurrentAlarm(currentalarm);
                alarmClock.addAlarm(currentalarm);

                continueSetAlarm();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        }
    }

    // EFFECTS: continuing on the setAlarm method
    private void continueSetAlarm() {
        try {
            String response = JOptionPane.showInputDialog("Do you want to set another alarm? (yes/no)");
            if (response.equals("no") || response.equals("n") || response.equals("NO")) {
                String savingAlarm = JOptionPane.showInputDialog("Do you want to save your alarms? (yes/no)");
                if (savingAlarm.equals("yes") || response.equals("y") || response.equals("YES")) {
                    savecurrentAlarm();
                }
                setMoreAlarms = false;
            }
            setAlarmCheckTime();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input");
        }
        getAlarms();
        setAlarmCheckTime();
    }

    // EFFECTS: sets up a timer that checks if the alarm should paly or not
    private void setAlarmCheckTime() {
        Timer alarmCheckTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAlarm();
            }
        });
        alarmCheckTimer.start();
    }

    // MODIFIES: this
    // EFFECTS: checks if the set alarm matches the time on laptop, if yes then
    // display the ringing image
    // and play the alarm, also present a riddle for the question for the user to
    // solve
    private void checkAlarm() {
        LocalTime now = LocalTime.now();
        List<Alarm> alarms = alarmClock.getAlarms();

        for (int asize = 0; asize < alarms.size(); asize++) {
            Alarm alarm = alarms.get(asize);
            if (alarm.getHours() == now.getHour() && alarm.getMinutes() == now.getMinute() && !alarmRinging) {
                alarmRinging = true;
                alarmSound("data/resources/morning_flower.wav");
                displayImage("data/resources/riniging.png");
                riddleQuestion(alarm);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: presents a riddle to the user and stops the alarm if answered
    private void riddleQuestion(Alarm alarm) {
        Random riddlegenerator = new Random();
        Riddle riddle = riddles.get(riddlegenerator.nextInt(riddles.size()));

        while (!correctAnswer) {
            String riddleAnswer = JOptionPane.showInputDialog(riddle.getQuestion());

            if (riddleAnswer != null && riddleAnswer.equals(riddle.getAnswer())) {
                correctAnswer = true;
                stopAlarm();
                frame.remove(imagelabel);
                JOptionPane.showMessageDialog(frame, "Correct! Alarm stopped.");
                alarmClock.removeAlarm(alarm);

            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect! Try again.");
            }
        }
        correctAnswer = false;
        alarmRinging = false;
    }

    // EFFECTS: prints the set alarms that the user has added to the alarm clock
    // used for help https://www.geeksforgeeks.org/java-swing-jlist-with-examples/
    // used for help
    // https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
    // used for help
    // https://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
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

    // MODIFIES: this
    // EFFECTS: removes the alarm the user wants to remove form the set alarm list
    private void removeAlarm() {
        List<Alarm> alarms = alarmClock.getAlarms();
        String rhour = JOptionPane.showInputDialog("Enter the alarm hour that you would like to remove");
        String rmin = JOptionPane.showInputDialog("Enter the alarm Mintue that you would like to remove");
        try {
            int hour = Integer.parseInt(rhour);
            int min = Integer.parseInt(rmin);
            boolean alarmRemoved = false;
            for (Alarm alarm : alarms) {
                if (alarm.getHours() == hour && alarm.getMinutes() == min) {
                    alarms.remove(alarm);
                    alarmRemoved = true;
                    break;
                }
            }
            if (alarmRemoved) {
                JOptionPane.showMessageDialog(frame, "Alarm " + hour + ":" + min + " sucessfully removed");
            } else {
                JOptionPane.showMessageDialog(frame, "No matching alarm");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input");
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the set alarms to file
    // used the jsonSerializationDemo (TellerApp) as a reference
    private void savecurrentAlarm() {
        try {
            if (alarmClock.getAlarms().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No alarms to save.");
            }
            jsonWriter.open();
            jsonWriter.write(alarmClock);
            jsonWriter.close();
            JOptionPane.showMessageDialog(frame, "Successfully Saved Alarms");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Unable to write to file: " + JSON_STORE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "Please load the file before saving it");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads set alarms from file
    // used the jsonSerializationDemo (TellerApp) as a reference
    private void loadAlarmClock() {
        try {
            alarmClock = jsonReader.read();
            JOptionPane.showMessageDialog(frame, "Successfully Loaded Alarms clock");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to load alarm clock from " + JSON_STORE);
        }
    }

    // EFFECTS: creates an alarm sound
    // Used the alarmSiren() method in AlarmSystem application as a reference
    // Got help from
    // https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
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

    }

    // EFFECTS: stops the alarm sound playing
    // Used the alarmSiren() method in AlarmSystem application as a reference
    private void stopAlarm() {
        if (clip != null && clip.isRunning()) {
            clip.stop();

        }
    }

    // EFFECTS: display thsi image when the alarm is rining
    // Got help from https://www.codespeedy.com/how-to-add-an-image-in-jframe/
    private void displayImage(String imagePath) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(imagePath));
            ImageIcon icon = new ImageIcon(img);
            imagelabel = new JLabel(icon);
            JOptionPane.showMessageDialog(frame, imagelabel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: a riddle list with riddles for the user to solve and end their
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
}



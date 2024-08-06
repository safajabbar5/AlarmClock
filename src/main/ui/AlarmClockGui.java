package ui;

import model.Alarmclock;
import model.JsonReader;
import model.JsonWriter;
import model.Alarm;
import model.Riddle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 
    private JLabel imagelabel;
    private JButton alarmButton;
    private JButton loadButton;
    private JButton viewButton;
    private JButton exitButton;
    private JButton removeButton;
    private JButton saveButton;
    private Clip clip;


    private Alarmclock alarmClock;
    private ArrayList<Riddle> riddles;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/alarmClock.json";

    private boolean correctAnswer = false;
    private Boolean alarmRinging = false;
    private boolean setMoreAlarms;
    private boolean isSaved;
    private boolean alarmRemoved;
    
    

    // EFFECTS: sets up the Jframe with the helper methods and the title AlarmClock
    // Got help from https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
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
        setScreenWithTime();
        setAllButton();
        addingActiontoButton();
        setAlarmCheckTime();
        

        frame.setVisible(true);
    }


    // EFFECTS: set up the JLabel a welcome title and a timer that represents the local time on laptop
    // Got help from
    // https://docs.oracle.com/javase/tutorial/uiswing/components/label.html
    private void setScreenWithTime() {
        JLabel heading = new JLabel("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");
        heading.setFont(new Font("Times New Roman", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(heading, BorderLayout.NORTH);

        JLabel timeNow = new JLabel();
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

    // EFFECTS: constructs 6 buttons on the frame 
    // Got help from SmartHome application in the HomeTab class, the PlaceButton() method
    // Also got help from
    // https://docs.oracle.com/javase/tutorial/uiswing/components/button.html
    public void setAllButton() {
        JPanel buttonPanel = new JPanel();

        loadButton = new JButton("Load Alarm");
        viewButton = new JButton("View Current Alarms");
        exitButton = new JButton("Exit Application");
        alarmButton = new JButton("Set Alarm!!");
        removeButton = new JButton("Remove Alarms");
        saveButton = new JButton("Save Alarms");
        

        loadButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        viewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        exitButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        alarmButton.setFont(new Font("Times New Roman", Font.BOLD,16));
        removeButton.setFont(new Font("Times New Roman", Font.BOLD,16));
        saveButton.setFont(new Font("Times New Roman", Font.BOLD, 16));

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(alarmButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(removeButton);
        

        buttonPanel.add(Box.createHorizontalStrut(15));

        frame.add(buttonPanel, BorderLayout.SOUTH);

    }

    // MODIFIES: this
    // EFFECTS: adds action to the buttons added on the frame
    // Got help from https://www.tutorialspoint.com/how-to-add-action-listener-to-jbutton-in-java
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
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAlarms();
            }
        });
        continueActiontoButton();
    }

     // MODIFIES: this
    // EFFECTS: continue the addActionButton method to add action to remaning buttons
    // Got help from https://www.tutorialspoint.com/how-to-add-action-listener-to-jbutton-in-java
    private void continueActiontoButton() {
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAlarm();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savecurrentAlarm();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    
    // MODIFIES: this
    // EFFECTS: asks the user to set their time for their alarm,
    //          then the set alarms sent to the method getAlarms()
    //          and then to the setAlarmCheckTime()
    // Got help from https://www.geeksforgeeks.org/java-joptionpane/
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

                continueSetAlarmTime();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        }
    }

    // EFFECTS: continuing on the setAlarm method
    //          asks the user to set another alarm, and if they want to save their alarm
    // Got help from https://www.geeksforgeeks.org/java-joptionpane/
    private void continueSetAlarmTime() {
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

    // EFFECTS: sets up a timer that checks if the alarm should play or not
    // Got help from 
    // https://stackoverflow.com/questions/13366780/how-to-add-real-time-date-and-time-into-a-jframe-component-e-g-status-bar
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
    //          display the image, and play the alarm sound
    //          also present a riddle for the question for the user to solve 
    private void checkAlarm() {
        LocalTime now = LocalTime.now();
        List<Alarm> alarms = alarmClock.getAlarms();

        for (int a = 0; a < alarms.size(); a++) {
            Alarm alarm = alarms.get(a);
            if (alarm.getHours() == now.getHour() && alarm.getMinutes() == now.getMinute() && !alarmRinging) {
                alarmRinging = true;
                alarmSound("data/resources/morning_flower.wav");
                displayImage("data/resources/riniging.png");
                riddleQuestion(alarm);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: presents a riddle Question to the user and stops the alarm if answered correctly
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
    // Got help from https://www.geeksforgeeks.org/java-swing-jlist-with-examples/
    // Got help from https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
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
        String rhour = JOptionPane.showInputDialog("Enter the alarm hour that you would like to remove");
        String rmin = JOptionPane.showInputDialog("Enter the alarm Minutes that you would like to remove");
        try {
            int hour = Integer.parseInt(rhour);
            int min = Integer.parseInt(rmin);
            List<Alarm> alarms = alarmClock.getAlarms();
            for (Alarm alarm : alarms) {
                if (alarm.getHours() == hour && alarm.getMinutes() == min) {
                    alarms.remove(alarm);
                    alarmRemoved = true;
                    if (isSaved) {
                        savecurrentAlarm();
                    }
                    break;
                }
            }
            if (alarmRemoved) {
                JOptionPane.showMessageDialog(frame, "Alarm " + hour + ":" + min + " sucessfully removed");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input, No matching alarm");
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the set alarms to file
    // used the jsonSerializationDemo as a reference (same as Phase 2)
    private void savecurrentAlarm() {
        try {
            jsonWriter.open();
            jsonWriter.write(alarmClock);
            jsonWriter.close();
            isSaved = true;
            JOptionPane.showMessageDialog(frame, "Successfully Saved!");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads set alarms from file
    // used the jsonSerializationDemo as a reference (same as Phase 2)
    private void loadAlarmClock() {
        try {
            alarmClock = jsonReader.read();
            JOptionPane.showMessageDialog(frame, "Successfully Loaded!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to load alarm clock from " + JSON_STORE);
        }
    }

    // EFFECTS: creates an alarm sound
    // Used the alarmSiren() method in AlarmSystem application as a reference
    // Also got help from
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
    // Also got help from
    // https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
    private void stopAlarm() {
        if (clip != null && clip.isRunning()) {
            clip.stop();

        }
    }

    // EFFECTS: display this image when the alarm is ringing
    // Got help from https://stackoverflow.com/questions/18027833/adding-image-to-jframe
    private void displayImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        imagelabel = new JLabel(icon);
        JOptionPane.showMessageDialog(frame, imagelabel);
    }

    // EFFECTS: a riddle list with riddles for the user to solve and end their alarm
    public void readyRiddles() {
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



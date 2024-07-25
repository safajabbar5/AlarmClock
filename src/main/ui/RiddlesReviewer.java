package ui;

import model.Riddle;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;
import model.Alarmclock;
import model.Alarm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

//  A riddle application that allows user to answer the riddle and stop the alarm
public class RiddlesReviewer {
    
    // used the jsonSerializationDemo as a reference
    private static final String JSON_STORE = "./data/alarmClock.json";
    private Alarm currentalarm;
    private Alarmclock alarmClock;
    private List<Riddle> riddles;
    private Scanner scanner;
    private boolean isAlarmRunning;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: creates an instance of the RiddlesReviewer console ui application
    // used lab 3 as a reference
    public RiddlesReviewer() {
        this.currentalarm = null;
        this.alarmClock = new Alarmclock();
        this.scanner = new Scanner(System.in);
        this.isAlarmRunning = true;
        this.riddles = new ArrayList<>();
        // used the jsonSerializationDemo as a reference
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        readyRiddles();

        printSnoozes();
        System.out.println("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");

        handleMenu();

        while (this.isAlarmRunning) {
            checkAlarm();
        }
    }

    //EFFECTS: a riddle list with riddles for the user to solve and end their alarm.
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

    // EFFECTS: displays and processes inputs for the main menu
    public void handleMenu() {
        displayMenu();
        String input = this.scanner.nextLine();
        processMenuCommands(input);
    }

    // EFFECTS: displays a list of commands that can be used in the main menu
    public void displayMenu() {
        System.out.println("Please select an option:");
        System.out.println("1) Set an alarm in a 24-hour clock");
        System.out.println("2) Save alarms to file");
        System.out.println("3) Load alarms from file");
        System.out.println("4) Exit the application");
        printSnoozes();
    }

    // EFFECTS: processes the user's input in the main menu
    public void processMenuCommands(String input) {
        printSnoozes();
        switch (input) {
            case "1":
                setAlarmTime();
                break;
            case "2":
                savecurrentAlarm();
                handleMenu();
                break;
            case "3":
                loadAlarmClock();
                handleMenu();
                break;
            case "4":
                isAlarmRunning = false;
                System.out.println("Exiting the application");
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
        printSnoozes();
    }

    // EFFECTS: sets the alarm time based on the user input
    private void setAlarmTime() {
        boolean setMoreAlarms = true;

        while (setMoreAlarms) {
            System.out.println("Enter the time (hour) for your alarm, please choose between (0-23)");
            int hour = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the time (minutes) for your alarm, please choose between (0-59)");
            int min = Integer.parseInt(scanner.nextLine());

            currentalarm = new Alarm(hour, min);
            alarmClock.setCurrentAlarm(currentalarm);
            alarmClock.addAlarm(currentalarm);
            System.out.println("Setting alarm for " + hour + " : " + min);
            printSnoozes();

            System.out.println("Do you want to set another alarm? (yes/no)");
            String userRes = scanner.nextLine();
            if (userRes.equals("no")) {
                setMoreAlarms = false;
            }
            checkAlarm();
        }
        getAlarms();
        savingandloadingAlarm();
        checkAlarm();

    }

    // MODIFIES: this
    // EFFECTS: saves set alarm based on the users input
    private void savingandloadingAlarm() {
        System.out.println("Do you want to save your alarm (yes/no)");
        String userRes = scanner.nextLine();
        if (userRes.equals("yes")) {
            savecurrentAlarm();
            lastMessage();
        } else {
            getAlarms();
            lastMessage();
        }

    }

    // EFFECTS: makes a last message after the user has been given an option to save their alarm
    private void lastMessage() {
        System.out.println("Your alarmclock will ring, when it is time to wake you up");
        System.out.println("Good Night!! :)");
    }

    // EFFECTS: prints the set alarms that the user has added to the alarm clock
    private void getAlarms() {
        List<Alarm> alarms = alarmClock.getAlarms();
        printSnoozes();
        System.out.println("Here are your current set of alarms");
        for (Alarm alarm : alarms) {
            System.out.println(alarm.getHours() + ":" + alarm.getMinutes());
        }

    }

    // MODIFIES: this
    // EFFECTS: checks if the alarm set is the same as the local time,
    // if it is the same then alarm should ring, and display a riddle for the user to
    // solve. If the user solves the riddle then stop the alarm, if not then keep playing
    // the alarm.
    private void checkAlarm() {
        LocalTime now = LocalTime.now();
        List<Alarm> alarms = alarmClock.getAlarms();
        alarms.removeIf(alarm -> {   // lambda expression to determine which alarm to remove from list
            if (alarm.getHours() == now.getHour() && alarm.getMinutes() == now.getMinute()) {
                System.out.println("YOUR ALARM IS RINGING!");
                System.out.println("Solve this riddle to stop the alarm\"");
                Riddle riddle = getRandomRiddle();
                boolean correctAnswer = false;
                while (!correctAnswer) {
                    System.out.println(riddle.getQuestion());
                    String answer = scanner.nextLine();
                    if (riddle.checkRiddleAnswer(answer)) {
                        System.out.println("Correct! Alarm stopped.");
                        correctAnswer = true;
                        return true;
                    } else {
                        System.out.println("Incorrect! Try again.");
                    }
                }
            }
            return false;
        });
    }

    // EFFECTS: randomly get a riddle from the readyriddle list of riddles
    // used diceGame from lecture lab as a reference
    private Riddle getRandomRiddle() { 
        Random random = new Random();
        int index = random.nextInt(riddles.size());
        return riddles.get(index);

    }

    // EFFECTS: prints out a line of snoozes to act as a divider
    private void printSnoozes() {
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
    }

    // MODIFIES: this
    // EFFECTS: saves the set alarms to file
    // used the jsonSerializationDemo as a reference
    private void savecurrentAlarm() {
        try {
            jsonWriter.open();
            jsonWriter.write(alarmClock);
            jsonWriter.close();
            System.out.println("Saved alarm clock to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
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
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}

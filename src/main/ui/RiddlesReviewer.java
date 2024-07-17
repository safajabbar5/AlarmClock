package ui;

import model.Riddle;
import model.Alarmclock;
import model.Alarm;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

//  A riddle application that allows user to answer the riddle and stop the alarm
public class RiddlesReviewer {
    private Alarm currentalarm;
    private Alarmclock alarmClock;
    private List<Riddle> riddles;
    private Scanner scanner;
    private boolean isAlarmRunning;

    // EFFECTS: creates an instance of the RiddlesReviewer console ui application   // used lab 3 as a reference
    public RiddlesReviewer() {
        this.currentalarm = null;   
        this.alarmClock = new Alarmclock();
        this.scanner = new Scanner(System.in);
        this.isAlarmRunning = true;
        this.riddles = new ArrayList<>();
        readyRiddles();

        printSnoozes();
        System.out.println("Welcome to the AlarmClock app - the perfect way to say goodbye to your sleep!!");

        while (this.isAlarmRunning) {
            handleMenu();
            checkAndHandleAlarm();
        }
    }

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
        System.out.println("2) Exit the application");
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
        {
            System.out.println("Enter the time (hour) for your alarm, please choose between (0-23)");
            int hour = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the time (minutes) for your alarm, please choose between (0-59)");
            int min = Integer.parseInt(scanner.nextLine());

            currentalarm = new Alarm(hour, min);
            alarmClock.setCurrentAlarm(currentalarm);
            alarmClock.addAlarm(currentalarm);
            System.out.println("Confirming that Alarm is set for " + hour + " : " + min);
        }
    }

    // EFFECTS: checks if the alarm set is the same as the local time, 
    //          if it is the same then play the alarm, and display a riddle for the user to solve.
    //          If the user solves the riddle then stop the alarm, if not then keep playing the alarm.
    private void checkAndHandleAlarm() {
        if (currentalarm != null && alarmClock.alarmIsPlaying(currentalarm)) {
            System.out.println("Alarm is ringing! Solve this riddle to stop the alarm");
            Riddle riddle = getRandomRiddle();
            boolean correctAnswer = false;

            while (!correctAnswer) {
                System.out.println(riddle.getQuestion());
                String answer = scanner.nextLine();
                if (riddle.checkRiddleAnswer(answer)) {
                    System.out.println("Correct! Alarm stopped.");
                    correctAnswer = true;
                    isAlarmRunning = false;
                } else {
                    System.out.println("Incorrect! Try again.");
                }
            }
        }
    }

    // EFFECTS: randomly get a riddle from the readyriddle list of riddles above

    private Riddle getRandomRiddle() {        // used diceGame from lecture lab as a reference
        Random random = new Random();
        int index = random.nextInt(riddles.size());
        return riddles.get(index);

    }

    // EFFECTS: prints out a line of snoozes to act as a divider
    private void printSnoozes() {
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
    }
}

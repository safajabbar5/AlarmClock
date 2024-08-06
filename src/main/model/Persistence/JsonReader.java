package model.persistence;

import model.Alarm;
import model.Alarmclock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads the alarmclock data from JSON data stored in file
// used the jsonSerializationDemo as a reference for everything in this Class
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads alarmclock from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Alarmclock read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAlarmclock(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses alarmclock from JSON object and returns it
    private Alarmclock parseAlarmclock(JSONObject jsonObject) {
        Alarmclock aclock = new Alarmclock();
        addAlarms(aclock, jsonObject);
        return aclock;
    }

     // MODIFIES: this
    // EFFECTS: parses alarms from JSON object and adds them to alarmclock
    private void addAlarms(Alarmclock alarmclock, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("alarms");
        for (Object json : jsonArray) {
            JSONObject nextAlarm = (JSONObject) json;
            addAlarm(alarmclock, nextAlarm);
        }
    }

    // MODIFIES: this
    // EFFECTS: parses alarm from JSON object and adds it to alarmclock
    private void addAlarm(Alarmclock alarmclock, JSONObject jsonObject) {
        int hours = jsonObject.getInt("hours");
        int minutes = jsonObject.getInt("minutes");
        Alarm alarm = new Alarm(hours, minutes);
        alarmclock.addAlarm(alarm);
    }
}

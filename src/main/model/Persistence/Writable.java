package model.Persistence;

import org.json.JSONObject;

// used the jsonSerializationDemo as a reference for everything in this Class
public interface Writable {
    // EFFECTS: returns this as JSON object
JSONObject toJson();
}

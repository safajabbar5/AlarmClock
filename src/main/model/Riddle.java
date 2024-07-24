package model;


    // A class representing a riddle with a question and answer 
public class Riddle {
    private String question;
    private String answer;


    // EFFECTS: constructs a riddle object, with a question and answer
    public Riddle(String question, String answer) {
        this.question = question;
        this.answer = answer;

    }

     // MODIFIES: this
    // EFFECTS: checks if the riddle answer matches the answer submitted by the user
    public boolean checkRiddleAnswer(String submittedAnswer) { // from lab 3
        if (submittedAnswer.matches(answer)) {
            return true;
        } else {
            return false;
        }

    }

    // EFFECTS: returns the riddle's question
    public String getQuestion() {
        return question;

    }

    // EFFECTS: returns the riddle's answer
    public String getAnswer() {
        return answer;

    }

}

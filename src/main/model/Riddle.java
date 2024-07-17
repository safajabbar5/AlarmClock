package model;

import java.util.List;

// A class representing a riddle with a question and answer 
public class Riddle {
 private String question;
 private String answer;
 private List<Riddle> riddles;

// EFFECTS: constructs a riddle object
 public Riddle(String question, String answer) {
    this.question = question;
    this.answer = answer;
   
 }

 riddles = new ArrayList<>();


 public void listOfRiddles() {
    riddles.add(new Riddle
    ("I speak without a mouth and hear without ears.I have no body, but I come alive with wind. What am I?" , "echo"));
    riddles.add(new Riddle
    ("You measure my life in hours and I serve you by expiring. I’m quick when I’m thin and slow when I’m fat. The wind is my enemy." , "candle"));
    riddles.add(new Riddle
    ("I have keys, but no locks and space, and no rooms. You can enter, but you can’t go outside. What am I?", "keyboard"));
    riddles.add(new Riddle
    ("This belongs to you, but everyone else uses it.", "name"));
    riddles.add(new Riddle
    ("The more there is, the less you see.", "darkness"));
    riddles.add(new Riddle
    ("Nobody has ever walked this way. Which way is it?", "milkyway"));
    riddles.add(new Riddle
    ("What can go through glass without breaking it?", "light"));
}

// MODIFIES: this
// EFFECTS: checks if the riddle answer matches the answer submitted by the user
 public boolean checkRiddleAnswer(String submittedAnswer) {  // from lab 3
    if (submittedAnswer.matches(answer))  {
        return true;
    } else {
        return false;
    }
  
}
    public String getQuestion() {
       return question;
        
    }

    public String getAnswer() {
        return answer;
         
     }


}

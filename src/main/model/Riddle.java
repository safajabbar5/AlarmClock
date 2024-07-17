package model;

// import java.util.List;

// A class representing a riddle with a question and answer 
public class Riddle {
 private String question;
 private String answer;
//  private List<Riddle> riddles;

// EFFECTS: constructs a riddle object
 public Riddle(String question, String answer) {
    this.question = question;
    this.answer = answer;
   
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

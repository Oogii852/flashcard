package flashcard;

public class Card {
    private String question;
    private String answer;
    private int correctCount = 0;
    private int wrongCount = 0;
    private boolean wrongLastRound = false;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public void markCorrect() {
        correctCount++;
        wrongLastRound = false;
    }

    public void markWrong() {
        wrongCount++;
        wrongLastRound = true;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public int getCorrectCount() { return correctCount; }
    public int getWrongCount() { return wrongCount; }
    public boolean wasWrongLastRound() { return wrongLastRound; }
    public int getTotalAnswered() { return correctCount + wrongCount; }
}
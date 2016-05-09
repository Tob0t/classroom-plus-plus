package ex5.it.mcm.fhooe.classroomplusplus.model;

/**
 * Created by Tob0t on 05.05.2016.
 */
public class Survey {
    private String question;
    private String leftAnswer;
    private String rightAnswer;

    public Survey() {
    }

    public Survey(String question, String leftAnswer, String rightAnswer) {
        this.question = question;
        this.leftAnswer = leftAnswer;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getLeftAnswer() {
        return leftAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }
}

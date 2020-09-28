package engine.Model;

import java.util.ArrayList;

public class QuizCreatingRequest {

    private String title;
    private String text;
    private ArrayList<String> options;
    private Integer answer;

    public QuizCreatingRequest() {}

    public QuizCreatingRequest(String title, String text, ArrayList<String> options, Integer answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
}

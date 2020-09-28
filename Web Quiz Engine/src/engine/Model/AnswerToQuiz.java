package engine.Model;

import java.util.List;

public class AnswerToQuiz {
    private List<Integer> answer;

    public AnswerToQuiz(){}
    public AnswerToQuiz(List<Integer> answer) {
        this.answer = answer;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }
}

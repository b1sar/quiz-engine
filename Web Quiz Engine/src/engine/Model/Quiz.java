package engine.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(allowSetters = true, value = {"answer", "hibernateLazyInitializer", "handler", "owner"})
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @NotEmpty
    @Size(min = 2)//should contain at least 2 items
    private ArrayList<String> options;

    @Column
    @ElementCollection(targetClass=Integer.class)
    private List<Integer> answer;


    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner;

    public Quiz() {}

    public Quiz(String title, String text, ArrayList<String> options, List<Integer> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public Quiz(@NotEmpty String title, @NotEmpty String text,
                @NotEmpty @Size(min = 2) ArrayList<String> options, List<Integer> answer, User owner) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.owner = owner;
    }

    public Quiz(Integer id, @NotEmpty String title, @NotEmpty String text,
                @NotEmpty @Size(min = 2) ArrayList<String> options, List<Integer> answer, User owner) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.owner = owner;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getAnswer() {
        return answer== null ? new ArrayList<Integer>() : answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}

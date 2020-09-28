package engine.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class CompletedQuizRecord {
    @Id
    @GeneratedValue
    @Column
    @JsonIgnore
    private Integer quizId;

    @Column
    private Integer id;

    @JsonIgnore
    private String userMail;

    @Column
    private LocalDateTime completedAt;

    public CompletedQuizRecord(){}
    public CompletedQuizRecord(Integer id, LocalDateTime completedAt) {
        id = id;
        this.completedAt = completedAt;
    }

    public CompletedQuizRecord(Integer id, Integer quizId, LocalDateTime completedAt) {
        this.id = id;
        this.quizId = quizId;
        this.completedAt = completedAt;
    }

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}

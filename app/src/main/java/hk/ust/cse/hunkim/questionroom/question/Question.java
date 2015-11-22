package hk.ust.cse.hunkim.questionroom.question;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hunkim on 7/16/15.
 */
public class Question extends BaseQuestion {
    @JsonProperty("room") protected String room;
    @JsonProperty("answers") protected List<Answer> answers = new ArrayList<Answer>();

    // Dummy Constructor for JSONObject-ifying
    public Question() {}
    public Question(String room, String text) {
        super(text);

        this.room = room;
    }

    public String getRoom() {
        return this.room;
    }

    public List<Answer>  getAnswers() {
        return answers;
    }

    public Answer getAnswer(int index) {
        return answers.get(index);
    }

    public void addFollowup(int answerIndex, FollowUp followup) {
        this.getAnswer(answerIndex).addFollowUp(followup);
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }
}

package hk.ust.cse.hunkim.questionroom.question;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hunkim on 7/16/15.
 */
public class Question extends BaseQuestion {
    @JsonProperty("room") protected String room;
    @JsonProperty("answers") protected List<Answer> answers;

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
        if (this.answers == null) {
            this.answers = new ArrayList<Answer>();
        }

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
        if (this.answers == null) {
            this.answers = new ArrayList<Answer>();
        }

        this.answers.add(answer);
    }
}

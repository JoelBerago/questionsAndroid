package hk.ust.cse.hunkim.questionroom.question;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 29/10/2015.
 */
public abstract class BaseQuestion implements Comparable<BaseQuestion>, Serializable {
    @JsonProperty("__v") private String __v;
    @JsonProperty("_id") protected String id;
    @JsonProperty("text") protected String text;
    @JsonProperty("imageURL") protected String imageURL;
    // TODO: Fix time handling; server side's fault probably.
    @JsonProperty("time") private String time;
    protected long unixTime;
    @JsonProperty("likes") protected List<String> likes;
    protected boolean newQuestion;
    @JsonProperty("userId") protected int userId;

    // Dummy Constructor for JSONObject-ifying
    public BaseQuestion() {}

    /**
     * Set question from a String message
     * @param message string message
     */
    public BaseQuestion(String message) {
        this.text = message;
        //this.unixTime = new Date().getTime();
        this.imageURL = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void updateNewQuestion() {
        newQuestion = this.unixTime > new Date().getTime() - 180000;
    }

    public boolean isNewQuestion() {
        return newQuestion;
    }

    /**
     * New one/high echo goes bottom
     * @param other other chat
     * @return order
     */
    @Override
    public int compareTo(BaseQuestion other) {
        // Push new on top
        other.updateNewQuestion(); // update NEW button
        this.updateNewQuestion();

        if (this.newQuestion != other.newQuestion) {
            return this.newQuestion ? 1 : -1; // this is the winner
        }


//        if (this.likes == other.likes) {
//            if (other.unixTime == this.unixTime) {
//                return 0;
//            }
//            return other.unixTime > this.unixTime ? -1 : 1;
//        }

        int thisLikes = 0;
        int otherLikes = 0;

        if (this.likes != null)
            thisLikes = this.likes.size();
        if (other.likes != null)
            otherLikes = other.likes.size();

        return thisLikes - otherLikes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseQuestion)) {
            return false;
        }
        return getId() == ((BaseQuestion) o).getId();
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getLikes() {
        if (likes == null)
            likes = new ArrayList<String>();

        return likes;
    }

    public void addLikes(String user) {
        getLikes().add(user);
    }
}

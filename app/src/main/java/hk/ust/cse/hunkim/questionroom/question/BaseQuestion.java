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
public abstract class BaseQuestion implements Serializable {
    @JsonProperty("__v") private String __v;
    @JsonProperty("_id") protected String id;
    @JsonProperty("text") protected String text;
    @JsonProperty("imageURL") protected String imageURL;
    // TODO: Fix time handling; server side's fault probably.
    @JsonProperty("time") private String time;
    @JsonProperty("likes") protected List<String> likes = new ArrayList<String>();
    @JsonProperty("userId") protected int userId;
    @JsonProperty("jailed") protected boolean jailed;
    @JsonProperty("acctType") protected String acctType;
    @JsonProperty("username") protected String username;
    @JsonProperty("experience") protected int experience;

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

    public int getExperience(){
        return experience;
    }

    public void setExperience(int exp){
        this.experience=exp;
        return;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void addLikes(String user) {
        getLikes().add(user);
    }
}

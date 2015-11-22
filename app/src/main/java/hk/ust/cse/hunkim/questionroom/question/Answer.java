package hk.ust.cse.hunkim.questionroom.question;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 29/10/2015.
 */
public class Answer extends BaseQuestion {
    @JsonProperty("follow_ups") private List<FollowUp> follow_ups = new ArrayList<FollowUp>();

    public List<FollowUp> getFollow_ups() {
        return follow_ups;
    }

    // Dummy Constructor for JSONObject-ifying
    public Answer() {}

    public Answer(String text) {
        super(text);
    }

    public void addFollowUp(FollowUp followup) {
        this.follow_ups.add(followup);
    }
}

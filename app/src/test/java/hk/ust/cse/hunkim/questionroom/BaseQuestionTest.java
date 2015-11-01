package hk.ust.cse.hunkim.questionroom;

import android.content.Loader;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;


/**
 * Created by hunkim on 7/15/15.
 */

public class BaseQuestionTest extends TestCase {
    Question q;

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBaseQuestionCompareToAndEqualTo() {
        Question q1 = new Question("comp3111", "Why?");
        Question q2 = new Question("comp3111", "Why?");
        Question q3 = new Question("comp3111", "What?");
        Question q4 = new Question("comp3111H", "When?");

        String[] strs = new String[1];
        strs[0] = "asf";
        q2.setLikes(strs);

        q2.equals("asf");
        q2.equals(q1);

        assertEquals(q1.compareTo(q1), 0);
        assertEquals(q1.compareTo(q3), 0);
        assertEquals(q1.compareTo(q4), 0);
        assertEquals(q3.compareTo(q4), 0);

        q1.equals(q1);
        q1.equals(q2);
        q1.equals("test");
    }

    public void testSmallerTests() {
        Question q1 = new Question("comp3111", "Why?");

        String[] strs = new String[10];
        for (int i=0; i < 10; i++) {
            strs[i] = "" + i;
        }
        q1.setLikes(strs);
        q1.getUnixTime();

        Answer a = new Answer();
        a.getFollow_ups();
        a.addFollowUp(new FollowUp());
        a.getFollow_ups();

        q1.addAnswer(a);
        q1.addFollowup(0, new FollowUp());

        Answer b = new Answer("b");
        b.addFollowUp(new FollowUp());
    }
}

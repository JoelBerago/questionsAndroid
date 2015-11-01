package hk.ust.cse.hunkim.questionroom;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.Question;


/**
 * Created by hunkim on 7/15/15.
 */

public class QuestionTest  extends TestCase {
    Question q;

    protected void setUp() throws Exception {
        super.setUp();

        q = new Question("Dummy", "Hello? This is very nice");
    }

    @SmallTest

    public void testChatFirstString() {
        String[] strHead = {
                "Hello? This is very nice", "Hello?",
                "This is cool! Really?", "This is cool!",
                "How.about.this? Cool", "How.about.this?"
        };

        for (int i = 0; i < strHead.length; i++) {
            BaseQuestion ques = new Question("room", strHead[i]);
            assertEquals("Question.create and check string", ques.getText(), strHead[i]);
        }
    }

    @SmallTest
    public void testHead() {
        assertEquals("Head", "Hello? This is very nice", q.getText());
    }


    public void testSmallerTests() {
        Question q1 = new Question();
        q1.getAnswers();

        q1.addAnswer(new Answer());
        q1.getAnswers();
        q1.getAnswer(0);

        q1.setLikes(null);
        q1.compareTo(q1);
        assertEquals(q1.compareTo(q1), 0);
    }
}

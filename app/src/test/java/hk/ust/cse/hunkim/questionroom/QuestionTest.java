package hk.ust.cse.hunkim.questionroom;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.Question;


/**
 * Created by hunkim on 7/15/15.
 */

public class QuestionTest  extends TestCase {
    Question q;

    protected void setUp() throws Exception {
        super.setUp();

        q = new Question("", "Hello? This is very nice");
    }

    @SmallTest

    public void testChatFirstString() {
        String[] strHead = {
                "Hello? This is very nice", "Hello?",
                "This is cool! Really?", "This is cool!",
                "How.about.this? Cool", "How.about.this?",
                ""
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


    /*public void testLikes()       //not yet functional.
    {
        String test = "Here's a couple sentences. I'm testing to see an actual object gets created.";
        Question q = new Question("room", test);
        assertEquals(q.getLikes().length,0);
    }*/

    /*public void testDate()        //not yet functional.
    {
        String[] texts = {
                "Hello? This is very nice", "Hello?",
                "This is cool! Really?", "This is cool!",
                "How.about.this? Cool", "How.about.this?"
        }; //copied from example above
        Question[] q = new Question[6];
        for (int i=0;i<6;i++) {
            q[i] = new Question("room", texts[i]);
        }
        for (int i=0; i<5;i++)
        {
            assertNotSame(q[i].getUnixTime(), q[i + 1].getUnixTime());
        }
    }*/
}

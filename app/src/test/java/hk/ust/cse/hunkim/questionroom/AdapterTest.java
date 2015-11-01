package hk.ust.cse.hunkim.questionroom;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.TextView;

import junit.framework.TestCase;

import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;


/**
 * Created by hunkim on 7/15/15.
 */

public class AdapterTest extends ActivityUnitTestCase<MainActivity> {
    Question q;
    private Intent mStartIntent;

    public AdapterTest()  {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mStartIntent = new Intent(Intent.ACTION_MAIN);
        mStartIntent.putExtra(JoinActivity.ROOM_NAME, "testing");
    }

    public void testDatabaseListAdapter() {
        MainActivity ma = startActivity(mStartIntent, null, null);
        QuestionListAdapter q = new QuestionListAdapter(ma, R.layout.question);
        Question ques = new Question();
        ques.setImageURL("");
    }
}

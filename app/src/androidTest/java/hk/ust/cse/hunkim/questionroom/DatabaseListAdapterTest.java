package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.ImageButton;

import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import java.util.List;

import static java.lang.Integer.parseInt;

class DatabaseListAdapterTest2 extends DatabaseListAdapter {
        public DatabaseListAdapterTest2(Context context, int mLayoutID){
            super(context , mLayoutID);
        }

    @Override
    protected void populateView(View view, Question question) {

    }

    @Override
    protected void sortModels(List<Question> mModels) {

    }
}


/**
 * Created by Devaux on 01/11/15.
 */
public class DatabaseListAdapterTest  extends ActivityUnitTestCase<MainActivity> {
    DBUtil dbutil;


    private Intent mStartIntent;
    private ImageButton mButton;

    public DatabaseListAdapterTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        // In setUp, you can create any shared test data,
        // or set up mock components to inject
        // into your Activity. But do not call startActivity()
        // until the actual test methods.
        // into your Activity. But do not call startActivity()
        // until the actual test methods.
        mStartIntent = new Intent(Intent.ACTION_MAIN);
        mStartIntent.putExtra(JoinActivity.ROOM_NAME, "all");
    }
    public void test () {
        Context ma = startActivity(mStartIntent, null, null);
        DatabaseListAdapter test = new DatabaseListAdapterTest2(ma, 1) ;

        final Question q = new Question("test","hello");
        q.setRoom("all");
        test.push(q);

        Answer a = new Answer("hello too");
//        test.push(0,a);
       // test.push(0, 0, new FollowUp("bye"));
    }
}

package hk.ust.cse.hunkim.questionroom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;

/**
 * Created by hunkim on 7/20/15.
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private Intent mStartIntent;
    private ImageButton mButton;

    public MainActivityTest() {
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
        mStartIntent.putExtra(JoinActivity.ROOM_NAME, "");
    }

    @MediumTest
    public void testPreconditions() {
        startActivity(mStartIntent, null, null);
        mButton = (ImageButton) getActivity().findViewById(R.id.sendButton);
        assertNotNull(getActivity());
        assertNotNull(mButton);

        assertEquals("This is set correctly", "Room name: all", getActivity().getTitle());
    }

    @MediumTest
    public void testMChat() {
        startActivity(mStartIntent, null, null);
        mButton = (ImageButton) getActivity().findViewById(R.id.sendButton);
        assertNotNull(getActivity());
        assertNotNull(mButton);
    }


    @MediumTest
    public void testPostingMessage() {
        MainActivity activity = startActivity(mStartIntent, null, null);
        mButton = (ImageButton) activity.findViewById(R.id.sendButton);
        final TextView text = (TextView) activity.findViewById(R.id.messageInput);
        final ListView lView = getActivity().getListView();

        //assertTrue( activity.hasAdapter());
        assertNotNull(mButton);
        assertNotNull(text);
        assertNotNull(lView);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                lView.performItemClick(lView, 0, lView.getItemIdAtPosition(0));
            }
        });
        getInstrumentation().waitForIdleSync();

        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                text.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();

        text.setText("This is a test!");

        mButton.performClick();

        text.setText(""); //handle null output without error

        mButton.performClick();



        // LH start
        EditText inputText = (EditText) activity.findViewById(R.id.messageInput);
        inputText.beginBatchEdit();
        inputText.setText("Hi there");
        inputText.endBatchEdit();



        activity.findViewById(R.id.uploadImage).performClick();



        // ImageHelper.picturePath="/data/local/1.png";
        //activity.findViewById(R.id.sendButton).performClick();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.onCreate(null, null);
        }
        activity.onStop();
        activity.onStart();
        activity.Close(mButton);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void MatthieuTest() {
        MainActivity act = startActivity(null, null, null);
        act.onCreate(null, null);

        Intent intent = new Intent(Intent.ACTION_MAIN);
      //  intent.int(mStartIntent);
        intent.putExtra(JoinActivity.ROOM_NAME, "all");
        MainActivity activity = startActivity(intent, null, null);
        activity.onCreate(null,null);

    }
}

package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.Question;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Joel on 22/11/2015.
 */
@RunWith(AndroidJUnit4.class)
public class FollowupsActivityUITest extends ActivityInstrumentationTestCase2<FollowupActivity> {
    private FollowupActivity activity;
    private static boolean setUpIsDone = false;

    public FollowupsActivityUITest() {
        super(FollowupActivity.class);
    }

    @Rule
    public ActivityTestRule<FollowupActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(FollowupActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        super.setUp();

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.commit();

        Answer question = new Answer("asdf");
        question.setId("asdf");

        Intent intent = new Intent(context, JoinActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(FollowupActivity.ANSWER, question);
        intent.putExtras(b);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }


    @Test
    public void testUserIdNegativeOne() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", -1);
        editor.commit();
    }

    @Test
    public void testUserJailed() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.putBoolean("jailed", true);
        editor.commit();
    }

    @Test
    public void testSendQuestionTotallyEmpty() throws Exception {
        onView(withId(R.id.sendButton)).perform(click());
    }

    @Test
    public void testSendQuestion() throws Exception {
        onView(withId(R.id.messageInput)).perform(click());
        onView(withId(R.id.messageInput)).perform(typeText("TEST123"),
                closeSoftKeyboard());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.sendButton)).perform(click());
    }

    @Test
    public void testSendQuestionWithPicture() throws Exception {
        ImageHelper.picturePath = "/data/local/tmp/1.png";
        onView(withId(R.id.messageInput)).perform(click());
        onView(withId(R.id.messageInput)).perform(typeText("TEST123"),
                closeSoftKeyboard());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.sendButton)).perform(click());
    }
}

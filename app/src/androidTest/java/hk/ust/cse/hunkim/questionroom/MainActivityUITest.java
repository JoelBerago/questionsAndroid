package hk.ust.cse.hunkim.questionroom;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Joel on 22/11/2015.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityUITest extends ActivityInstrumentationTestCase2<LogIn> {
    private LogIn activity;
    private static boolean setUpIsDone = false;

    public MainActivityUITest() {
        super(LogIn.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        activity = getActivity();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().clear().commit();

        if (!setUpIsDone) {
            /////
            // Login
            onView(withId(R.id.email)).perform(click());
            onView(withId(R.id.email)).perform(typeText("asdf@connect.ust.hk"),
                    closeSoftKeyboard());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.password)).perform(click());
            onView(withId(R.id.password)).perform(typeText("asdfasdf"),
                    closeSoftKeyboard());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.btn_signIn)).perform(click());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setUpIsDone = true;

        /////
        // Join Room
        onView(withId(R.id.room_name)).perform(click());
        onView(withId(R.id.room_name)).perform(typeText("testroom"),
                closeSoftKeyboard());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.join_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Joel on 22/11/2015.
 */
@RunWith(AndroidJUnit4.class)
public class JailedUITest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;
    private static boolean setUpIsDone = false;

    public JailedUITest() {
        super(MainActivity.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void testJailed() {
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


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLikeClickable() {
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


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "asdf");
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLikeClickableJailed() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", -1);
        editor.putBoolean("jailed", true);
        editor.commit();


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }
}

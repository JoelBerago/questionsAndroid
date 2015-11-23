package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Joel on 22/11/2015.
 */
@RunWith(AndroidJUnit4.class)
public class LevelUITest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity activity;
    private static boolean setUpIsDone = false;

    public LevelUITest() {
        super(MainActivity.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void testLevel4() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.putInt("experience", 100);
        editor.commit();


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLevel9() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.putInt("experience", 500);
        editor.commit();


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLevel14() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.putInt("experience", 2000);
        editor.commit();


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLevel19() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.putInt("experience", 3000);
        editor.commit();


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLeve24() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(JoinActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.putInt("experience", 5000);
        editor.commit();


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ROOM_NAME, "testroom");
        mainActivityActivityTestRule.launchActivity(intent);
    }
}

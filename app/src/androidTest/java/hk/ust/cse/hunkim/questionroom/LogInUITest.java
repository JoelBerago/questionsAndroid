package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

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
public class LogInUITest extends ActivityInstrumentationTestCase2<LogIn> {
    private LogIn activity;

    public LogInUITest() {
        super(LogIn.class);
    }

    @Test
    public void testOnCreate() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().getTargetContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("logged", "logged");
        editor.putInt("userId", 1);
        editor.commit();

        activity = getActivity();
    }

    @Test
    public void testOnCreateEmptyUserId() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().getTargetContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("logged", "");
        editor.putInt("userId", 1);
        editor.commit();

        activity = getActivity();
    }

    @Test
    public void testOnCreateNull() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().getTargetContext());
        preferences.edit().clear().commit();
        activity = getActivity();
    }

    @Test
    public void testTestRegisterField() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(LogIn.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();

        activity = getActivity();

        onView(withId(R.id.btn_signUp)).perform(click());
        onView(withId(R.id.btn_signUp)).perform(click());
    }
}

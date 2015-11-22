package hk.ust.cse.hunkim.questionroom;

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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Joel on 22/11/2015.
 */
@RunWith(AndroidJUnit4.class)
public class JoinActivityUITest extends ActivityInstrumentationTestCase2<JoinActivity> {
    private JoinActivity activity;

    public JoinActivityUITest() {
        super(JoinActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    @Test
    public void testJoinUsingButton() throws Exception {
        onView(withId(R.id.room_name)).perform(click());
        onView(withId(R.id.room_name)).perform(typeText("comp3111"),
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
        onView(withId(R.id.close)).check(matches(isDisplayed()));
    }

    @Test
    public void testJoinEmpty() throws Exception {
        onView(withId(R.id.join_button)).perform(click());
        onView(withId(R.id.room_name)).check(matches(isDisplayed()));
    }


    /**
     * call here your local tests
     *
     * @throws Exception
     */
    @Test
    public void testLocal()throws Exception {
        Class<?> test = Class.forName("hk.ust.cse.hunkim.questionroom.ImageHelperTest");
        JUnitCore junit = new JUnitCore();
        junit.run(test);
    }
}

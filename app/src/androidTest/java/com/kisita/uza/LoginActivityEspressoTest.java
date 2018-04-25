package com.kisita.uza;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kisita.uza.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;





/*
 * Created by HuguesKi on 23-04-18.
 */

@RunWith(AndroidJUnit4.class)
        public class LoginActivityEspressoTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
    new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void ensureTextChangesWork() {
        // Type text and then press the button.
        onView(withId(R.id.field_email))
                .perform(typeText("test@ahoo.fr"), closeSoftKeyboard());
        onView(withId(R.id.field_password))
                .perform(typeText("testtest"), closeSoftKeyboard());
        onView(withId(R.id.button_sign_in)).perform(click());
    }
}

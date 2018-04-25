package com.kisita.uza;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.kisita.uza.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.kisita.uza.DrawingEspressoTest.goToFragment;
import static com.kisita.uza.TestUtils.withRecyclerView;





/*
 * Created by HuguesKi on 23-04-18.
 */

@RunWith(AndroidJUnit4.class)
public class OnSaleEspressoTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void favouriteButtonDisplayed() {
        // Type text and then press the button.
        goToFragment(R.id.nav_artworks);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withRecyclerView(R.id.cardList)
                .atPositionOnView(1, R.id.favourite))
                .check(matches(isDisplayingAtLeast(5)));
    }

    @Test
    public void clickFavouriteButton() {
        // Type text and then press the button.
        goToFragment(R.id.nav_artworks);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.cardList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ItemViewAction.clickChildViewWithId(R.id.favourite)));
    }
}


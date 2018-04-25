package com.kisita.uza;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kisita.uza.activities.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;





/*
 * Created by HuguesKi on 23-04-18.
 */

@RunWith(AndroidJUnit4.class)
        public class DrawingEspressoTest {

    public static final int [] items = {
            R.id.nav_home,
            R.id.nav_artworks,
            R.id.nav_checkout,
            R.id.nav_favourites,
            R.id.nav_commands,
            R.id.nav_manage,
            R.id.nav_billing_info};

    public static final int [] titles = {
            R.string.app_name,
            R.string.artworks,
            R.string.cart,
            R.string.favourites,
            R.string.commands,
            R.string.title_filters,
            R.string.billing_information};

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
    new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testToolbarTitle() {
        // Type text and then press the button.
        for(int i = 0 ; i < items.length ; i++)
            testDrawerToolbarTitle(items[i],titles[i]);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

    private void testDrawerToolbarTitle(int item, int title){
        goToFragment(item);
        onView(withId(R.id.toolbar_title)).check(matches(withText(getResourceString(title))));
    }

    public static void goToFragment(int item) {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen()));
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(item));
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());
    }
}

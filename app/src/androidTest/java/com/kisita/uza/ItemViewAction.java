package com.kisita.uza;

/*
 * Created by HuguesKi on 23-04-18.
 */

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

public class ItemViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }

                public boolean isVisible(UiController uiController, View view){
                    View  v = view.findViewById(id);
                    if(v.getVisibility() == 0){
                        return true;
                    }
                    return false;
                }
            };
        }

    }

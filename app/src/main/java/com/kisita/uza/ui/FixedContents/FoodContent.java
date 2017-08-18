package com.kisita.uza.ui.FixedContents;

import com.kisita.uza.R;
import com.kisita.uza.model.UzaListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FoodContent {

    public static final String [] categories = {"Meat, Fish & Eggs","Dairy products","Fat","Vegetables and fruits","Cereals & Derivatives","Drinks","Sweet products"};

    public static final int [] icons = {R.drawable.meat,R.drawable.dairy ,R.drawable.mobile_money, R.drawable.fruits,R.drawable.cereals,R.drawable.drinks,R.drawable.sweet};
    /**
     * An array of sample (dummy) items.
     */
    public static final List<UzaListItem> ITEMS = new ArrayList<UzaListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, UzaListItem> ITEM_MAP = new HashMap<String, UzaListItem>();

    private static final int COUNT = categories.length;

    public FoodContent() {

    }

    static {
        for(int i = 0 ; i < COUNT ; i ++ ){
           // addItem(new UzaListItem(categories[i],icons[i]));
        }
    }

    private static void addItem(UzaListItem item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.name, item);
    }
}

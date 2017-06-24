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
public class MenContent {

    public static final String [] categories = {"Clothing","Shoes & Bags","Watches & Accessories","Perfumes & Beauty"};

    private static final int [] icons = {R.drawable.clothing,R.drawable.shoes,R.drawable.watch,R.drawable.perfumes};
    /**
     * An array of sample (dummy) items.
     */
    private static final List<UzaListItem> ITEMS = new ArrayList<UzaListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private static final Map<String, UzaListItem> ITEM_MAP = new HashMap<String, UzaListItem>();

    private static final int COUNT = categories.length;

    public MenContent() {

    }

    static {
        for(int i = 0 ; i < COUNT ; i ++ ){
            addItem(new UzaListItem(categories[i],icons[i]));
        }
    }

    private static void addItem(UzaListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }
}

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

    public static final int [] categories = {R.string.all,R.string.clothing,R.string.shoes_bags,R.string.watches_accessories,R.string.perfumes_beauty};

    private static final int [] icons = {R.drawable.ic_apps_black_24dp,R.drawable.clothing,R.drawable.shoes,R.drawable.watch,R.drawable.perfumes};
    /**
     * An array of sample (dummy) items.
     */
    public static final List<UzaListItem> ITEMS = new ArrayList<UzaListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private static final Map<Integer, UzaListItem> ITEM_MAP = new HashMap<Integer, UzaListItem>();

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

package com.kisita.uza.ui.FixedContents;

import com.kisita.uza.R;
import com.kisita.uza.model.UzaListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HuguesKi on 18-08-17.
 */

public class BooksContent {

    public static final int [] categories = {R.string.all,R.string.sciences,R.string.history,R.string.geography,R.string.languages,R.string.fiction,R.string.comic_strip,R.string.kids};

    private static final int icons = R.drawable.ic_book_black_24dp;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<UzaListItem> ITEMS = new ArrayList<UzaListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private static final Map<Integer, UzaListItem> ITEM_MAP = new HashMap<Integer, UzaListItem>();

    private static final int COUNT = categories.length;

    public BooksContent() {
    }

    static {
        for(int i = 0 ; i < COUNT ; i ++ ){
            addItem(new UzaListItem(categories[i],icons));
        }
    }

    private static void addItem(UzaListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }
}

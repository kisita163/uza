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
public class PaymentContent {

    private static final int [] methods = {R.string.credit_card,R.string.cash,R.string.mobile_money};

    private static final int [] icons = {R.drawable.ic_credit_card_black_24dp,R.drawable.cash,R.drawable.mobile_money};
    /**
     * An array of sample (dummy) items.
     */
    public static final List<UzaListItem> ITEMS = new ArrayList<UzaListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, UzaListItem> ITEM_MAP = new HashMap<Integer, UzaListItem>();

    private static final int COUNT = methods.length;

    public PaymentContent() {

    }

    static {
        for(int i = 0 ; i < COUNT ; i ++ ){
            addItem(new UzaListItem(methods[i],icons[i]));
        }
    }

    private static void addItem(UzaListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }
}

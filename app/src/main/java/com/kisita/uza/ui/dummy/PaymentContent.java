package com.kisita.uza.ui.dummy;

import com.kisita.uza.R;

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

    private static final String [] methods = {"Credit card","Cash","Mobile money"};

    private static final int [] icons = {R.drawable.ic_credit_card_black_24dp,R.drawable.cash,R.drawable.mobile_money};
    /**
     * An array of sample (dummy) items.
     */
    public static final List<PaymentItem> ITEMS = new ArrayList<PaymentItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PaymentItem> ITEM_MAP = new HashMap<String, PaymentItem>();

    private static final int COUNT = methods.length;

    public PaymentContent() {

    }

    static {
        for(int i = 0 ; i < COUNT ; i ++ ){
            addItem(new PaymentItem(methods[i],icons[i]));
        }
    }

    private static void addItem(PaymentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class PaymentItem {
        public final String name;
        public final int icon;

        public PaymentItem(String id, int icon) {
            this.name = id;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

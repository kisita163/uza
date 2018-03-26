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
public class SettingsContent {

    public static final int [] categories = {R.string.title_notifications,
            R.string.currency,
            R.string.payment_mthod,
            R.string.billing_information,
            R.string.about_us,
            R.string.action_logout};

    private static final int [] icons = {R.drawable.ic_action_notification,
            R.drawable.ic_action_currency,
            R.drawable.ic_action_payement,
            R.drawable.ic_action_address,
            R.drawable.ic_action_info,
            R.drawable.ic_action_logout};
    /**
     * An array of sample (dummy) items.
     */
    public static final List<UzaListItem> ITEMS = new ArrayList<UzaListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private static final Map<Integer, UzaListItem> ITEM_MAP = new HashMap<Integer, UzaListItem>();

    private static final int COUNT = categories.length;

    public SettingsContent() {

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
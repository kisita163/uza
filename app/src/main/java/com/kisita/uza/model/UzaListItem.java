package com.kisita.uza.model;

/**
 * Created by HuguesKi on 14/06/2017.
 */

public class UzaListItem {
    public final String name;
    public final int icon;
    public String selectedFragments;

    public UzaListItem(String id, int icon) {
        this.name = id;
        this.icon = icon;
    }

    public UzaListItem(String id, int icon,String selectedFragments) {
        this.name = id;
        this.icon = icon;
        this.selectedFragments = selectedFragments;
    }
    @Override
    public String toString() {
        return name;
    }
}
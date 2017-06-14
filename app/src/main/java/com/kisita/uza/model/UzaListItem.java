package com.kisita.uza.model;

/**
 * Created by HuguesKi on 14/06/2017.
 */

public class UzaListItem {
    public final String name;
    public final int icon;

    public UzaListItem(String id, int icon) {
        this.name = id;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
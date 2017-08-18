package com.kisita.uza.model;

/**
 * Created by HuguesKi on 14/06/2017.
 */

public class UzaListItem {
    public final int name;
    public final int icon;
    public String selectedFragments;

    public UzaListItem(int id, int icon) {
        this.name = id;
        this.icon = icon;
    }

    public UzaListItem(int id, int icon,String selectedFragments) {
        this.name = id;
        this.icon = icon;
        this.selectedFragments = selectedFragments;
    }
}
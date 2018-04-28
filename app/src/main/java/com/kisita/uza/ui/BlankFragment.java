package com.kisita.uza.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisita.uza.R;
import com.kisita.uza.activities.DrawerActivity;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaBannerAdapter;

import java.util.ArrayList;

import static com.kisita.uza.custom.CustomActivity.BikekoMenu.ARTWORKS;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.CART;
import static com.kisita.uza.custom.CustomActivity.BikekoMenu.FAVOURITES;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MENU_ITEM = "menu_item";

    private int mItemNumber;
    private RecyclerView mBannerRecList;
    private ArrayList<Data> mBannerItemsList;
    private LinearLayout sameArtistCont;


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemNumber Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */

    public static BlankFragment newInstance(int itemNumber) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putInt(MENU_ITEM, itemNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemNumber = getArguments().getInt(MENU_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_blank, container, false);
        sameArtistCont   = v.findViewById(R.id.banner_container);
        mBannerRecList   = v.findViewById(R.id.cardList);
        mBannerItemsList = new ArrayList<>();
        mBannerItemsList = (((DrawerActivity)getActivity()).getItemsList());
        TextView bannerTitle = v.findViewById(R.id.banner_title);
        setBanner();
        TextView blankText   = v.findViewById(R.id.blank_text);
        setBlankText(blankText);
        bannerTitle.setText(getString(R.string.artworks));

       return v;
    }

    private void setBanner() {
        if(mBannerItemsList.size() > 0) {
            sameArtistCont.setVisibility(View.VISIBLE);
            //
            UzaBannerAdapter mCardAdapter = new UzaBannerAdapter(this.getContext(), mBannerItemsList);

            mBannerRecList.setHasFixedSize(true);


            StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
                    StaggeredGridLayoutManager.HORIZONTAL);

            mBannerRecList.setLayoutManager(llm);
            mBannerRecList.setAdapter(mCardAdapter);
        }
    }

    public void setBlankText(TextView blankText) {

        if(mItemNumber == CART.ordinal()){
            blankText.setText(getString(R.string.nothing_in_the_cart));
        }

        if(mItemNumber == FAVOURITES.ordinal()){
            blankText.setText(getString(R.string.nothing_in_favourites));
        }

        if(mItemNumber == ARTWORKS.ordinal()){
            blankText.setText(getString(R.string.nothing_to_show));
        }
    }
}

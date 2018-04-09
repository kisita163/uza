package com.kisita.uza.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DatabaseReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.UzaPageAdapter;

import java.util.HashMap;
import java.util.Map;

import static com.kisita.uza.model.Data.FAVOURITES_COLUMNS;
import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.setFormat;
import static com.kisita.uza.utils.UzaFunctions.setPrice;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends CustomFragment{
    // the fragment initialization parameters
    private static final String TAG = "### DetailFragment";

    private static final String ITEM_DATA = "ITEM_DATA";

    private String key;

    private ImageView mlike;

    protected OnFragmentInteractionListener mListener;

    private Boolean mLiked = false;

    private String mCurrency;


    private boolean mCommand = false;


    private Data itemData;

    private ShareDialog shareDialog;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data Item's data.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(Data data) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEM_DATA,data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemData     = (Data)getArguments().getSerializable(ITEM_DATA);
            mCurrency = getCurrency(getContext());
        }
        shareDialog = new ShareDialog(this);
        //printKeyHash();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_detail, null);
        setHasOptionsMenu(true);
        return setupView(v);
    }

    private void onButtonPressed(String[] details, boolean update) {
        if (mListener != null) {
            mListener.onItemAddedInCart(details,update);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnNewArticleInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Setup the click & other events listeners for the view components of this
     * screen. You can add your logic for Binding the data to TextViews and
     * other views as per your need.
     */
    private View setupView(View v)
    {

        TextView  item_name         = v.findViewById(R.id.item_name);
        TextView  item_author       = v.findViewById(R.id.item_author);
        TextView  item_size         = v.findViewById(R.id.item_size);
        TextView  item_type         = v.findViewById(R.id.item_type);
        TextView  item_price        = v.findViewById(R.id.item_price);
        TextView  item_description  = v.findViewById(R.id.item_description);

        Button add = v.findViewById(R.id.addToCart);
        add.setOnClickListener(this);
        mlike =  v.findViewById(R.id.favourite);
        mlike.setOnClickListener(this);

        ImageView shareFacebook = (ImageView) v.findViewById(R.id.shareFacebook);
        shareFacebook.setOnClickListener(this);

        ImageView shareWhatsapp = (ImageView) v.findViewById(R.id.share_whatsapp);
        shareWhatsapp.setOnClickListener(this);

        if(itemData != null) {
            String price;
            price = setPrice(itemData.getCurrency(),itemData.getPrice(),getContext());

            String s = setFormat(price) + " "+mCurrency;

            item_price.setText(s);
            item_author.setText(itemData.getAuthor());
            item_type.setText(itemData.getType());
            item_name.setText(itemData.getName());
            item_size.setText(itemData.getSize());
            item_description.setText(itemData.getDescription(

            ));
        }
        initPager(v);
        return v;
    }
    /**
     * Inits the pager view.
     */
    private void initPager(View v)
    {
        /* The pager. */
        ViewPager pager = v.findViewById(R.id.pager);
        pager.setPageMargin(10);
        /* The view that hold dots. */
        LinearLayout vDots = v.findViewById(R.id.vDots);

        UzaPageAdapter adapter = new UzaPageAdapter(getContext(), itemData, vDots, null);

        pager.setOnPageChangeListener(adapter);

        pager.setAdapter(adapter);
    }


    private void setAddButton() {
        //Log.i(TAG, "Setting add button");
        //add.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), R.string.item_in_the_cart, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addToCart:
                String[] details;

                if(!mCommand){
                    itemData.setQuantity("1");
                    String commandKey = getDb().child("users").push().getKey();
                    details = new String[]{itemData.getItemId(),
                                           itemData.getSize(), ""/*selected color TODO*/,itemData.getQuantity(),"", commandKey, getUid(), itemData.getSeller()};
                    onButtonPressed(details,mCommand);
                    mCommand = true; //update case + update the quantity here for example
                }
                //Log.i(TAG,selectedColor + " " +  selectedSize + " " +  selectedQty);
                Toast.makeText(getContext(), R.string.item_in_the_cart, Toast.LENGTH_LONG).show();
                break;
            case R.id.favourite:
                likePressed();
                break;
            case R.id.shareFacebook:
                ((UzaActivity)getActivity()).showProgressDialog("Please wait");
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(getString(R.string.uza_store_link)))
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#africart")
                                .build())
                        .build();

                if(!ShareDialog.canShow(ShareLinkContent.class)){
                    Log.i(TAG,"Can't show share dialog");
                    return;
                }
                shareDialog.show(content);
                break;
            case R.id.share_whatsapp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.uza_store_link));
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                break;

            default:
                Toast.makeText(this.getContext(), "Unknown error occured", Toast.LENGTH_LONG).show();
        }
    }

    private void likePressed() {
        //Log.i(TAG, "button like pressed (case)");
        DatabaseReference likes = getDb().child("users-data").child(getUid()).child("likes");
        Map<String, Object> childUpdates = new HashMap<>();
        if (!mLiked) {
            //Log.i(TAG, "mlike is false");
            String like = getDb().child("users").push().getKey();
            key = like;
            childUpdates.put("/users-data/" + getUid() + "/likes/" + like,itemData.getItemId());
            getDb().updateChildren(childUpdates);
            mlike.setImageResource(R.drawable.ic_action_favorite_black);
            mLiked = true;
        } else {
            //Log.i(TAG, "mlike is true - key = "+key);
            likes.child(key).removeValue();
            mLiked = false;
            mlike.setImageResource(R.drawable.ic_action_favorite);
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri PlacesUri = UzaContract.LikesEntry.CONTENT_URI;
        //Log.i(TAG,itemData.getData()[UID]);
        return new CursorLoader(getContext(),
                PlacesUri,
                FAVOURITES_COLUMNS,
                itemData.getItemId(),
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
            //Log.i(TAG,data.getString(0) + "  " + itemData.getData()[0]);
            mlike.setImageResource(R.drawable.ic_action_favorite_black);
            key = data.getString(0);
            mLiked = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
         void onItemAddedInCart(String[] details, boolean update);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.i(TAG,"****************On activity created");
        if(mLiked){
            mlike.setImageResource(R.drawable.ic_action_favorite_black);
        }
        if(mCommand){
            setAddButton();
        }
    }
}

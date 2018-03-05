package com.kisita.uza.ui;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.utils.ColorSizeAdapter;
import com.kisita.uza.utils.UzaPageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kisita.uza.model.Data.FAVOURITES_COLUMNS;
import static com.kisita.uza.model.Data.UZA.*;
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
public class DetailFragment extends CustomFragment implements ColorSizeAdapter.OnFieldChangedListener{
    // the fragment initialization parameters
    private static final String TAG = "### DetailFragment";

    private static final String DESCR = "description";
    private static final String ITEM_DATA = "ITEM_DATA";

    private String [] mDescription;

    private String key;

    /** The pager. */
    private ViewPager pager;

    private FloatingActionButton add;

    private ImageView mlike;

    private ImageView mComment;

    /** The view that hold dots. */
    private LinearLayout vDots;

    private OnFragmentInteractionListener mListener;

    private Boolean mLiked = false;

    private FirebaseStorage mStorage;

    private String mCurrency;


    private boolean mCommand = false;


    /*Number of available pictures*/
    private int mPictures;

    private LinearLayout mColorsContainer;

    private LinearLayout mSizesContainer;

    private ColorSizeAdapter colorAdapter;

    private ColorSizeAdapter sizeAdapter;

    private TextView quantity;

    private EditText  commandComment;

    private String    sizeResource = "";

    private String    colorResource = "";

    private String commandKey = "";

    private Data itemData;

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
        args.putStringArray(DESCR, data.getData());
        args.putSerializable(ITEM_DATA,data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDescription = getArguments().getStringArray(DESCR);
            itemData     = (Data)getArguments().getSerializable(ITEM_DATA);
            // get Firebase database  reference for pictures
            mStorage  = FirebaseStorage.getInstance();
            mPictures = itemData.getPictures().size();
            mCurrency = getCurrency(getContext());
        }
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

    public void onButtonPressed(String[] details,boolean update) {
        if (mListener != null) {
            mListener.onFragmentInteraction(details,update);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
        TextView  item_price        = v.findViewById(R.id.item_price);
        TextView  item_description  = v.findViewById(R.id.item_description);
        commandComment              = v.findViewById(R.id.command_comment);

        add =  v.findViewById(R.id.fabCart);
        add.setOnClickListener(this);
        mlike =  v.findViewById(R.id.btnLike);
        mlike.setOnClickListener(this);

        mComment =  v.findViewById(R.id.btnComment);
        mComment.setOnClickListener(this);

        setColorsContainer(v);
        setSizesContainer(v);
        setQuantityContainer(v);

        if(itemData != null) {
            String price;
            price = setPrice(itemData.getData()[CURRENCY],itemData.getData()[PRICE],getContext());
            item_name.setText(itemData.getData()[NAME] + " | " + itemData.getData()[SELLER]);
            item_price.setText(setFormat(price) + " "+mCurrency);
            item_description.setText(itemData.getData()[DESCRIPTION]);
        }
        initPager(v);
        return v;
    }

    private void setQuantityContainer(View v) {
        quantity  = v.findViewById(R.id.integer_number);

        ImageView increase = v.findViewById(R.id.increase);
        ImageView decrease = v.findViewById(R.id.decrease);

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity.getText().toString());
                quantity.setText(String.valueOf(qty + 1));
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity.getText().toString());
                if(qty > 0){
                    quantity.setText(String.valueOf(qty - 1));
                }
            }
        });
    }

    private void setSizesContainer(View v) {

        mSizesContainer =  v.findViewById(R.id.sizes_container);
        mSizesContainer.setVisibility(View.GONE);

        if(!itemData.getData()[SIZE].equalsIgnoreCase("")){
            List<String> sizeList = new ArrayList<>(Arrays.asList(itemData.getData()[SIZE].split(",")));
            //Log.i(TAG,mDescription[SIZE] + "  " + sizeList.size());
            if(sizeList.size() == 1 && sizeList.get(0).equalsIgnoreCase("Size")){
                return;
            }
            mSizesContainer.setVisibility(View.VISIBLE);
            //Then get the ListView
            RecyclerView listView =  v.findViewById(R.id.sizesList);
            listView.setHasFixedSize(true);
            sizeAdapter = new ColorSizeAdapter(this,getContext(),sizeList,ColorSizeAdapter.SIZE ,sizeResource);

            listView.setAdapter(sizeAdapter);

            GridLayoutManager llm = new GridLayoutManager(getContext(),getDivider(getScreenWidth(getContext())),LinearLayoutManager.VERTICAL,false);
            listView.setLayoutManager(llm);
        }
    }

    private void setColorsContainer(View v) {
        mColorsContainer =  v.findViewById(R.id.colors_container);
        mColorsContainer.setVisibility(View.GONE);

        if(!itemData.getData()[COLOR].equalsIgnoreCase("")){
            mColorsContainer.setVisibility(View.VISIBLE);
            List<String> colorList = new ArrayList<>(Arrays.asList(itemData.getData()[COLOR].split(",")));
            //Log.i(TAG,mDescription[COLOR] + "  " + colorList.size());
            //Then get the ListView
            RecyclerView listView =  v.findViewById(R.id.colorsList);
            listView.setHasFixedSize(true);
            colorAdapter = new ColorSizeAdapter(this,getContext(),colorList,ColorSizeAdapter.COLOR,colorResource);

            listView.setAdapter(colorAdapter);
            GridLayoutManager llm = new GridLayoutManager(getContext(),getDivider(getScreenWidth(getContext())),LinearLayoutManager.VERTICAL,false);
            listView.setLayoutManager(llm);
        }
    }

    /**
     * Inits the pager view.
     */
    private void initPager(View v)
    {
        pager =  v.findViewById(R.id.pager);
        pager.setPageMargin(10);
        vDots = v.findViewById(R.id.vDots);

        UzaPageAdapter adapter = new UzaPageAdapter(getContext(), itemData, vDots, null);

        pager.setOnPageChangeListener(adapter);

        pager.setAdapter(adapter);
    }


    private void setAddButton() {
        //Log.i(TAG, "Setting add button");
        add.setVisibility(View.INVISIBLE);
        add.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        Toast.makeText(getContext(), R.string.item_in_the_cart, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCart:
                String[] details;
                String selectedColor = "";
                String selectedSize = "";
                String selectedQty  = quantity.getText().toString();
                //check the selected color
                if(colorAdapter != null){
                    selectedColor = colorAdapter.getResource();
                    if(selectedColor.equalsIgnoreCase("")){
                        Toast.makeText(getContext(),"You need to select a color",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                //check the selected size
                if(sizeAdapter != null){
                    selectedSize = sizeAdapter.getResource();
                    if(selectedSize.equalsIgnoreCase("")){
                        Toast.makeText(getContext(),"You need to select a size",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                // check the quantity
                if(selectedQty.equalsIgnoreCase("0")){
                    selectedQty = "1";
                }
                if(!mCommand){
                    commandKey = getDb().child("users").push().getKey(); // New command id
                    details = new String[]{mDescription[UID],
                                                selectedSize,
                                               selectedColor,
                                                 selectedQty,
                            commandComment.getText().toString(),
                                                     commandKey,
                                                       getUid(),
                                                      mDescription[SELLER]};
                    onButtonPressed(details,mCommand);
                    mCommand = true; //update case
                }else{
                    details = new String[]{mDescription[UID],
                                                selectedSize,
                                               selectedColor,
                                                 selectedQty,
                            commandComment.getText().toString(),
                                                     commandKey,
                                                        getUid(),
                                                        mDescription[SELLER]};
                    onButtonPressed(details,mCommand);
                }
                //Log.i(TAG,selectedColor + " " +  selectedSize + " " +  selectedQty);
                setAddButton();
                break;
            case R.id.btnLike:
                likePressed();
                break;
            case R.id.btnComment:
                //Log.i(TAG,"Start comment fragment");
                CommentFragment f = CommentFragment.newInstance(1,mDescription[UID]);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                        .addToBackStack(null)
                        .replace(R.id.content_frame, f)
                        .commit();
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
            childUpdates.put("/users-data/" + getUid() + "/likes/" + like, mDescription[UID]);
            getDb().updateChildren(childUpdates);
            mlike.setImageResource(R.drawable.button_liked);
            mLiked = true;
        } else {
            //Log.i(TAG, "mlike is true - key = "+key);
            likes.child(key).removeValue();
            mLiked = false;
            mlike.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    public void onFieldChangedListener() {
        if(mCommand) {
            add.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            add.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri PlacesUri = UzaContract.LikesEntry.CONTENT_URI;
        //Log.i(TAG,itemData.getData()[UID]);
        return new CursorLoader(getContext(),
                PlacesUri,
                FAVOURITES_COLUMNS,
                itemData.getData()[UID],
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
            //Log.i(TAG,data.getString(0) + "  " + itemData.getData()[0]);
            mlike.setImageResource(R.drawable.button_liked);
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
         void onFragmentInteraction(String[] details,boolean update);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.i(TAG,"****************On activity created");
        if(mLiked){
            mlike.setImageResource(R.drawable.button_liked);
        }
        if(mCommand){
            setAddButton();
        }
    }

    public static class ColorSize{
        private String name;
        private boolean selected = false;

        public ColorSize(String name, boolean selected){
            this.name = name;
            this.selected = selected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}

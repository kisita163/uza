package com.kisita.uza.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.utils.ColorSizeAdapter;
import com.kisita.uza.utils.PageAdapter;
import com.kisita.uza.utils.SpinnerColorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.kisita.uza.model.Data.UZA.*;
import static com.kisita.uza.utils.UzaCardAdapter.setPrice;

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

    private static final String DESCR = "description";

    private String [] mDescription;

    private String key;

    /** The pager. */
    private ViewPager pager;

    private FloatingActionButton add;

    private ImageView mlike;

    private ImageView mComment;

    /** The view that hold dots. */
    private LinearLayout vDots;

    private String mColor = new String("");

    private String mSize = new String("");

    private OnFragmentInteractionListener mListener;

    private Boolean mLiked = false;

    private DatabaseReference commands;

    private DatabaseReference likes;

    private FirebaseStorage mStorage;

    private StorageReference mStorageRef;

    private String mCurrency;

    private AlertDialog mDialog;

    private boolean mCommand = false;


    /*Number of available pictures*/
    private int mPictures;

    private LinearLayout mColorsContainer;

    private LinearLayout mSizesContainer;

    private ColorSizeAdapter colorAdapter;

    private ColorSizeAdapter sizeAdapter;

    private TextView quantity;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param description Parameter 1.
     * @param picture Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(String[] description, byte[] picture) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putStringArray(DESCR, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDescription = getArguments().getStringArray(DESCR);
            // get user data
            commands = getDb().child("users-data").child(getUid()).child("commands");
            likes = getDb().child("users-data").child(getUid()).child("likes");
            mStorage = FirebaseStorage.getInstance();

            getCurrency();

            //Log.i(TAG,"***** number of pictures is :"+mDescription[PICTURES]);

            if(mDescription[PICTURES].equalsIgnoreCase("")){
                mPictures = 1;
            }else{
                try {
                    mPictures = Integer.valueOf(mDescription[PICTURES]);
                }catch(NumberFormatException e){
                    Log.e(TAG,e.getMessage());
                    mPictures = 1;
                }
            }

            commands.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.i(TAG,"***" + dataSnapshot.child("key").toString());
                    if(dataSnapshot.hasChildren()) {
                        //Log.i(TAG, "***" + dataSnapshot.getValue().toString());
                        for(DataSnapshot d :  dataSnapshot.getChildren()){
                            //Log.i(TAG,"***" + d.child("key").getValue().toString());
                            if(d.child("key").getValue().toString().equalsIgnoreCase(mDescription[UID])){
                                setAddButton();
                                mCommand = true;
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            likes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()) {
                        //Log.i(TAG, dataSnapshot.getValue().toString());
                        for(DataSnapshot d :  dataSnapshot.getChildren()){
                            //Log.i(TAG,d.getValue().toString());
                            if(d.getValue().toString().equalsIgnoreCase(mDescription[UID])){
                                mlike.setImageResource(R.drawable.button_liked);
                                key = d.getKey();
                                mLiked = true;
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, null);
        setHasOptionsMenu(true);
        //setDialogView();
        return setupView(v);
    }

    public void onButtonPressed(String[] details) {
        if (mListener != null) {
            mListener.onFragmentInteraction(details);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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

        TextView  item_name         = (TextView)v.findViewById(R.id.item_name);
        TextView  item_price        = (TextView)v.findViewById(R.id.item_price);
        TextView  item_description  = (TextView)v.findViewById(R.id.item_description);

        add = (FloatingActionButton) v.findViewById(R.id.fabCart);
        add.setOnClickListener(this);
        mlike = (ImageView) v.findViewById(R.id.btnLike);
        mlike.setOnClickListener(this);

        mComment = (ImageView) v.findViewById(R.id.btnComment);
        mComment.setOnClickListener(this);

        setColorsContainer(v);
        setSizesContainer(v);
        setQuantityContainer(v);

        if(mDescription != null) {
            String price;
            price = setPrice(mDescription[CURRENCY],mDescription[PRICE],getContext());
            mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/" + mDescription[UID] + "/android.png");
            item_name.setText(mDescription[NAME] + " | " + mDescription[SELLER]);
            item_price.setText(price + " "+mCurrency);
            item_description.setText(mDescription[DESCRIPTION]);
        }
        initPager(v);
        return v;
    }

    private void setQuantityContainer(View v) {
        quantity  = (TextView)v.findViewById(R.id.integer_number);
        ImageView increase = (ImageView)v.findViewById(R.id.increase);
        final ImageView decrease = (ImageView)v.findViewById(R.id.decrease);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(quantity.getText().toString());
                qty += 1;
                quantity.setText(String.valueOf(qty));
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(quantity.getText().toString());
                if(qty == 0){
                    return;
                }else{
                    qty -= 1;
                    quantity.setText(String.valueOf(qty));
                }
            }
        });
    }

    private void setSizesContainer(View v) {
        mDescription[SIZE] = mDescription[SIZE].replace("[","");
        mDescription[SIZE] = mDescription[SIZE].replace("]","");
        mSizesContainer = (LinearLayout) v.findViewById(R.id.sizes_container);
        mSizesContainer.setVisibility(View.GONE);

        if(!mDescription[SIZE].equalsIgnoreCase("")){
            mSizesContainer.setVisibility(View.VISIBLE);
            List<String> sizeList = new ArrayList<>(Arrays.asList(mDescription[SIZE].split(",")));
            Log.i(TAG,mDescription[SIZE] + "  " + sizeList.size());
            //Then get the ListView
            RecyclerView listView = (RecyclerView) v.findViewById(R.id.sizesList);
            listView.setHasFixedSize(true);
            sizeAdapter = new ColorSizeAdapter(getContext(),sizeList,ColorSizeAdapter.SIZE);

            listView.setAdapter(sizeAdapter);
            GridLayoutManager llm = new GridLayoutManager(getContext(),sizeList.size(),LinearLayoutManager.VERTICAL,false);
            listView.setLayoutManager(llm);
        }
    }

    private void setColorsContainer(View v) {
        mDescription[COLOR] = mDescription[COLOR].replace("[","");
        mDescription[COLOR] = mDescription[COLOR].replace("]","");
        mColorsContainer = (LinearLayout) v.findViewById(R.id.colors_container);
        mColorsContainer.setVisibility(View.GONE);

        if(!mDescription[COLOR].equalsIgnoreCase("")){
            mColorsContainer.setVisibility(View.VISIBLE);
            List<String> colorList = new ArrayList<>(Arrays.asList(mDescription[COLOR].split(",")));
            Log.i(TAG,mDescription[COLOR] + "  " + colorList.size());
            mColor = colorList.get(0);
            //Then get the ListView
            RecyclerView listView = (RecyclerView) v.findViewById(R.id.colorsList);
            listView.setHasFixedSize(true);
            colorAdapter = new ColorSizeAdapter(getContext(),colorList,ColorSizeAdapter.COLOR);

            listView.setAdapter(colorAdapter);
            GridLayoutManager llm = new GridLayoutManager(getContext(),colorList.size(),LinearLayoutManager.VERTICAL,false);
            listView.setLayoutManager(llm);
        }
    }

    /**
     * Inits the pager view.
     */
    private void initPager(View v)
    {
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setPageMargin(10);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                if (vDots == null || vDots.getTag() == null)
                    return;
                ((ImageView) vDots.getTag())
                        .setImageResource(R.drawable.dot_gray);
                ((ImageView) vDots.getChildAt(pos))
                        .setImageResource(R.drawable.dot_blue);
                vDots.setTag(vDots.getChildAt(pos));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        vDots = (LinearLayout) v.findViewById(R.id.vDots);

        pager.setAdapter(new PageAdapter(getContext(),mStorage,mDescription[UID],mPictures));
        setupDotbar(v);
    }

    /**
     * Setup the dotbar to show dots for pages of view pager with one dot as
     * selected to represent current page position.
     */
    private void setupDotbar(View v)
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(10, 0, 0, 0);
        vDots.removeAllViews();
        for (int i = 0; i < mPictures; i++)
        {
            ImageView img = new ImageView(v.getContext());
            img.setImageResource(i == 0 ? R.drawable.dot_blue
                    : R.drawable.dot_gray);
            vDots.addView(img, param);
            if (i == 0)
            {
                vDots.setTag(img);
            }
        }
    }

    private void setAddButton() {
        //Log.i(TAG, "Setting add button");
        add.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "Item in the cart", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCart:
                String selectedColor = "";
                String selectedSize = "";
                String selectedQty  = quantity.getText().toString();

                if(colorAdapter != null){
                    selectedColor = colorAdapter.getResource();
                    if(selectedColor.equalsIgnoreCase("")){
                        Toast.makeText(getContext(),"You need to select a color",Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                if(sizeAdapter != null){
                    selectedSize = sizeAdapter.getResource();
                    if(selectedSize.equalsIgnoreCase("")){
                        Toast.makeText(getContext(),"You need to select a size",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                Log.i(TAG,colorAdapter.getResource() + " " +  sizeAdapter.getResource() + " " + quantity.getText().toString());
                String[] details = {mDescription[UID],selectedSize, selectedColor, selectedQty};
                setAddButton();
                onButtonPressed(details);
                break;
            case R.id.btnLike:
                likePressed();
                break;
            case R.id.btnComment:
                //Log.i(TAG,"Start comment fragment");
                commentFragment f = commentFragment.newInstance(1,mDescription[UID]);
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
            mlike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
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
        public void onFragmentInteraction(String[] details);
    }

    private void getCurrency(){
        Context mContext = getContext();
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getResources().getString(R.string.uza_keys),
                Context.MODE_PRIVATE);
        mCurrency = sharedPref.getString(mContext.getString(R.string.uza_currency),"EUR");
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

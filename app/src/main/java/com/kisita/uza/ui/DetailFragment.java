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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.utils.PageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.kisita.uza.model.Data.UZA.*;

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

    private String mColor = "";

    private String mSize = "";

    private OnFragmentInteractionListener mListener;

    private Boolean mLiked = false;

    private DatabaseReference commands;
    private DatabaseReference likes;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private String mCurrency;
    private AlertDialog mDialog;
    private boolean mCommand = false;

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

            commands.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG,"***" + dataSnapshot.child("key").toString());
                    if(dataSnapshot.hasChildren()) {
                        Log.i(TAG, "***" + dataSnapshot.getValue().toString());
                        for(DataSnapshot d :  dataSnapshot.getChildren()){
                            Log.i(TAG,"***" + d.child("key").getValue().toString());
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
                        Log.i(TAG, dataSnapshot.getValue().toString());
                        for(DataSnapshot d :  dataSnapshot.getChildren()){
                            Log.i(TAG,d.getValue().toString());
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
        setDialogView();
        return setupView(v);
    }

    private void setDialogView() {
        //Preparing views
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.purchase_details, null);

        //layout_root should be the name of the "top-level" layout node in the dialog_layout.xml file.
        final Spinner size = (Spinner) layout.findViewById(R.id.size);
        final TextView tsize = (TextView) layout.findViewById(R.id.size_text);

        final Spinner color = (Spinner) layout.findViewById(R.id.color);
        final TextView tcolor = (TextView) layout.findViewById(R.id.color_text);

        final View separator1 = layout.findViewById(R.id.separator1);
        final View separator2 = layout.findViewById(R.id.separator2);


        if(!mDescription[COLOR].equalsIgnoreCase("")){

            List<String> spinnerArray =  new ArrayList<>();
            List<String> colorList = new ArrayList<>(Arrays.asList(mDescription[COLOR].split(",")));

            for(String s:colorList){
                spinnerArray.add(s);
            }

            ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this.getActivity(),
                               android.R.layout.simple_spinner_item,spinnerArray);
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            color.setAdapter(colorAdapter);
            color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mColor = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            tcolor.setVisibility(View.GONE);
            color.setVisibility(View.GONE);
            separator2.setVisibility(View.GONE);
        }

        if(!mDescription[SIZE].equalsIgnoreCase("")){
            List<String> spinnerArray =  new ArrayList<>();

            List<String> sizeList = new ArrayList<>(Arrays.asList(mDescription[SIZE].split(",")));

            for(String s:sizeList){
                spinnerArray.add(s);
            }

            ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this.getActivity()
                    ,android.R.layout.simple_spinner_item,spinnerArray);
            sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            size.setAdapter(sizeAdapter);
            size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSize = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            tsize.setVisibility(View.GONE);
            size.setVisibility(View.GONE);
            separator1.setVisibility(View.GONE);
        }


        final EditText quantity = (EditText) layout.findViewById(R.id.quantity);
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    mDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setClickable(false);
                    quantity.setError( getString(R.string.Quantity_field) );
                } else {
                    // Something into edit text. Enable the button.
                    mDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setClickable(true);
                }
            }
        });

        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);
        builder.setTitle("Details");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton(R.string.Save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"Color = "+mColor+", Size = "+mSize);

                dialog.dismiss();
                setAddButton();
                String[] details = {mDescription[UID], mSize, mColor, quantity.getText().toString()};
                onButtonPressed(details);
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog = builder.create();
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

        TextView item_name  = (TextView)v.findViewById(R.id.item_name);
        TextView  item_price  = (TextView)v.findViewById(R.id.item_price);
        TextView  item_description  = (TextView)v.findViewById(R.id.item_description);
        ImageView item_picture = (ImageView) v.findViewById(R.id.imageView1);

        add = (FloatingActionButton) v.findViewById(R.id.fabCart);
        add.setOnClickListener(this);
        mlike = (ImageView) v.findViewById(R.id.btnLike);
        mlike.setOnClickListener(this);

        mComment = (ImageView) v.findViewById(R.id.btnComment);
        mComment.setOnClickListener(this);

        if(mDescription != null) {
            mStorageRef = mStorage.getReferenceFromUrl("gs://glam-afc14.appspot.com/" + mDescription[UID] + "/android.png");
            item_name.setText(mDescription[NAME] + " | " + mDescription[SELLER]);
            item_price.setText(mDescription[PRICE] + " "+mCurrency);
            item_description.setText(mDescription[DESCRIPTION]);
            item_picture.setImageResource(R.drawable.on_sale_item6);
            // Load the image using Glide
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .fitCenter()
                    .error(R.drawable.on_sale_item6)
                    .into(item_picture);
        }
        initPager(v);

        //TODO "Show more pictures" button
        return v;
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

        pager.setAdapter(new PageAdapter());
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
        for (int i = 0; i < 5; i++)
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
        Log.i(TAG, "Setting add button");
        add.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "Item in the cart", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCart:
                mDialog.show();
                break;
            case R.id.btnLike:
                likePressed();
                break;
            case R.id.btnComment:
                Log.i(TAG,"Start comment fragment");
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
        Log.i(TAG, "button like pressed (case)");
        Map<String, Object> childUpdates = new HashMap<>();
        if (!mLiked) {
            Log.i(TAG, "mlike is false");
            String like = getDb().child("users").push().getKey();
            key = like;
            childUpdates.put("/users-data/" + getUid() + "/likes/" + like, mDescription[UID]);
            getDb().updateChildren(childUpdates);
            mlike.setImageResource(R.drawable.button_liked);
            mLiked = true;
        } else {
            Log.i(TAG, "mlike is true - key = "+key);
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
        Log.i(TAG,"****************On activity created");
        if(mLiked){
            mlike.setImageResource(R.drawable.button_liked);
        }
        if(mCommand){
            setAddButton();
        }
    }
}

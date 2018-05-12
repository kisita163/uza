package com.kisita.uza.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.kisita.uza.R;
import com.kisita.uza.activities.UzaActivity;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.internal.BiLog;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.BikekoViewPager;
import com.kisita.uza.utils.UzaBannerAdapter;
import com.kisita.uza.utils.UzaPageAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kisita.uza.utils.UzaFunctions.DRAWING;
import static com.kisita.uza.utils.UzaFunctions.FRAMING;
import static com.kisita.uza.utils.UzaFunctions.SIGNATURE;
import static com.kisita.uza.utils.UzaFunctions.TECHNIQUE;
import static com.kisita.uza.utils.UzaFunctions.getCategory;
import static com.kisita.uza.utils.UzaFunctions.getCommandState;
import static com.kisita.uza.utils.UzaFunctions.getCommandStateLogo;
import static com.kisita.uza.utils.UzaFunctions.getCurrency;
import static com.kisita.uza.utils.UzaFunctions.getDetailsDrawing;
import static com.kisita.uza.utils.UzaFunctions.getDetailsFraming;
import static com.kisita.uza.utils.UzaFunctions.getDetailsSignature;
import static com.kisita.uza.utils.UzaFunctions.getIntegerFromString;
import static com.kisita.uza.utils.UzaFunctions.infoAlertDialog;
import static com.kisita.uza.utils.UzaFunctions.itemCategoryNum;
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

    private ImageView mlike;

    protected OnFragmentInteractionListener mListener;

    private String mCurrency;

    private boolean mCommand = false;

    private Data itemData;

    private ArrayList<Data> mBannerItemsList;

    private LinearLayout sameArtistCont;

    private LinearLayout mCommandCont;

    private Button mAdd;

    private RecyclerView mBannerRecList;

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
        //printKeyHash();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        @SuppressLint("InflateParams")
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


    public void notifyChanges(ArrayList<Data> data) {
        getOtherArtworks(data);
        setFromTheSameAuthorBanner();
    }

    void getOtherArtworks(ArrayList<Data> data){
        mBannerItemsList.clear();
        if(data != null){
            for(Data d : data){
                if(d.getAuthor().equalsIgnoreCase(itemData.getAuthor()) && !d.getItemId().equalsIgnoreCase(itemData.getItemId())){
                    mBannerItemsList.add(d);
                }
            }
        }
    }

    private void setFromTheSameAuthorBanner() {
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
        // Description views
        TextView  item_signature    = v.findViewById(R.id.auth);
        TextView  item_technique    = v.findViewById(R.id.technique);
        TextView  item_drawing      = v.findViewById(R.id.tirage);
        TextView  item_framing      = v.findViewById(R.id.framing);
        //
        TextView  commandId         = v.findViewById(R.id.command_id_value);
        TextView  commandState      = v.findViewById(R.id.command_state_value);
        //TextView  commandQty        = v.findViewById(R.id.command_quantity_value);

        mCommandCont                = v.findViewById(R.id.command_state_container);
        sameArtistCont              = v.findViewById(R.id.banner_container);
        mAdd                        = v.findViewById(R.id.addToCart);

        ImageView    stateLogo      = v.findViewById(R.id.state_logo);


        if(itemData.isCommand()){
            mAdd.setVisibility(View.GONE);
            commandState.setText(getCommandState(itemData.getCommandState()));
            commandId.setText(itemData.getCommandId());
            //commandQty.setText(itemData.getQuantity());
            stateLogo.setImageResource(getCommandStateLogo(itemData.getCommandState()));
        }else if(itemData.isInCart()){
            handleItemInCart(getString(R.string.item_in_the_cart));
        }else if(!itemData.isAvailable()){
            handleItemInCart(getString(R.string.item_not_available));
        }
        else{
            mCommandCont.setVisibility(View.GONE);
            mAdd.setOnClickListener(this);
        }

        mlike =  v.findViewById(R.id.favourite);
        mlike.setOnClickListener(this);

        if(itemData.isFavourite()){
            BiLog.i(TAG,"I like this item");
            mlike.setImageResource(R.drawable.ic_action_favorite_black);
        }


        if(itemData != null) {
            String price;
            price = setPrice(itemData.getCurrency(),itemData.getPrice(),getContext());

            String s = setFormat(price) + " "+mCurrency;

            item_price.setText(s);
            item_author.setText(itemData.getAuthor());
            item_type.setText(getString(getCategory(itemCategoryNum(itemData.getType()))));
            item_name.setText(itemData.getName());
            item_size.setText(itemData.getSize());
            // Description
            String[] techniques = getResources().getStringArray(R.array.techniques_array);

            item_signature.setText(getString(getDetailsSignature(itemData.getDescription().get(SIGNATURE))));
            item_technique.setText(techniques[getIntegerFromString(itemData.getDescription().get(TECHNIQUE))]);
            item_drawing.setText(getString(getDetailsDrawing(itemData.getDescription().get(DRAWING))));
            item_framing.setText(getString(getDetailsFraming(itemData.getDescription().get(FRAMING))));
        }
        mBannerRecList = v.findViewById(R.id.cardList);
        mBannerItemsList = new ArrayList<>();
        getOtherArtworks(((UzaActivity)getActivity()).getItemsList());
        setFromTheSameAuthorBanner();
        initPager(v);

        return v;
    }

    private void handleItemInCart(String string) {
        mCommandCont.setVisibility(View.GONE);
        mAdd.setText(string);
        mAdd.setBackgroundColor(getResources().getColor(R.color.main_grey));
        mAdd.setOnClickListener(null);
    }

    /**
     * Inits the pager view.
     */
    private void initPager(View v)
    {
        /* The pager. */
        BikekoViewPager pager = v.findViewById(R.id.pager);
        pager.setPageMargin(10);
        /* The view that hold dots. */
        LinearLayout vDots = v.findViewById(R.id.vDots);

        UzaPageAdapter adapter = new UzaPageAdapter(getContext(), itemData, vDots, null);

        pager.setOnPageChangeListener(adapter);

        pager.setAdapter(adapter);
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
                                           itemData.getSize(), ""/*selected color*/,itemData.getQuantity(),"", commandKey, getUid(), itemData.getSeller()};
                    onButtonPressed(details,mCommand);
                    mCommand = true; //update case + update the quantity here for example
                }
                //BiLog.i(TAG,selectedColor + " " +  selectedSize + " " +  selectedQty);
                infoAlertDialog(getContext(),getString(R.string.item_in_the_cart));
                handleItemInCart(getString(R.string.item_in_the_cart));
                break;
            case R.id.favourite:
                likePressed();
                break;
            default:
                Toast.makeText(this.getContext(), "Unknown error occured", Toast.LENGTH_LONG).show();
        }
    }

    private void likePressed() {
        //BiLog.i(TAG, "button like pressed (case)");
        DatabaseReference likes = getDb().child("users-data").child(getUid()).child("likes");
        Map<String, Object> childUpdates = new HashMap<>();

        if (!itemData.isFavourite()) {
            //BiLog.i(TAG, "mlike is false");
            String like = getDb().child("users").push().getKey();
            childUpdates.put("/users-data/" + getUid() + "/likes/" + like,itemData.getItemId());
            getDb().updateChildren(childUpdates);
            itemData.setFavourite(true);
            itemData.setFavouriteId(like);
            mlike.setImageResource(R.drawable.ic_action_favorite_black);
        } else {
            likes.child(itemData.getFavouriteId()).removeValue();
            mlike.setImageResource(R.drawable.ic_action_favorite);
            itemData.setFavourite(false);
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
         void onItemAddedInCart(String[] details, boolean update);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //BiLog.i(TAG,"****************On activity created");
        if(itemData.isFavourite()){
            mlike.setImageResource(R.drawable.ic_action_favorite_black);
        }
    }
}

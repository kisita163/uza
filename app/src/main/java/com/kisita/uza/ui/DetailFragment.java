package com.kisita.uza.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisita.uza.MainActivity;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.PageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends CustomFragment {
    // the fragment initialization parameters
    private static final String DESCRIPTION = "description";
    private static final String PICTURES = "pictures";

    private String [] mDescription;
    private String mPictures;

    /** The pager. */
    private ViewPager pager;

    /** The view that hold dots. */
    private LinearLayout vDots;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param description Parameter 1.
     * @param pictures Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(String [] description, String pictures) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putStringArray(DESCRIPTION, description);
        args.putString(PICTURES, pictures);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDescription = getArguments().getStringArray(DESCRIPTION);
            mPictures = getArguments().getString(PICTURES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, null);

        ((MainActivity) getActivity()).toolbar.setTitle(getResources().getString(R.string.app_name));//TODO product name
        ((MainActivity) getActivity()).toolbar.findViewById(
                R.id.spinner_toolbar).setVisibility(View.GONE);

        setHasOptionsMenu(true);
        return setupView(v);
    }

    public void onButtonPressed(String key) {
        if (mListener != null) {
            mListener.onFragmentInteraction(key);
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

        if(mDescription != null) {
            item_name.setText(mDescription[Data.UzaData.NAME.ordinal()] + " | " + mDescription[Data.UzaData.SELLER.ordinal()]);
            item_price.setText(mDescription[Data.UzaData.PRICE.ordinal()]);
            item_description.setText(mDescription[Data.UzaData.DESCRIPTION.ordinal()]);
        }

        initAddButton(v);
        initPager(v);

        //TODO "Show more pictures" button
        //TODO "Message" button
        //TODO "Heart" button

        return v;
    }

    private void initAddButton(View v) {
        Button add = (Button)v.findViewById(R.id.fabCart);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(mDescription[Data.UzaData.UID.ordinal()]);
            }
        });
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
        public void onFragmentInteraction(String key);
    }
}

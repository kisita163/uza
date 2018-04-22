package com.kisita.uza.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kisita.uza.R;

import static com.kisita.uza.custom.CustomActivity.BikekoMenu.ARTWORKS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    private OnHomeInteractionListener mListener;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StartFragment.
     */

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        FrameLayout artworks = v.findViewById(R.id.bikeko_artworks);
        //FrameLayout artists  = v.findViewById(R.id.bikeko_artists);

        artworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(ARTWORKS.ordinal());
            }
        });

        /*artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(ARTISTS);
            }
        });*/

        ImageView img = v.findViewById(R.id.home_image);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/glam-afc14.appspot.com/o/home_image%2Fdanseurs.png?alt=media&token=44632681-ac0a-421d-b228-689b9071ba4b")
                .fitCenter()
                .error(R.drawable.danseurs)
                .into(img);


        return v;
    }


    public void onButtonPressed(int button) {
        if (mListener != null) {
            mListener.onHomeInteraction(button);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeInteractionListener) {
            mListener = (OnHomeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnHomeInteractionListener {
        void onHomeInteraction(int button);
    }
}

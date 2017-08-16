package com.kisita.uza.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.kisita.uza.ui.FixedContents.ElectronicsContent;
import com.kisita.uza.ui.FixedContents.FoodContent;
import com.kisita.uza.ui.FixedContents.HomeContent;
import com.kisita.uza.ui.FixedContents.KidsContent;
import com.kisita.uza.ui.FixedContents.MenContent;
import com.kisita.uza.utils.ColorSizeAdapter;
import com.kisita.uza.utils.UzaSpinnerAdapter;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomFragment;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNewArticleInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewArticleFragment extends CustomFragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    /** The view that hold dots. */
    private LinearLayout vDots;

    private Button mAddPicture;

    ImageView uploadedImg0;
    ImageView uploadedImg1;
    ImageView uploadedImg2;
    ImageView uploadedImg3;
    ImageView uploadedImg4;

    Spinner currencySpinner;
    Spinner categorySpinner;
    Spinner typeSpinner;

    UzaSpinnerAdapter typeAdapter;

    private Button addColorButton;
    private Button removeColorButton;

    private List<String> colorList;


    private Button addSizeButton;
    private Button removeSizeButton;


    ImageView [] uploadedImages = {uploadedImg0,uploadedImg1,uploadedImg2,uploadedImg3,uploadedImg4};
    boolean [] uploadedImagesStatus = {false,false,false,false,false};

    private OnNewArticleInteractionListener mListener;
    private PageAdapter mPagerAdapter;
    private ViewPager mPager;
    private ColorSizeAdapter colorAdapter;
    private ArrayList mSelectedItems; // Where we track the selected items

    public NewArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     * @return A new instance of fragment NewArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewArticleFragment newInstance() {
        NewArticleFragment fragment = new NewArticleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_new_article, container, false);
        initView(v);
        initPager(v);
        setCurrencyView(v);
        setCategoryView(v);
        setTypeView(v);
        setColorsButtons(v);
        setSizesButtons(v);
        setColorsContainer(v);
        return v;
    }

    private void setSizesButtons(View v) {
        final String [] sizeSystems = getResources().getStringArray(R.array.sizeSystems);
        addSizeButton = (Button)v.findViewById(R.id.add_size);
        removeSizeButton = (Button)v.findViewById(R.id.remove_size);
        addSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//,R.style.dialog);
                builder.setTitle("Select size system")
                        .setSingleChoiceItems(R.array.sizeSystems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Selected system is : ",sizeSystems[which]) ;
                                selecteSizesDialog(sizeSystems[which]);
                            }
                        })
                        // Set the action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create();
                builder.show();
            }
        });

        removeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void selecteSizesDialog(String sizeSystem) {
        mSelectedItems = new ArrayList(); // Where we track the selected items

    }

    /* This function set the currency field*/
    private void setCurrencyView(View v) {
        currencySpinner = (Spinner)v.findViewById(R.id.currency);
        UzaSpinnerAdapter adapter = new UzaSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.addAll(new String[]{"CDF","EUR","USD"});

        currencySpinner.setAdapter(adapter);
    }

    private void setCategoryView(View v) {
        categorySpinner = (Spinner)v.findViewById(R.id.category);
        UzaSpinnerAdapter adapter = new UzaSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.addAll(new String[]{"Men","Women","Kids","Electronics","Home","Food"});
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSpinner.setEnabled(true);
                switch (categorySpinner.getSelectedItem().toString()){

                    case "Men":
                    case "Women":
                        typeAdapter.clear();
                        typeAdapter.addAll(MenContent.categories);
                        typeAdapter.notifyDataSetChanged();
                        break;
                    case "Kids":
                        typeAdapter.clear();
                        typeAdapter.addAll(KidsContent.categories);
                        typeAdapter.notifyDataSetChanged();
                        break;
                    case "Electronics":
                        typeAdapter.clear();
                        typeAdapter.addAll(ElectronicsContent.categories);
                        typeAdapter.notifyDataSetChanged();
                        break;
                    case "Home":
                        typeAdapter.clear();
                        typeAdapter.addAll(HomeContent.categories);
                        typeAdapter.notifyDataSetChanged();
                        break;
                    case "Food":
                        typeAdapter.clear();
                        typeAdapter.addAll(FoodContent.categories);
                        typeAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTypeView(View v) {
        typeSpinner = (Spinner)v.findViewById(R.id.type);
        typeAdapter = new UzaSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setEnabled(false);
    }

    private void setColorsButtons(View v) {
        addColorButton = (Button)v.findViewById(R.id.add_color);
        removeColorButton = (Button)v.findViewById(R.id.remove_color);
        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ColorPicker cp = new ColorPicker(getActivity());
                cp.show();
                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        Log.i("Selected color is :",color+"");
                        String strColor = String.format("#%06X", 0xFFFFFF & color);
                        Log.i("Selected color is :",strColor+"");
                        colorList.add(strColor);
                        colorAdapter.setColorSizeObjects(strColor); // For visualization
                        colorAdapter.notifyDataSetChanged();
                        cp.cancel();
                    }
                });
            }
        });

        removeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(colorList.size() >= 1)
                    colorList.remove(colorList.size()-1);*/
                colorAdapter.removeSelectedColorSizeObjects(); // For visualization
                colorAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setColorsContainer(View v) {
        RecyclerView listView = (RecyclerView) v.findViewById(R.id.colorsList);
        listView.setHasFixedSize(true);
        colorList = new ArrayList<>();
        colorAdapter = new ColorSizeAdapter(null,getContext(),colorList,ColorSizeAdapter.COLOR,null);

        listView.setAdapter(colorAdapter);
        GridLayoutManager llm = new GridLayoutManager(getContext(),getDivider(getScreenWidth(getContext())), LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(llm);
    }

    private void initView(View v) {
        mPager = (ViewPager)v.findViewById(R.id.pager);
        mAddPicture = (Button)v.findViewById(R.id.add_picture);
        mAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNewArticleInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewArticleInteractionListener) {
            mListener = (OnNewArticleInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewArticleInteractionListener");
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
    public interface OnNewArticleInteractionListener {
        // TODO: Update argument type and name
        void onNewArticleInteraction(Uri uri);
    }

    /**
     * Inits the pager view.
     */
    private void initPager(View v)
    {
		/* The pager. */
        ViewPager pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setPageMargin(10);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos)
            {
                if (vDots == null || vDots.getTag() == null)
                    return;
                ((ImageView) vDots.getTag())
                        .setImageResource(R.drawable.dot_gray);
                ((ImageView) vDots.getChildAt(pos))
                        .setImageResource(R.drawable.dot_blue);
                vDots.setTag(vDots.getChildAt(pos));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
        vDots = (LinearLayout) v.findViewById(R.id.vDots);
        mPagerAdapter = new PageAdapter();
        pager.setAdapter(mPagerAdapter);
        setupDotbar();
    }

    /**
     * Setup the dotbar to show dots for pages of view pager with one dot as
     * selected to represent current page position.
     */
    private void setupDotbar()
    {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(10, 0, 0, 0);
        vDots.removeAllViews();
        for (int i = 0; i < 5; i++)
        {
            ImageView img = new ImageView(getContext());
            img.setImageResource(i == 0 ? R.drawable.dot_blue
                    : R.drawable.dot_gray);
            vDots.addView(img, param);
            if (i == 0)
            {
                vDots.setTag(img);
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            uploadedImages[mPager.getCurrentItem()].setImageBitmap(imageBitmap);
            uploadedImagesStatus[mPager.getCurrentItem()] = true;

            mPagerAdapter.notifyDataSetChanged();
        }
    }


    /**
     * The Class PageAdapter is adapter class for ViewPager and it simply holds
     * a Single image view with dummy images. You need to write logic for
     * loading actual images.
     */
    private class PageAdapter extends PagerAdapter {

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        private StorageReference mReference;
        @Override
        public int getCount() {
            return uploadedImages.length;
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
         */
        @Override
        public Object instantiateItem(ViewGroup container, int arg0) {

            uploadedImages[arg0] = (ImageView) getLayoutInflater(null).inflate(
                    R.layout.login_images, null);
            uploadedImages[arg0].setScaleType(ImageView.ScaleType.FIT_CENTER);
            container.addView(uploadedImages[arg0],
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            return uploadedImages[arg0];
        }

        /* (non-Javadoc)
        * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
        */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {

        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}

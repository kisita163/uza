package com.kisita.uza.listerners;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

/**
 * Created by Hugues on 12/05/2017.
 */
public class PictureEventListener implements OnFailureListener, OnSuccessListener<byte[]> {
    final private String TAG = "### PicEventListener";
    private Data mData;
    private UzaCardAdapter mAdapter;
    private FirebaseStorage mStorage;
    private Bitmap mBitmap;
    private String mKey;

    public PictureEventListener(Data data, UzaCardAdapter adapter, String key) {
        this.mAdapter = adapter;
        this.mData = data;
        this.mKey = key;
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onSuccess(byte[] bytes) {
        Log.i(TAG, "File downloaded for : " + mData.getUid());
        mData.setmPicBytes(bytes);
        mAdapter.notifyDataSetChanged();
    }
}

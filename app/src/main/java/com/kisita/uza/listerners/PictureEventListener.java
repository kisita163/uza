package com.kisita.uza.listerners;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kisita.uza.model.Data;
import com.kisita.uza.utils.UzaCardAdapter;

/**
 * Created by Hugues on 12/05/2017.
 */
public class PictureEventListener implements OnFailureListener, OnSuccessListener<byte[]> {
    final private String TAG = "### PicEventListener";
    private Data mData;
    private UzaCardAdapter mAdapter;


    public PictureEventListener(Data data, UzaCardAdapter adapter, String key) {
        this.mAdapter = adapter;
        this.mData = data;
    }

    @Override
    public void onFailure(Exception e) {
        Log.i(TAG, "Could not download file for : " + mData.getUid());
    }

    @Override
    public void onSuccess(byte[] bytes) {
        Log.i(TAG, "File downloaded for : " + mData.getUid());
        mData.setmPicBytes(bytes);
        mAdapter.notifyDataSetChanged();
    }
}

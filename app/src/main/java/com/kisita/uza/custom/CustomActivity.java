package com.kisita.uza.custom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;

import com.kisita.uza.R;
import com.kisita.uza.model.Data;
import com.kisita.uza.provider.UzaContract;
import com.kisita.uza.services.FirebaseService;
import com.kisita.uza.ui.ItemsFragment;
import com.kisita.uza.utils.TouchEffect;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

import static com.kisita.uza.model.Data.ITEMS_COLUMNS;

/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like setting a Theme to activity.
 */
@SuppressLint("Registered")
public class CustomActivity extends AppCompatActivity implements
		OnClickListener,
		ItemsFragment.OnItemFragmentInteractionListener,
		LoaderManager.LoaderCallbacks<Cursor>
{
	protected ArrayList<Data> itemsList;

	protected boolean mListFilled = false;


	//we are going to use a handler to be able to run in our TimerTask
	final Handler handler = new Handler();

	Timer scheduledLoad;
	TimerTask mTimerTask;

	public enum BikekoMenu {
		HOME,
		ARTWORKS,
		//ARTISTS,
		CART,
		FAVOURITES,
		COMMANDS,
		FILTERS,
		LOGS,
		BILLING,
		LOGOUT
	}

	public static final TouchEffect TOUCH = new TouchEffect();
	/**
	 * Apply this Constant as touch listener for views to provide alpha touch
	 * effect. The view must have a Non-Transparent background.
	 */

	protected boolean mBound = false;

	protected FirebaseService mService;

	public String TAG = "###"+ getClass().getName();

	private ProgressDialog mProgressDialog;

	public final static String BRAINTREE_SERVER = "http://www.e-kisita.com/braintree/application/requests.php";

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.BASE)
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		itemsList = new ArrayList<>();
		loadData();
		initializeTimerTask();
		scheduledLoad = new Timer("load data");
		scheduledLoad.schedule(mTimerTask, 500, 1500); // Verify itemData after 500 ms
	}

	private void initializeTimerTask() {
		// this will run when timer elapses
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(itemsList.size() == 0 && !mListFilled) { // items list is empty. Try to load again
							Log.i(TAG,"Items list is empty. Try to load again");
							loadData();
						}else{
							stopScheduledTask();
						}
					}
				});
			}

		};
	}



	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
	}

	public void showProgressDialog(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this, R.style.UzaAlertDialogTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMessage(message);
		}

		mProgressDialog.show();
	}

	public void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	public void fetchAuthorization() {

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("token", true);

		client.post(BRAINTREE_SERVER, params, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				onAuthorizationFetched(null);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String clientToken) {
				onAuthorizationFetched(clientToken);
			}
		});
	}

	/**
	 * Load  product data for displaying on the RecyclerView.
	 */
	private void  loadData()
	{
		showProgressDialog(getString(R.string.loading));
		if (getSupportLoaderManager().getLoader(1) == null){
			getSupportLoaderManager().initLoader(1, null, this);
		}else{
			getSupportLoaderManager().restartLoader(1,null,this);
		}
	}

	public void sendPaymentNonce(String nonce,String amount){
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams requestParams = new RequestParams();
		requestParams.put("payment_method_nonce",nonce);
		requestParams.put("amount",amount);

		client.post(BRAINTREE_SERVER, requestParams, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				onTransactionDone(responseString);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				onTransactionDone(responseString);
			}
		});
	}

	/** Defines callbacks for service binding, passed to bindService() */
	protected ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
									   IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			Log.i(TAG, "Connected to Firebase service");
			FirebaseService.LocalBinder binder = (FirebaseService.LocalBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.i(TAG, "Disconnected from Firebase service");
			mBound = false;
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, FirebaseService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(mConnection);
		stopScheduledTask();
		mBound = false;
	}

	protected void onAuthorizationFetched(String token) {}
	protected void onTransactionDone(String responseString) {}


	@Override
	public void onItemFragmentInteraction(String title) {}

	@Override
	public void onCommandSelectedInteraction(String key) {}

	/**
	 * Instantiate and return a new Loader for the given ID.
	 * <p>
	 * <p>This will always be called from the process's main thread.
	 *
	 * @param id   The ID whose loader is to be created.
	 * @param args Any arguments supplied by the caller.
	 * @return Return a new Loader instance that is ready to start loading.
	 */
	@NonNull
	@Override
	public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		Uri PlacesUri = UzaContract.ItemsEntry.CATEGORY_URI;
		return new android.support.v4.content.CursorLoader(this,
				PlacesUri,
				ITEMS_COLUMNS,
				"Arts",
				null,
				null);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
		if( data.isBeforeFirst()) {
			hideProgressDialog();
			itemsList.clear();
			mListFilled = true;
		}

		while (data.moveToNext()) {
			Log.i(TAG, "/!\\"+data.getString(0));
			for(int i = 1 ; i < data.getColumnCount() ; i ++ ){
				if(data.getString(i) == null){
					Log.i(TAG, "\t/!\\ null");
				}
				else {
					Log.i(TAG,"\t/!\\"+ data.getString(i));
				}
			}
			Data d = new Data(data);
            /*int index = Arrays.binarySearch(itemsList.toArray(), d); // Is the new data already in my list?
            if(index > 0){
                Log.i(TAG,"======== This item exist in the array " + index);
                itemsList.set(index,d);
            }else{
                itemsList.add(d);
            }*/
			itemsList.add(d);
			Log.i(TAG,"**ID"+d.getPrice());
		}
		notifyChanges();
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> loader) {

	}

	private void stopScheduledTask() {
		// Stopping timer task
		if(scheduledLoad != null) {
			scheduledLoad.cancel();
			scheduledLoad.purge();
			scheduledLoad = null;
		}
	}

	protected void notifyChanges(){

	}

	public ArrayList<Data> getItemsList() {
		Log.i(TAG,"------------> get the list " + itemsList.size());
		return itemsList;
	}
}

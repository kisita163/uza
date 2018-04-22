package com.kisita.uza.custom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.uza.services.FirebaseService;
import com.kisita.uza.ui.CommentFragment;
import com.kisita.uza.ui.ItemsFragment;
import com.kisita.uza.utils.TouchEffect;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import static com.kisita.uza.model.Data.ITEMS_COMMANDS_COLUMNS;

/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like setting a Theme to activity.
 */
@SuppressLint("Registered")
public class CustomActivity extends AppCompatActivity implements
		OnClickListener,
		CommentFragment.OnListFragmentInteractionListener,
		ItemsFragment.OnItemFragmentInteractionListener
{

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
			mProgressDialog = new ProgressDialog(this);
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
		mBound = false;
	}

	protected void onAuthorizationFetched(String token) {}
	protected void onTransactionDone(String responseString) {}

	@Override
	public void onListFragmentInteraction(CommentFragment.ArticleComment item) {}

	@Override
	public void onItemFragmentInteraction(String title) {}

	@Override
	public void onCommandSelectedInteraction(String key) {}
}

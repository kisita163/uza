package com.kisita.uza.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.User;

/**
 * The Activity LoginActivity is launched after the Home screen. You need to write your
 * logic for actual LoginActivity. You also need to implement Facebook LoginActivity if
 * required.
 */
public class LoginActivity extends CustomActivity
{
	/** Firebase fields */
	private static final String TAG = "SignInActivity";

	private DatabaseReference mDatabase;
	private FirebaseAuth mAuth;

	/** VIEW */

	private EditText mEmailField;
	private EditText mPasswordField;
	private Button mSignInButton;
	private Button mSignUpButton;
	private Button mForgotButton;
	private EditText mName;
	private EditText mPhoneNumber;
	private EditText mConfirmPassword;

	private boolean signUp = false;


	/** The view that hold dots. */
	private LinearLayout vDots;
	private LoginButton loginButton;
	private CallbackManager callbackManager;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		try {
			//Log.i(TAG,"Make data persistent");
			FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		}catch (Exception e){
			//Log.i(TAG,"The app crash arguing that this instance must be called prior to other instances");
		}
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mAuth = FirebaseAuth.getInstance();

		mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.keepSynced(true);
		/*
			This listener get exchange rate and shipping cost from firebase database
		 */
		final Query itemsQuery = getMoneyQuery(mDatabase);
		itemsQuery.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);

				SharedPreferences.Editor editor = sharedPref.edit();

				for(DataSnapshot d  :  dataSnapshot.getChildren()){
					Log.i(TAG,"******* key = "+d.getKey() + " value = "+d.getValue());
					if(d.getKey().equalsIgnoreCase("eur-cdf")){
						editor.putString("eur-cdf",d.getValue().toString());
					}else if(d.getKey().equalsIgnoreCase("usd-cdf")){
						editor.putString("usd-cdf",d.getValue().toString());
					}else if(d.getKey().equalsIgnoreCase("usd-eur")){
						editor.putString("usd-eur",d.getValue().toString());
					}else if(d.getKey().equalsIgnoreCase("region1")){
						editor.putString("region1",d.getValue().toString());
					}else if(d.getKey().equalsIgnoreCase("region2")){
						editor.putString("region2",d.getValue().toString());
					}else if(d.getKey().equalsIgnoreCase("region3")){
						editor.putString("region3",d.getValue().toString());
					}
				}
				editor.apply();
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				System.out.println("Something changed in money" + dataSnapshot.getKey());
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				System.out.println("Something removed in money");
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				System.out.println("Something moved in money");
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		callbackManager = CallbackManager.Factory.create();

		setupView();
	}

	@Override
	public void onStart() {
		super.onStart();

		// Check auth on Activity start
		if (mAuth.getCurrentUser() != null) {
			onAuthSuccess(mAuth.getCurrentUser());
		}
	}

	private void onAuthSuccess(FirebaseUser user) {
		String username = usernameFromEmail(user.getEmail());

		// Write new user
		writeNewUser(user.getUid(), username, user.getEmail());

		// Go to MainActivity
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
		finish();
	}

	/**
	 * Setup the click & other events listeners for the view components of this
	 * screen. You can add your logic for Binding the data to TextViews and
	 * other views as per your need.
	 */
	private void setupView()
	{
		// Views
		//Name in case of sign up
		mName = (EditText) findViewById(R.id.field_name);
		mName.setVisibility(View.GONE);

		//Phone number in case of sign up
		mPhoneNumber = (EditText) findViewById(R.id.field_number);
		mPhoneNumber.setVisibility(View.GONE);

		//Email
		mEmailField = (EditText) findViewById(R.id.field_email);
		mPasswordField = (EditText) findViewById(R.id.field_password);

		//Confirm password
		mConfirmPassword = (EditText) findViewById(R.id.field_confirm_password);
		mConfirmPassword.setVisibility(View.GONE);

		mSignInButton = (Button) findViewById(R.id.button_sign_in);
		mSignInButton.setOnClickListener(this);

		mForgotButton= (Button)findViewById(R.id.btnForget);

		loginButton = (LoginButton)findViewById(R.id.login_button);
		loginButton.setReadPermissions("email");

		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				handleFacebookAccessToken(loginResult.getAccessToken());
			}

			@Override
			public void onCancel() {
				//Log.i(TAG,"Facebook onCancel");
			}

			@Override
			public void onError(FacebookException error) {
				//Log.i(TAG,"Facebook onError "+error.getMessage());

			} });

		mSignUpButton = (Button) findViewById(R.id.btnReg);
		mSignUpButton.setOnClickListener(this);

		initPager();
	}

	/**
	 * Inits the pager view.
	 */
	private void initPager()
	{
		/* The pager. */
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setPageMargin(10);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

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
		vDots = (LinearLayout) findViewById(R.id.vDots);

		pager.setAdapter(new PageAdapter());
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
			ImageView img = new ImageView(this);
			img.setImageResource(i == 0 ? R.drawable.dot_blue
					: R.drawable.dot_gray);
			vDots.addView(img, param);
			if (i == 0)
			{
				vDots.setTag(img);
			}

		}
	}

	/* (non-Javadoc)
	 * @see com.taxi.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.button_sign_in)
		{
			/*Intent i = new Intent(this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();*/
			if(signUp){
				signUp();
			}else{
				signIn();
			}
		}

		if(v.getId() == R.id.btnReg){
			showProgressDialog(getString(R.string.please_wait));
			mName.setVisibility(View.VISIBLE);
			mPhoneNumber.setVisibility(View.VISIBLE);
			mConfirmPassword.setVisibility(View.VISIBLE);

			//loginButton.setVisibility(View.GONE);
			mForgotButton.setVisibility(View.GONE);
			mSignUpButton.setVisibility(View.GONE);

			signUp = true;

			mSignInButton.setText(R.string.submit);
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(2000);

					} catch (Exception e)
					{
						e.printStackTrace();
					} finally
					{
						runOnUiThread(new Runnable() {
							@Override
							public void run()
							{
								hideProgressDialog();
							}
						});
					}
				}
			}).start();
		}
	}

	private boolean validateSignUpForm() {
		boolean result = true;
		if (TextUtils.isEmpty(mName.getText().toString())) {
			mName.setError(getString(R.string.required));
			result = false;
		} else {
			mName.setError(null);
		}

		if (TextUtils.isEmpty(mPhoneNumber.getText().toString())) {
			mPhoneNumber.setError(getString(R.string.required));
			result = false;
		} else {
			mPhoneNumber.setError(null);
		}

		if (TextUtils.isEmpty(mEmailField.getText().toString())) {
			mEmailField.setError(getString(R.string.required));
			result = false;
		} else {
			mEmailField.setError(null);
		}

		if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
			mPasswordField.setError(getString(R.string.required));
			result = false;
		} else {
			mPasswordField.setError(null);
		}

		if (TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
			mConfirmPassword.setError(getString(R.string.required));
			result = false;
		} else {
			mConfirmPassword.setError(null);
		}

		if(!mConfirmPassword.getText().toString().equals(mPasswordField.getText().toString())){
			mConfirmPassword.setError(getString(R.string.password_not_equal));
			result = false;
		} else {
			mPasswordField.setError(null);
			mConfirmPassword.setError(null);
		}


		return result;
	}

	private boolean validateForm() {
		boolean result = true;
		if (TextUtils.isEmpty(mEmailField.getText().toString())) {
			mEmailField.setError(getString(R.string.required));
			result = false;
		} else {
			mEmailField.setError(null);
		}

		if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
			mPasswordField.setError(getString(R.string.required));
			result = false;
		} else {
			mPasswordField.setError(null);
		}

		return result;
	}

	private void signUp() {
		if (!validateSignUpForm()) {
			return;
		}
		showProgressDialog(getString(R.string.please_wait));

		/*String name = mName.getText().toString();
		String phone = mPhoneNumber.getText().toString();*/
		String email = mEmailField.getText().toString();
		String password = mPasswordField.getText().toString();

		mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					// Sign in success, update UI with the signed-in user's information
					Log.d(TAG, "createUserWithEmail:success");
					FirebaseUser user = mAuth.getCurrentUser();
					onAuthSuccess(user);
					hideProgressDialog();
				} else {
					// If sign in fails, display a message to the user.
					//Log.w(TAG, "createUserWithEmail:failure", task.getException());
					Toast.makeText(LoginActivity.this, getString(R.string.authentication_failed)+task.getException().getMessage(),
							Toast.LENGTH_LONG).show();
					//updateUI(null);
					hideProgressDialog();
				}
			}
		});
	}

	private void signIn() {
		//Log.d(TAG, "signIn");
		if (!validateForm()) {
			return;
		}

		showProgressDialog(getString(R.string.please_wait));
		String email = mEmailField.getText().toString();
		String password = mPasswordField.getText().toString();

		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
						hideProgressDialog();

						if (task.isSuccessful()) {
							onAuthSuccess(task.getResult().getUser());
						} else {
							Toast.makeText(LoginActivity.this, "Sign In Failed",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	// [START basic_write]
	private void writeNewUser(String userId, String name, String email) {
		User user = new User(name, email);

		mDatabase.child("users").child(userId).setValue(user);
	}

	private String usernameFromEmail(String email) {
		if (email.contains("@")) {
			return email.split("@")[0];
		} else {
			return email;
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
			return 5;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int arg0) {
			final ImageView img = (ImageView) getLayoutInflater().inflate(
					R.layout.login_images, null);
			mReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://glam-afc14.appspot.com/login/android.png");

			//img.setImageResource(R.drawable.img_signin);
			Glide.with(getApplicationContext())
					.using(new FirebaseImageLoader())
					.load(mReference)
					.fitCenter()
					.error(R.drawable.on_sale_item6)
					.into(img);
			container.addView(img,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			return img;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	private void handleFacebookAccessToken(AccessToken token) {
		AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							//Log.i(TAG,"The user is  : "+user.getEmail().toString());
							onAuthSuccess(user);
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
						}
					}
				});
	}

	public Query getMoneyQuery(DatabaseReference databaseReference) {
		return databaseReference.child("money");
	}
}

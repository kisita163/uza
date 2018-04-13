package com.kisita.uza.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisita.uza.R;
import com.kisita.uza.custom.CustomActivity;
import com.kisita.uza.model.User;
import com.kisita.uza.services.FirebaseService;

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

	private CallbackManager callbackManager;
	private boolean mNewUser = false;
	private boolean mFacebookUser = false;

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
		mDatabase.keepSynced(true);

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

	private void onAuthSuccess(final FirebaseUser user) {
		String name;
		String phone;
		// Write new user
		if(mNewUser) {
			name  = mName.getText().toString();
			phone = mPhoneNumber.getText().toString();

			user.sendEmailVerification()
					.addOnCompleteListener(this, new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()){
								Toast.makeText(LoginActivity.this,
										getString(R.string.email_verification) +  "  " + user.getEmail(),
										Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(LoginActivity.this,
										R.string.send_verification_fail,
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			writeNewUser(user.getUid(),name, user.getEmail(),phone);
		}

		if(mFacebookUser){
			name  = Profile.getCurrentProfile().getName();
			phone = "";
			writeNewUser(user.getUid(),name, user.getEmail(),phone);
		}

		startService(new Intent(LoginActivity.this,FirebaseService.class));
		// Go to MainActivity
		Intent startMain = new Intent(LoginActivity.this,DrawerActivity.class);
		startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(startMain);

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
		mName = findViewById(R.id.field_name);
		mName.setVisibility(View.GONE);

		//Phone number in case of sign up
		mPhoneNumber = findViewById(R.id.field_number);
		mPhoneNumber.setVisibility(View.GONE);

		//Email
		mEmailField = findViewById(R.id.field_email);
		mPasswordField = findViewById(R.id.field_password);

		//Confirm password
		mConfirmPassword = findViewById(R.id.field_confirm_password);
		mConfirmPassword.setVisibility(View.GONE);

		mSignInButton = findViewById(R.id.button_sign_in);
		mSignInButton.setOnClickListener(this);

		mForgotButton= findViewById(R.id.btnForget);

		LoginButton loginButton = findViewById(R.id.login_button);
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

		mSignUpButton = findViewById(R.id.btnReg);
		mSignUpButton.setOnClickListener(this);
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

		String email = mEmailField.getText().toString();
		String password = mPasswordField.getText().toString();

		mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					// Sign in success, update UI with the signed-in user's information
					Log.d(TAG, "createUserWithEmail:success");
					FirebaseUser user = mAuth.getCurrentUser();
					mNewUser      = true;
					mFacebookUser = false; // Just to be sure
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
	private void writeNewUser(String userId, String name, String email, String phone) {
		Log.i(TAG,"Writing new user in database...");
		SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.uza_keys), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.putString(getString(R.string.uza_billing_name),name);
		editor.putString(getString(R.string.uza_billing_phone),phone);
		editor.apply();

		User user = new User(name, email,phone);
		mDatabase.child("users").child(userId).setValue(user);
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
							mFacebookUser = true;
							mNewUser      = false; // Just to be sure
							onAuthSuccess(user);
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
						}
					}
				});
	}
}

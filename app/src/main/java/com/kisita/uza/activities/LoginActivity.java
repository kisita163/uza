package com.kisita.uza.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

	/** The pager. */
	private ViewPager pager;

	/** The view that hold dots. */
	private LinearLayout vDots;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		try {
			Log.i(TAG,"Make data persistent");
			FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		}catch (Exception e){
			Log.i(TAG,"The app crash arguing that this instance must be called prior to other instances");
		}
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mAuth = FirebaseAuth.getInstance();

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
		mEmailField = (EditText) findViewById(R.id.field_email);
		mPasswordField = (EditText) findViewById(R.id.field_password);
		mSignInButton = (Button) findViewById(R.id.button_sign_in);

		Button b = (Button) setTouchNClick(R.id.btnReg);
		b.setText(Html.fromHtml(getString(R.string.sign_up)));

		setTouchNClick(R.id.button_sign_in);
		setTouchNClick(R.id.btnForget);
		setTouchNClick(R.id.btnFb);

		initPager();
	}

	/**
	 * Inits the pager view.
	 */
	private void initPager()
	{
		pager = (ViewPager) findViewById(R.id.pager);
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

	/**
	 * The Class PageAdapter is adapter class for ViewPager and it simply holds
	 * a Single image view with dummy images. You need to write logic for
	 * loading actual images.
	 */
	private class PageAdapter extends PagerAdapter
	{

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return 5;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup, int)
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int arg0)
		{
			final ImageView img = (ImageView) getLayoutInflater().inflate(
					R.layout.img, null);

			img.setImageResource(R.drawable.img_signin);

			container.addView(img,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			return img;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			try
			{
				// super.destroyItem(container, position, object);
				// if(container.getChildAt(position)!=null)
				// container.removeViewAt(position);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
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
			signIn();
		}
	}

	private boolean validateForm() {
		boolean result = true;
		if (TextUtils.isEmpty(mEmailField.getText().toString())) {
			mEmailField.setError("Required");
			result = false;
		} else {
			mEmailField.setError(null);
		}

		if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
			mPasswordField.setError("Required");
			result = false;
		} else {
			mPasswordField.setError(null);
		}

		return result;
	}

	private void signIn() {
		Log.d(TAG, "signIn");
		if (!validateForm()) {
			return;
		}

		showProgressDialog();
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
}

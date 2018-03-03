package com.kisita.uza.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.uza.R;


/**
 * The Class SplashScreen will launched at the start of the application. It will
 * be displayed for 3 seconds and than finished automatically and it will also
 * start the next activity of app.
 */
public class SplashScreen extends Activity
{

    private static final String TAG = "SplashScreen";
    /** Check if the app is running. */
	private boolean isRunning;

	private ImageView uzaLogo;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);
		uzaLogo = (ImageView) findViewById(R.id.lady) ;

		isRunning = true;
		// Connect the user as anonymous user (Login: kisita2002@yahoo.fr Pass : kisita)
		// For buying anything, the user will have to introduce his personal info
        signIn(FirebaseAuth.getInstance(),"kisita2002@yahoo.fr","kisita");
		//Blink UZA logo
		manageBlink();
		//Start app after 4 seconds
		startSplash();
	}

	private void manageBlink(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(uzaLogo,"alpha",0.1f);
        anim.setDuration(1500);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
	}

    private void signIn(FirebaseAuth auth, String email, String password) {
        // If the user as already entered his credentials, don't connect him as an anonymous user
        if (auth.getCurrentUser() != null) {
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(SplashScreen.this, "Connexion to the remote server failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

	/**
	 * Starts the count down timer for 3-seconds. It simply sleeps the thread
	 * for 4-seconds.
	 */
	private void startSplash()
	{

		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try
				{

					Thread.sleep(4000);

				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							doFinish();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * If the app is still running than this method will start the LoginActivity
	 * activity and finish the Splash.
	 */
	private synchronized void doFinish()
	{

		if (isRunning)
		{
			isRunning = false;
			Intent i = new Intent(SplashScreen.this, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			isRunning = false;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
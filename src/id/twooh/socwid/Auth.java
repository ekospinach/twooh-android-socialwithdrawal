package id.twooh.socwid;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import id.twooh.socwid.R;
import id.facebook.android.DialogError;
import id.facebook.android.Facebook;
import id.facebook.android.Facebook.DialogListener;
import id.facebook.android.FacebookError;

@SuppressWarnings("unused")
public class Auth extends Activity implements OnClickListener{
	
	static Facebook facebook = new Facebook("283539765022300"); // Application ID of your app at facebook
    boolean isLoggedIn = false;
    String fbtokk;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.auth);
        
        View auth = findViewById(R.id.auth_button);
        auth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.auth_button)
		{
			
			//Implementing SSO
	        facebook.authorize(this, new String[]{"publish_stream"}, new DialogListener(){

				@Override
				public void onComplete(Bundle values) {
					values.getString(Facebook.TOKEN);
					//control comes here if the login was successful
//					Facebook.TOKEN is the key by which the value of access token is stored in the Bundle called 'values'
					Log.d("COMPLETE","AUTH COMPLETE. VALUES: "+values.size());
					Log.d("AUTH TOKEN","== "+values.getString(Facebook.TOKEN));
					fbtokk = Facebook.TOKEN;
					startSocWid();
					}

				@Override
				public void onFacebookError(FacebookError e) {
					Log.d("FACEBOOK ERROR","FB ERROR. MSG: "+e.getMessage()+", CAUSE: "+e.getCause());
				}

				@Override
				public void onError(DialogError e) {
					Log.e("ERROR","AUTH ERROR. MSG: "+e.getMessage()+", CAUSE: "+e.getCause());
				}

				@Override
				public void onCancel() {
					Log.d("CANCELLED","AUTH CANCELLED");
				}
			});
			//startSocWid();
		}
		
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult","onActivityResult");
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	public void startSocWid()
	{
		Intent i = new Intent(this, SocWidActivity.class);
		startActivity(i);
		i.putExtra(SocWidActivity.FACEBOOK_TOKEN2, fbtokk);
		//i.putExtra(SocWidActivity.facebook, facebook);
	}

}

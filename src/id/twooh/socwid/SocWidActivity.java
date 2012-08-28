package id.twooh.socwid;

import java.io.IOException;
import java.net.MalformedURLException;

import id.facebook.android.DialogError;
import id.facebook.android.Facebook;
import id.facebook.android.Facebook.DialogListener;
import id.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class SocWidActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	//Social Withdrawal
	static String FACEBOOK_TOKEN;
	static String FACEBOOK_TOKEN2;
	static Facebook facebook = new Facebook("283539765022300");
	String status;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View update = findViewById(R.id.update_button);
        update.setOnClickListener(this);
        View text = findViewById(R.id.text_status);
        text.setOnClickListener(this);
       
        facebook.authorize(this, new String[]{"publish_stream"}, new DialogListener(){

			@Override
			public void onComplete(Bundle values) {
				values.getString(Facebook.TOKEN);
				//control comes here if the login was successful
//				Facebook.TOKEN is the key by which the value of access token is stored in the Bundle called 'values'
				Log.d("COMPLETE","AUTH COMPLETE. VALUES: "+values.size());
				Log.d("AUTH TOKEN","== "+values.getString(Facebook.TOKEN));
				FACEBOOK_TOKEN = values.getString(Facebook.TOKEN);
				//updateStatus(values.getString(Facebook.TOKEN),status);
				TextView tv1 = (TextView) findViewById(R.id.text_fbt);
				tv1.setText(FACEBOOK_TOKEN);
				TextView tv2 = (TextView) findViewById(R.id.text_fbt2);
				tv2.setText("HEYHEYHEY");
			}

			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
        });
        //FACEBOOK_TOKEN2 = new Bundle().getString(Facebook.TOKEN);
        
    }
    
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.text_status :
				EditText ed=(EditText) findViewById(R.id.status);
				if(ed.getText().toString().equals("Apa yang anda pikirkan ?"))
				{
					ed.setText("");
				}
				break;
			case R.id.update_button :
				EditText edt=(EditText) findViewById(R.id.status);
				status = edt.getText().toString();
				updateStatus(FACEBOOK_TOKEN, status);
				break;
									
		}
		
	}
	
    //updating Status
    public void updateStatus(String accessToken, String status){
    	try {
			Bundle bundle = new Bundle();
			bundle.putString("message", status); //'message' tells facebook that you're updating your status
			bundle.putString(Facebook.TOKEN,accessToken);
			//tells facebook that you're performing this action on the authenticated users wall, thus 
//			it becomes an update. POST tells that the method being used is POST
			String response = facebook.request("me/feed",bundle,"POST");
			Log.d("UPDATE RESPONSE",""+response);
		} catch (MalformedURLException e) {
			Log.e("MALFORMED URL",""+e.getMessage());
		} catch (IOException e) {
			Log.e("IOEX",""+e.getMessage());
		}
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult","onActivityResult");
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
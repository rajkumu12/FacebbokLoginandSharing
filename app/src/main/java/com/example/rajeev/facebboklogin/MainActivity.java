package com.example.rajeev.facebboklogin;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    ImageView profileImageView;
    TextView profileInfoTextView;
    FacebookCallback<LoginResult> facebookCallback;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        profileImageView = (ImageView) findViewById(R.id.profile_image);
        profileInfoTextView = (TextView) findViewById(R.id.profile_info_textview);
        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "FacebookCallback was Successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "FacebookCallback was Successful");
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "FacebookCallback Cancelled", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "FacebookCallback Cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "FacebookCallback had Errors with n"+e, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "FacebookCallback had Errors with n" + e);
            }
        };
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager,facebookCallback);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (newProfile == null) {
                    profileImageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                    profileInfoTextView.setText("");
                }else{
                    setUpImageAndInfo(newProfile);
                }
            }
        };
        profileTracker.startTracking();
//final ShareDialog shareDialog=new ShareDialog(MainActivity.this);
        //share.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
               // ShareLinkContent linkContents= new ShareLinkContent.Builder()
                  //  .setQuote("TechSir")
                   // .setContentUrl(Uri.parse("https://trendspot360.blogspot.com/2018/05/android-app-not-running-on-other.html")).build();
                //if (ShareDialog.canShow(ShareLinkContent.class)){
               // shareDialog.show(linkContents);
          //  }
       // }
      //  });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile userProfile = Profile.getCurrentProfile();
        if (userProfile != null){
            setUpImageAndInfo(userProfile);
        }else{
            Toast.makeText(this, "Profile is Null", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Profile is Null");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void setUpImageAndInfo(Profile userProfile) {
        final String userInfo = "<u>First Name:</u> " + userProfile.getFirstName() +
                "<br/><u>Last Name:</u> " + userProfile.getLastName() +
                "<br/><u>User Id:</u> " + userProfile.getId() +
                "<br/><u>Profile Link:</u> " + userProfile.getLinkUri().toString();
        profileInfoTextView.setText(Html.fromHtml(userInfo));
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userProfile.getId().toString() + "/picture?type=large")
                .into(profileImageView);
    }
}
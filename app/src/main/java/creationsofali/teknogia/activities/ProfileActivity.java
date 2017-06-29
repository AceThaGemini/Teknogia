package creationsofali.teknogia.activities;

import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import creationsofali.teknogia.R;
import creationsofali.teknogia.appfonts.MyCustomAppFont;
import creationsofali.teknogia.datamodels.Muhuni;
import creationsofali.teknogia.helpers.EmailHelper;
import creationsofali.teknogia.helpers.PermissionsHelper;
import creationsofali.teknogia.helpers.PhoneCallHelper;
import creationsofali.teknogia.helpers.TwitterHelper;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textBio, textTitle, textShortDesc;
    Animation animationShow, animationHide;
    CollapsingToolbarLayout collapsingToolbar;
    FloatingActionButton fab;
    ImageView imageDp, iconEmail, iconTwitter, iconPhone, iconInstagram;
    LinearLayout layoutBio;

    Muhuni muhuni;

    private final String TAG = ProfileActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // api level 21 or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setSharedElementEnterTransition(
                    TransitionInflater.from(this).inflateTransition(R.transition.trans_shared_element));

        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //  fab = (FloatingActionButton) findViewById(R.id.fab);
        //  collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        imageDp = (ImageView) findViewById(R.id.imageDp);
        animationShow = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.anim_fab_show);
        // animationHide = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.anim_fab_hide);

        layoutBio = (LinearLayout) findViewById(R.id.layoutBio);
        textBio = (TextView) findViewById(R.id.textBio);
        textTitle = (TextView) findViewById(R.id.textTitle);
        iconEmail = (ImageView) findViewById(R.id.iconEmail);
        iconPhone = (ImageView) findViewById(R.id.iconPhone);
        iconTwitter = (ImageView) findViewById(R.id.iconTwitter);
        iconInstagram = (ImageView) findViewById(R.id.iconInstagram);

        String gsonProfile = getIntent().getStringExtra("profile");

        if (gsonProfile != null)
            muhuni = new Gson().fromJson(gsonProfile, Muhuni.class);
        else
            muhuni = new Muhuni();


        // display photo
        imageDp.setImageResource(muhuni.getImageDp());

        try {
            // if collapsingToolbar != null
            collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
            Typeface droidSansFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/droid_sans-regular.ttf");
            collapsingToolbar.setExpandedTitleTypeface(droidSansFont);
            collapsingToolbar.setCollapsedTitleTypeface(droidSansFont);

            collapsingToolbar.setTitle(muhuni.getName());
            textShortDesc = (TextView) findViewById(R.id.textShortDesc);
            textShortDesc.setText(muhuni.getShortDesc());
            Log.d(TAG, "Short desc set to collapsingToolbar");

        } catch (NullPointerException e) {
            // if no collapsingToolbar
            if (actionBar != null) {
                actionBar.setTitle(muhuni.getName());
                actionBar.setSubtitle(muhuni.getShortDesc());
            }

            Log.d(TAG, "Short desc set to actionBar subtitle");
        }

        textBio.setText(muhuni.getBio());
        textTitle.setText(muhuni.getTitle());

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // do magic
//            }
//        });

        iconEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = muhuni.getEmail();
                if (email != null)
                    EmailHelper.launchComposeEmail(email, ProfileActivity.this);
                else
                    Toast.makeText(ProfileActivity.this, "No email defined yet.", Toast.LENGTH_SHORT).show();
            }
        });

        iconTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String screenName = muhuni.getTwitterName();
                if (screenName != null)
                    TwitterHelper.launchTwitter(screenName, ProfileActivity.this);
                else
                    Toast.makeText(ProfileActivity.this, "No account defined yet.", Toast.LENGTH_SHORT).show();

            }
        });

        iconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = muhuni.getPhone();
                if (phone != null) {
                    // check permission
                    if (PermissionsHelper.hasCallPermission(ProfileActivity.this))
                        PhoneCallHelper.callPhone(ProfileActivity.this, phone);
                    else
                        // request permission
                        PermissionsHelper.requestCallPermission(ProfileActivity.this);

                } else
                    // no phone
                    Toast.makeText(ProfileActivity.this, "New phone who dis?", Toast.LENGTH_SHORT).show();
            }
        });

        iconInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "No account defined yet.", Toast.LENGTH_SHORT).show();
            }
        });

        // app fonts
        View rootView = findViewById(android.R.id.content);
        new MyCustomAppFont(getApplicationContext(), rootView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // fab.startAnimation(animationShow);
    }


    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition slideAnimation = TransitionInflater.from(this).inflateTransition(R.transition.trans_slide_enter);
            slideAnimation.addTarget(layoutBio);
            getWindow().setEnterTransition(slideAnimation);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

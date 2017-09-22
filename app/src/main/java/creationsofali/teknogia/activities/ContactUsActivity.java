package creationsofali.teknogia.activities;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import creationsofali.teknogia.adapter.GridAdapter;
import creationsofali.teknogia.appfonts.MyCustomAppFont;
import creationsofali.teknogia.R;
import creationsofali.teknogia.datamodels.Muhuni;
import creationsofali.teknogia.datamodels.RequestCode;
import creationsofali.teknogia.helpers.GridRecyclerView;
import creationsofali.teknogia.helpers.TopBottomSpaceDecoration;

public class ContactUsActivity extends AppCompatActivity {

    Toolbar toolbar;
    GridRecyclerView recyclerView;
    GridAdapter adapter;
    RecyclerView.LayoutManager gridLayoutManager;

    int setCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // api level 21 or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setSharedElementExitTransition(
                    TransitionInflater.from(this).inflateTransition(R.transition.trans_shared_element));

        setContentView(R.layout.activity_contact_us);

        toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        // layout manager
        gridLayoutManager = new GridLayoutManager(ContactUsActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // item decoration
        int bottomSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f,
                getResources().getDisplayMetrics());

        TopBottomSpaceDecoration decoration = new TopBottomSpaceDecoration(0, bottomSpace);
        recyclerView.addItemDecoration(decoration);

        // adapter
        adapter = new GridAdapter(ContactUsActivity.this, getTheCrew());
        // devices running api level older than 21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            recyclerView.setAdapter(adapter);


        View rootView = findViewById(android.R.id.content);
        new MyCustomAppFont(getApplicationContext(), rootView);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            switch (requestCode) {
                case RequestCode.RC_CALL_PHONE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(ContactUsActivity.this, "Call permission granted.", Toast.LENGTH_SHORT).show();
                    // PhoneCallHelper.callPhone(ContactUsActivity.this, "");
            }
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (setCount == 0) {
            recyclerView.setAdapter(adapter);
            recyclerView.scheduleLayoutAnimation();
            setCount++;
        }
    }

    private ArrayList<Muhuni> getTheCrew() {

        ArrayList<Muhuni> crewList = new ArrayList<>();
        Muhuni muhuni;

        muhuni = new Muhuni();
        muhuni.setName("Malamsha, Kevin");
        muhuni.setNickName("Bobo");
        muhuni.setTitle("Founder & CEO");
        muhuni.setTwitterName("Bobolysis");
        muhuni.setEmail("kevinshibobo@gmail.com");
        muhuni.setShortDesc("The Designer");
        muhuni.setImageDp(R.drawable.image_boondocks_huey);
        muhuni.setBio("Doing what I do best.. Meaw meaw!!");
        muhuni.setPhone("+255 753 153 327");
        // add to list
        crewList.add(muhuni);

        muhuni = new Muhuni();
        muhuni.setName("Suleiman, Ali");
        muhuni.setNickName("Ace");
        muhuni.setTitle("CTO");
        muhuni.setEmail("allysuley@gmail.com");
        muhuni.setTwitterName("AceThaGemini");
        muhuni.setShortDesc("The Brains");
        muhuni.setBio("Currently coding my way to death.");
        muhuni.setPhone("+255 689 361 147");
        muhuni.setImageDp(R.drawable.image_boondocks_huey);
        // add to list
        crewList.add(muhuni);

        muhuni = new Muhuni();
        muhuni.setName("Saitoti, Saitoti");
        muhuni.setNickName("Sai");
        muhuni.setTitle("N");
        muhuni.setShortDesc("The Visionary");
        muhuni.setBio("");
        muhuni.setImageDp(R.drawable.image_boondocks_huey);
        muhuni.setPhone("+255 657 133 068");
        // add to list
        crewList.add(muhuni);

        muhuni = new Muhuni();
        muhuni.setName("Sway, Calvin");
        muhuni.setNickName("Shabbah");
        muhuni.setTitle("VC");
        muhuni.setShortDesc("The Boss");
        muhuni.setImageDp(R.drawable.image_boondocks_huey);
        muhuni.setBio("Gaming all day everyday, but we gonna get that money anyway.");
        // add to list
        crewList.add(muhuni);

        muhuni = new Muhuni();
        muhuni.setName("Mosha, Evans");
        muhuni.setNickName("Kibuyu");
        muhuni.setTitle("DJ");
        muhuni.setShortDesc("The Finesse");
        muhuni.setImageDp(R.drawable.image_boondocks_huey);
        muhuni.setBio("Let's get turnt! Life is way too short.");
        // add to list
        crewList.add(muhuni);


        return crewList;

    }
}

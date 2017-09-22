package creationsofali.teknogia.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import creationsofali.teknogia.helpers.AppWebViewClient;
import creationsofali.teknogia.helpers.DrawerTypefaceHelper;
import creationsofali.teknogia.appfonts.MyCustomAppFont;
import creationsofali.teknogia.helpers.MyMenuBuilderHelper;
import creationsofali.teknogia.helpers.NetworkHelper;
import creationsofali.teknogia.helpers.PackageInfoHelper;
import creationsofali.teknogia.R;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationDrawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    SmoothProgressBar progressBar;
    LinearLayout layoutInstruction;
    ImageView iconInstruction;
    TextView textInstruction;
    Snackbar snackbar;

    // FloatingActionButton fabRefresh;
    Animation animationShow, animationHide;
    Handler handler;

    boolean doubleBackToExitPressedOnce = false, isAnotherItemChecked = false, hasErrorOccurred = false;
    ArrayList<Integer> checkedItemList = new ArrayList<>();
    private static final String TAG = "MainActivity";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getString(R.string.app_name));
        }

        webView = findViewById(R.id.webView);
        layoutInstruction = findViewById(R.id.layoutInstruction);
        layoutInstruction.setVisibility(View.GONE);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationDrawer = findViewById(R.id.navigationDrawer);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setInterpolator(new DecelerateInterpolator(1f));
        textInstruction = findViewById(R.id.textInstruction);
        iconInstruction = findViewById(R.id.iconInstruction);
        // fabRefresh = (FloatingActionButton) findViewById(R.id.fabRefresh);
        // fabRefresh.hide();
        // fabRefresh.setEnabled(false);
        // swipeRefreshLayout.setColorSchemeResources(getResources().getIntArray(R.array.progressBarColors));
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progressBarColors));

        animationShow = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_fab_show);
        animationHide = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_fab_hide);

        handler = new Handler();

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new AppWebViewClient(
                MainActivity.this, progressBar,
                swipeRefreshLayout, hasErrorOccurred));

        // check if the app launch from notification and show new post
        String newPostUrl = getIntent().getStringExtra("newPostUrl");
        Log.d(TAG, "onCreate:newPostUrl = " + newPostUrl);

        if (newPostUrl != null)
            loadUrlToWebView(newPostUrl);
        else
            loadUrlToWebView(null);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh
                swipeRefreshLayout.setRefreshing(true);
                // close snackbar
                if (snackbar != null && snackbar.isShown())
                    snackbar.dismiss();

                // load url
                loadUrlToWebView(webView.getUrl());
            }
        });

        checkedItemList.add(R.id.navHome);
        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navHome:
                        webView.clearHistory();
                        loadUrlToWebView("https://www.teknogia.com");
                        break;

                    case R.id.navCamera:
                        loadUrlToWebView("https://www.teknogia.com/kamera");
                        break;

                    case R.id.navComputer:
                        loadUrlToWebView("https://www.teknogia.com/kompyuta");
                        break;

                    case R.id.navPhone:
                        loadUrlToWebView("https://www.teknogia.com/simu");
                        break;

                    case R.id.navSocialNet:
                        loadUrlToWebView("https://www.teknogia.com/mitandao-ya-kijamii");
                        break;

                    case R.id.navCulture:
                        loadUrlToWebView("https://www.teknogia.com/utamaduni");
                        break;

                    case R.id.navContactUs:
                        startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                        break;

                    case R.id.navAbout:
                        showAboutDialog();
                        break;
                }
                // close drawer
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);

                // add to checkedList
                if (item.getItemId() != R.id.navHome && item.getItemId() != checkedItemList.get((checkedItemList.size() - 1))) {
                    isAnotherItemChecked = true;
                    checkedItemList.add(item.getItemId());
                }

                return true;
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.drawer_opened,
                R.string.drawer_closed);
        drawerLayout.setDrawerListener(drawerToggle);

        // start bg task to set custom fonts on drawer items
        new DrawerTypefaceHelper(MainActivity.this).executeTask(navigationDrawer.getMenu());

//        fabRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (NetworkHelper.isOnline(MainActivity.this))
//                    webView.reload();
//                else
//                    showSnackbar("Upo offline, tafadhali washa intaneti na ujaribu tena.");
//
//            }
//        });

        // app custom fonts
        View rootView = findViewById(android.R.id.content);
        new MyCustomAppFont(getApplicationContext(), rootView);

        // start bg task to set custom fonts on drawer items
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // delay for 1 sec to run the bg task after everything loaded
                new DrawerTypefaceHelper(getApplicationContext()).executeTask(navigationDrawer.getMenu());
            }
        }, 1000);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        MyMenuBuilderHelper.setOptionalIconsVisible(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // MyMenuBuilderHelper.setOptionalIconsVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionOpenInBrowser:
                // open link in browser
                String webUrl = webView.getUrl();
                Uri linkUri;

                if (webUrl != null)
                    linkUri = Uri.parse(webUrl);
                else
                    linkUri = Uri.parse("https://www.teknogia.com");

                Intent openInBrowserIntent = new Intent(Intent.ACTION_VIEW, linkUri);
                startActivity(openInBrowserIntent);
                return true;

            case R.id.optionShareLink:
                // share link
                String link = webView.getUrl();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                //noinspection deprecation
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                // text
                if (link != null)
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Cheki hii mtu wangu: \n" + link);
                else
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.teknogia.com");

                // subject
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Cheki hii mtu wangu: \n" + webView.getTitle());
                // share
                startActivity(Intent.createChooser(shareIntent, "Tuma kwa..."));
                return true;

            default:
                return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        // close drawer if open onBackPressed
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // double back to exit
            getSupportFragmentManager().popBackStack();

        } else if (webView.canGoBack()) {
            // go back
            webView.goBack();

            // update drawer for checked item
            if (isAnotherItemChecked && checkedItemList.size() > 1) {
                int previousCheckedItem = checkedItemList.size() - 2;
                navigationDrawer.setCheckedItem(checkedItemList.get(previousCheckedItem));
                checkedItemList.remove(previousCheckedItem + 1);
            }

        } else if (!doubleBackToExitPressedOnce) {

            this.doubleBackToExitPressedOnce = true;
            // now ask for the second backPress to exit
            Toast.makeText(
                    getApplicationContext(),
                    "Bofya tena kutoka.",
                    Toast.LENGTH_SHORT).show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // reset values after time delay
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {
            // default
            super.onBackPressed();
        }
    }

    public void showConfirmationSnackbar(final String url) {
        Snackbar.make(findViewById(R.id.coordinatorMain),
                "You refused to leave Teknogia.",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Badili", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLeavingTeknogiaDialog(url);
                    }
                })
                .setDuration(8000)
                .show();
    }

    public void showSnackbarAction(String message) {
        snackbar = Snackbar.make(findViewById(R.id.coordinatorMain),
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.color_primary_green))
                .setAction("Jaribu tena", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadUrlToWebView(webView.getUrl());
                    }
                })
                .setDuration(15000);

        snackbar.show();
    }

    public void showAboutDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_about, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        TextView textVersion = dialogView.findViewById(R.id.textVersion);
        textVersion.setText(PackageInfoHelper.getVersionName(MainActivity.this));

        TextView textRights = dialogView.findViewById(R.id.textRights);
        String unicodeCopyRight = "\u00A9"; // or (char) 169 = 10101001 = 0x00A9
        String unicodeTradeMark = "\u2122"; // or (char) 8482 = 0x2122
        // (c) 2017 TINTech Apps tm x Teknogia tm
        //      All Rights Reserved.
        int year = Calendar.getInstance().get(Calendar.YEAR);
        textRights.setText(unicodeCopyRight);
        textRights.append(year + " TINTech" + unicodeTradeMark + " x Teknogia" + unicodeTradeMark);
        textRights.append("\n" + "All Rights Reserved.");

//
//        dialogView.findViewById(R.id.iconTwitter).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TwitterHelper.launchTwitter("AceThaGemini", getApplicationContext());
//            }
//        });
//
//        dialogView.findViewById(R.id.iconEmail).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EmailHelper.launchComposeEmail("allysuley@gmail.com", getApplicationContext());
//            }
//        });

        dialogView.findViewById(R.id.textClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  onPositiveButtonClick
                dialog.dismiss();
            }
        });
    }

    private void loadUrlToWebView(String url) {

        if (NetworkHelper.isOnline(MainActivity.this)) {
            // online
            // show progress
            if (progressBar.getVisibility() != View.VISIBLE)
                progressBar.setVisibility(View.VISIBLE);

            progressBar.startAnimation(animationShow);

            //  if (fabRefresh.isEnabled()) {
            //      fabRefresh.startAnimation(animationHide);
            //      fabRefresh.setEnabled(false);
            //  }

            if (url == null)
                webView.loadUrl("https://www.teknogia.com");
            else
                webView.loadUrl(url);

            // remove instruction layout
            if (layoutInstruction.getVisibility() == View.VISIBLE) {
                layoutInstruction.startAnimation(animationHide);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutInstruction.setVisibility(View.GONE);
                    }
                }, 500);
            }

            // show webView
            if (webView.getVisibility() != View.VISIBLE && !hasErrorOccurred)
                webView.setVisibility(View.VISIBLE);

        } else {
            // offline
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

            showSnackbarAction(getString(R.string.device_offline));

            // if (fabRefresh.isEnabled()) {
            //     fabRefresh.startAnimation(animationHide);
            //     fabRefresh.setEnabled(false);
            //  }

            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.startAnimation(animationHide);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 500);
            }

            // show instruction
            onFailedShowInstruction(true);
        }

    }

    public void onFailedShowInstruction(boolean isOffline) {
        if (webView.getVisibility() == View.VISIBLE)
            webView.setVisibility(View.GONE);

        if (layoutInstruction.getVisibility() != View.VISIBLE) {
            layoutInstruction.setVisibility(View.VISIBLE);
            layoutInstruction.setAnimation(animationShow);
        }

        if (isOffline) {
            textInstruction.setText(R.string.turn_on_internet);
            iconInstruction.setImageResource(R.drawable.ic_signal_cellular_connected_no_internet);
        } else {
            textInstruction.setText(R.string.swipe_down_to_refresh);
            iconInstruction.setImageResource(R.drawable.ic_gesture_swipe_down);
            hasErrorOccurred = true;
        }
    }


    public void onLeavingTeknogiaDialog(final String url) {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_leaving_teknogia, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        final SharedPreferences.Editor sharedPrefEditor = getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE).edit();


        final CheckBox checkboxNeverAskAgain = dialogView.findViewById(R.id.checkboxNeverAskAgain);

        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cancel
                if (checkboxNeverAskAgain.isChecked()) {
                    sharedPrefEditor.putBoolean("isNeverAskAgainChecked", true);
                    sharedPrefEditor.putBoolean("isGoToWebOk", false);
                } else
                    sharedPrefEditor.putBoolean("isNeverAskAgainChecked", false);


                sharedPrefEditor.apply();
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  onPositiveButtonClick
                // start outsideWebIntent
                if (checkboxNeverAskAgain.isChecked()) {
                    sharedPrefEditor.putBoolean("isNeverAskAgainChecked", true);
                    sharedPrefEditor.putBoolean("isGoToWebOk", true);
                } else
                    sharedPrefEditor.putBoolean("isNeverAskAgainChecked", false);


                sharedPrefEditor.apply();
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, OutsideWebActivity.class).putExtra("url", url));
            }
        });
    }

    public void updateErrorCheck(boolean hasErrorOccurred) {
        this.hasErrorOccurred = hasErrorOccurred;
    }

}

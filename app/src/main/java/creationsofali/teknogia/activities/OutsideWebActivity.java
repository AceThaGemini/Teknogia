package creationsofali.teknogia.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import creationsofali.teknogia.appfonts.MyCustomAppFont;
import creationsofali.teknogia.helpers.NetworkHelper;
import creationsofali.teknogia.R;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class OutsideWebActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    SmoothProgressBar progressBar;
    LinearLayout layoutInstruction;
    ImageView iconInstruction;
    TextView textInstruction, textToolbarTitle, textToolbarSubtitle;

    Animation animationShow, animationHide;

    private static final String TAG = "OutsideWebActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_web);

        toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
            actionBar.setSubtitle("");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }


        webView = findViewById(R.id.webView);
        layoutInstruction = findViewById(R.id.layoutInstruction);
        layoutInstruction.setVisibility(View.GONE);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_blue);
        progressBar = findViewById(R.id.progressBar);
        textToolbarTitle = findViewById(R.id.textToolbarTitle);
        textToolbarSubtitle = findViewById(R.id.textToolbarSubtitle);

        animationShow = AnimationUtils.loadAnimation(OutsideWebActivity.this, R.anim.anim_fab_show);
        animationHide = AnimationUtils.loadAnimation(OutsideWebActivity.this, R.anim.anim_fab_hide);

        String url = getIntent().getStringExtra("url");
        String host = Uri.parse(url).getHost();
        Log.d(TAG, "onCreate:host = " + host + ", url = " + url);

        textToolbarTitle.setText(". . . .");
        textToolbarSubtitle.setText("");

        if (!host.contains("www."))
            textToolbarSubtitle.append("www.");

        // as the last part of the host
        textToolbarSubtitle.append(host);

        // start loading
        loadUrlToWebView(url);
        // update toolbar
        if (actionBar != null) {
            if (url.contains("https"))
                actionBar.setIcon(R.drawable.ic_lock);
            else
                actionBar.setIcon(R.drawable.ic_lock_open_outline);
        }


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressBar();

                textToolbarTitle.setText(view.getTitle());

                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                hideProgressBar();

                // show error
                onFailedShowInstruction(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh
                if (!swipeRefreshLayout.canChildScrollUp()) {
                    swipeRefreshLayout.setRefreshing(true);
                    loadUrlToWebView(webView.getUrl());
                }
            }
        });

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


    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.startAnimation(animationHide);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.isShown())
                        progressBar.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    private void showProgressBar() {
        if (progressBar.getVisibility() != View.VISIBLE)
            progressBar.setVisibility(View.VISIBLE);

        progressBar.startAnimation(animationShow);
    }

    private void loadUrlToWebView(String url) {

        if (NetworkHelper.isOnline(OutsideWebActivity.this)) {
            // online
            // show progress
            showProgressBar();
            // load
            webView.loadUrl(url);

            // remove instruction layout
            if (layoutInstruction.getVisibility() == View.VISIBLE) {
                layoutInstruction.startAnimation(animationHide);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutInstruction.setVisibility(View.GONE);
                    }
                }, 500);
            }

            // show webView
            if (webView.getVisibility() != View.VISIBLE)
                webView.setVisibility(View.VISIBLE);

        } else {
            // offline
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.startAnimation(animationHide);

                new Handler().postDelayed(new Runnable() {
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


    private void onFailedShowInstruction(boolean isOffline) {
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
            // hasErrorOccurred = true;
        }
    }
}

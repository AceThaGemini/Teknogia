package creationsofali.teknogia.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import creationsofali.teknogia.R;
import creationsofali.teknogia.activities.MainActivity;
import creationsofali.teknogia.activities.OutsideWebActivity;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by ali on 6/11/17.
 */

public class AppWebViewClient extends WebViewClient {

    private SmoothProgressBar progressBar;
    private Animation animationShow, animationHide;
    // private FloatingActionButton fabRefresh;
    private MainActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean hasErrorOccurred;
    private Handler handler;
    private static final String TAG = "AppWebViewClient";

    public AppWebViewClient(Context context, SmoothProgressBar progressBar,
                            SwipeRefreshLayout swipeRefreshLayout, boolean hasErrorOccurred) {
        this.progressBar = progressBar;
        this.activity = (MainActivity) context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.animationShow = AnimationUtils.loadAnimation(context, R.anim.anim_fab_show);
        this.animationHide = AnimationUtils.loadAnimation(context, R.anim.anim_fab_hide);
        this.hasErrorOccurred = hasErrorOccurred;
        this.handler = new Handler();
        // this.fabRefresh = fabRefresh;
        // fabRefresh.startAnimation(animationHide);
    }

//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        // show progress
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.startAnimation(animationShow);
//
//        fabRefresh.setAnimation(animationHide);
//        fabRefresh.setEnabled(false);
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            // For Android L and above
//            if (request.getUrl().getHost().endsWith("teknogia.com"))
//                return false;
//
//            // for all other links in that's not your domain
//            // ask for confirmation leaving the app
//            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
//            view.getContext().startActivity(intent);
//
//            return true;
//        }
//
//        return super.shouldOverrideUrlLoading(view, request);
//    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //  if (fabRefresh.isEnabled()) {
        //      fabRefresh.setAnimation(animationHide);
        //       fabRefresh.setEnabled(false);
        //  }
        // if (NetworkHelper.isOnline(activity.getApplicationContext())) {
        // For Non-Android L and above, i.e. SDK < 21
        if (Uri.parse(url).getHost().endsWith("teknogia.com")) {
            // show progress
            showProgressBar();
            // reset checker on start loading
            hasErrorOccurred = false;
            activity.updateErrorCheck(false);

            view.loadUrl(url);
            Log.d(TAG, "shouldOverrideUrlLoading#loadUrl: " + url);
            return true;
        }
        // } else {
        //  activity.showSnackbarAction(activity.getResources().getString(R.string.device_offline));
        // return true;
        //  }

        // for all other links in that's not your domain
        // ask for confirmation leaving the app
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(
                activity.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);

        boolean isCheckBoxChecked = sharedPreferences.getBoolean("isNeverAskAgainChecked", false);
        boolean isGoToWebOk = sharedPreferences.getBoolean("isGoToWebOk", false);

        if (!isCheckBoxChecked)
            // ask for confirmation
            activity.onLeavingTeknogiaDialog(url);

        else if (isGoToWebOk)
            // go to web without asking
            activity.getApplicationContext().startActivity(
                    new Intent(activity, OutsideWebActivity.class).putExtra("url", url));

        else
            // user cancelled and checked never ask again
            //complete denial or some shit
            activity.showConfirmationSnackbar(url);

        hideProgressBar();
        Log.d(TAG, "shouldOverrideUrlLoading#intent: " + url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // hide progressBar
        hideProgressBar();

        init();

        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        // show
        if (!hasErrorOccurred && view.getVisibility() != View.VISIBLE)
            // no error
            view.setVisibility(View.VISIBLE);

        Log.d(TAG, "onPageFinished: " + url);

        // if (!fabRefresh.isEnabled()) {
        //     fabRefresh.setAnimation(animationShow);
        //     fabRefresh.setEnabled(true);
        //  }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);

        Log.d(TAG, "onReceivedError: code =  " + errorCode +
                ", desc = " + description + ", url " + failingUrl);
        // view.setVisibility(View.GONE);
        hasErrorOccurred = true;
        activity.updateErrorCheck(true);
        activity.onFailedShowInstruction(false);
        activity.showSnackbarAction(activity.getApplicationContext()
                .getString(R.string.sorry_failed_to_load));

        // if (fabRefresh.isEnabled()) {
        //     fabRefresh.startAnimation(animationHide);
        //     fabRefresh.setEnabled(false);
        //  }

        hideProgressBar();
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    private void init() {
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.startAnimation(animationHide);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.isShown())
                        progressBar.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    private void showProgressBar() {
        if (progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(animationShow);
        }
    }
}

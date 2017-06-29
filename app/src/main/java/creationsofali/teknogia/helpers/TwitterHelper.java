package creationsofali.teknogia.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Desc: The class for starting Twitter intent from inside the app.
 *       Calling the method #launchTwitter will open the Twitter app,
 *       if installed, or Web browser, if app not installed.
 * Author: Ali
 * Date 5th April 17.
 */

public class TwitterHelper {

    public static void launchTwitter(String username, Context context) {
        Intent twitterIntent = null;
        try {
            // launch app if installed
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + username));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // no app, open in browser
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + username));
        } finally {
            // go to twitter account page
            if (twitterIntent != null) {
                twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(twitterIntent);
            }
        }
    }
}

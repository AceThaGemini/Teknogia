package creationsofali.teknogia.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ali on 6/16/17.
 */

public class PhoneCallHelper {

    public static void callPhone(Context context, String phone) {
        context.startActivity(new Intent(
                "android.intent.action.CALL",
                Uri.parse("tel:" + phone)));
    }
}

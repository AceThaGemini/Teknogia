package creationsofali.teknogia.helpers;

import android.support.v7.widget.PopupMenu;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ali on 5/26/17.
 */

public class MyPopupMenuHelper {

    private static final String TAG = "MyPopupMenuHelper";

    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);

        } catch (Exception e) {
            // NoSuchFieldException | IllegalAccessException |
            // ClassNotFoundException | NoSuchMethodException |
            // InvocationTargetException
            Log.d(TAG, "e = " + e.toString() + ", m = " + e.getMessage());
        }
    }
}

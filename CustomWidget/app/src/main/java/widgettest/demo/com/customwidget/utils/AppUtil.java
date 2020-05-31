package widgettest.demo.com.customwidget.utils;

import android.content.Context;

public class AppUtil {

    public static float cmToPxForWidth(Context context, float cm) {
        float xDpi = context.getResources().getDisplayMetrics().xdpi;
        float res = (int) (cm * xDpi / 2.54f);
        return res;
    }

}

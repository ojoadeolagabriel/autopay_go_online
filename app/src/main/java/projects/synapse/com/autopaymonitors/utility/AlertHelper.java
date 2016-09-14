package projects.synapse.com.autopaymonitors.utility;

import android.app.AlertDialog;
import android.content.Context;

import java.util.Calendar;

import projects.synapse.com.autopaymonitors.R;

/**
 * Created by AdeolaOjo on 9/12/2016.
 */
public class AlertHelper {

    /***
     * Warning alert dialog
     * @param msg
     * @param header
     * @param ctx
     */
    public static void Warning(String msg, String header, Context ctx) {
        AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        dialog.setMessage(msg);
        dialog.setIcon(R.drawable.warnng);
        dialog.setTitle(header);

        dialog.getWindow().setDimAmount(0.8f);
        dialog.show();
    }

    public static String greeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return "Morning, %s";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return "Afternoon, %s";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            return "Evening, %s";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            return "Welcome, %s";
        }
        return "Welcome, %s";
    }
}

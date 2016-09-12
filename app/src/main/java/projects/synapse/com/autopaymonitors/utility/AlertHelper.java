package projects.synapse.com.autopaymonitors.utility;

import android.app.AlertDialog;
import android.content.Context;

import projects.synapse.com.autopaymonitors.R;

/**
 * Created by AdeolaOjo on 9/12/2016.
 */
public class AlertHelper {

    public static void Warning(String msg, String header, Context ctx){
        AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        dialog.setMessage(msg);
        dialog.setIcon(R.drawable.warnng);
        dialog.setTitle(header);
        dialog.show();
    }
}

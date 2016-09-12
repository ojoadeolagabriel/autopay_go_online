package projects.synapse.com.autopaymonitors.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AdeolaOjo on 9/12/2016.
 */
public class NetworkHelper {

    /***
     * Check if network connection is available.
     * @param ctx
     * @return
     */
    public static boolean isNetworkAvailable(Context ctx){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }catch(Exception e){
            return false;
        }
    }
}

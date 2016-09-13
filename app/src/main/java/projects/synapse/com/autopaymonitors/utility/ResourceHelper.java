package projects.synapse.com.autopaymonitors.utility;

import android.content.Context;

/**
 * Created by AdeolaOjo on 9/13/2016.
 */
public class ResourceHelper {

    public static String getValue(int key, Context ctx){
        return ctx.getResources().getString(key);
    }
}

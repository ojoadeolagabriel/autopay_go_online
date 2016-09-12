package projects.synapse.com.autopaymonitors.utility;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by AdeolaOjo on 9/12/2016.
 */
public class AssetHelper {

    public static Typeface getTypefaceByName(Context c, String name) {
        try {
            AssetManager assets = c.getAssets();
            return Typeface.createFromAsset(assets, name);
        }catch (Exception exc){
            return null;
        }
    }
}

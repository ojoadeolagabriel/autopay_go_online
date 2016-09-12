package projects.synapse.com.autopaymonitors.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.model.AutoPayProcessor;
import projects.synapse.com.autopaymonitors.utility.ImageHelper;
import projects.synapse.com.autopaymonitors.viewholders.AutoPayProcessorViewHolder;

/**
 * Created by AdeolaOjo on 9/10/2016.
 */
public class AutoPayArrayAdapter extends ArrayAdapter<AutoPayProcessor> {

    Context context;
    List<AutoPayProcessor> processors;

    public AutoPayArrayAdapter(Context context, int resource, List<AutoPayProcessor> objects) {
        super(context, resource, objects);
        this.context = context;
        this.processors = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AutoPayProcessorViewHolder viewHolder;
        Typeface plain = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/open-san-light.ttf");
        Typeface semi_bold = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/opensan-semi-bold.ttf");
        AutoPayProcessor processor = processors.get(position);
        int resID = 0;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.autopay_processor_list_item, parent, false);
            viewHolder = new AutoPayProcessorViewHolder();

            viewHolder.tvBankTitle = (TextView) convertView.findViewById(R.id.bankProcessor);
            viewHolder.tvBankDescription = (TextView) convertView.findViewById(R.id.bankProcessorDescription);
            viewHolder.tvBankOnlineStatus = (TextView) convertView.findViewById(R.id.bankProcessStatusDescription);
            viewHolder.imgBankLogo = (ImageView) convertView.findViewById(R.id.icon1);

            //setup ui
            viewHolder.tvBankTitle.setTypeface(semi_bold);
            viewHolder.tvBankDescription.setTypeface(plain);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (AutoPayProcessorViewHolder) convertView.getTag();
        }

        if (processor != null) {
            viewHolder.tvBankTitle.setText(processor.name);
            viewHolder.tvBankDescription.setText(processor.description);

            //msg text
            if (processor.isEnabled) {
                viewHolder.tvBankOnlineStatus.setText("ONLINE");
                viewHolder.tvBankOnlineStatus.setTextColor(Color.parseColor("#000000"));
            } else {
                viewHolder.tvBankOnlineStatus.setText("OFFLINE");
                viewHolder.tvBankOnlineStatus.setTextColor(Color.parseColor("#B22222"));
            }

            try {

                resID = parent.getResources().getIdentifier("bank_" + processor.code, "drawable", parent.getContext().getPackageName());
                if (resID != 0) {
                    viewHolder.imgBankLogo.setImageResource(resID);
                    viewHolder.imgBankLogo.setImageBitmap(ImageHelper.roundCornerImage(BitmapFactory.decodeResource(context.getResources(), resID), 50));
                } else {
                    viewHolder.imgBankLogo.setImageResource(R.drawable.question);
                }
            } catch (Exception ex) {

            }
        }

        return convertView;
    }
}

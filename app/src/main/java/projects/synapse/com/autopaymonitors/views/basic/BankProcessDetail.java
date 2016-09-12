package projects.synapse.com.autopaymonitors.views.basic;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.model.AutoPayProcessor;

/**
 * Created by AdeolaOjo on 9/10/2016.
 */
public class BankProcessDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_detail);
        loadUI();
    }

    private void loadUI() {
        AutoPayProcessor processor = (AutoPayProcessor)getIntent().getSerializableExtra("bankProcessor");
        TextView tvBankProcessor = (TextView)findViewById(R.id.bankProcessor);
        TextView tvBankProcessorDesc = (TextView)findViewById(R.id.bankProcessorDescription);
        ImageView image = (ImageView)findViewById(R.id.icon2);

        tvBankProcessor.setText(processor.name);
        tvBankProcessorDesc.setText(processor.description);

        Typeface plain = Typeface.createFromAsset(getAssets(),  "fonts/open-san-light.ttf");
        Typeface semi_bold = Typeface.createFromAsset(getAssets(),  "fonts/opensan-semi-bold.ttf");
        tvBankProcessor.setTypeface(semi_bold);
        tvBankProcessor.setTextSize(19f);
        tvBankProcessorDesc.setTypeface(plain);

        int resID = getResources().getIdentifier("bank_" + processor.code, "drawable", getPackageName());
        if(resID!=0)
            image.setImageResource(resID);
    }
}

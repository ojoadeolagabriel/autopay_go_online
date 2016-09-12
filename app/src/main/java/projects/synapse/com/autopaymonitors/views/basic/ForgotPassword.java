package projects.synapse.com.autopaymonitors.views.basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.utility.AlertHelper;
import projects.synapse.com.autopaymonitors.utility.AssetHelper;

/**
 * Created by AdeolaOjo on 9/12/2016.
 */
public class ForgotPassword extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_view);
        loadUI();
        loadHandlers();
    }

    private void loadHandlers() {
        final TextView btnCancel = (TextView) findViewById(R.id.btnCancel);
        final TextView btnConfirm = (TextView) findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertHelper.Warning("Feature not yet supported!","Oops! feature not supported!", ForgotPassword.this);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splashViewIntent = new Intent(ForgotPassword.this, LoginView.class);
                startActivity(splashViewIntent);
                finish();
            }
        });
    }

    private void loadUI() {
        final TextView tvLoginTxt = (TextView) findViewById(R.id.tvLoginTxt);
        final TextView tvInstructions = (TextView) findViewById(R.id.tvInstructions);
        final TextView enterUsername = (TextView) findViewById(R.id.enterUsername);

        tvLoginTxt.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvInstructions.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        enterUsername.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
    }
}

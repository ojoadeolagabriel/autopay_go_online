package projects.synapse.com.autopaymonitors.views.basic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.utility.AssetHelper;

/**
 * Created by AdeolaOjo on 9/12/2016.
 */
public class LoginView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module);
        initializeUI();
    }

    private void initializeUI() {
        final TextView tvUsername = (TextView) findViewById(R.id.enterUsername);
        final TextView tvPassword = (TextView) findViewById(R.id.enterPassword);
        TextView tvLoginTxt = (TextView) findViewById(R.id.tvLoginTxt);
        TextView tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        tvUsername.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvForgotPassword.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvPassword.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvLoginTxt.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));

        //events
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvUserMsg = tvUsername.getText().toString();
                String tvPasswordMsg = tvUsername.getText().toString();

                boolean isUsernameInValid = tvUserMsg.equals("");
                boolean isPasswordInValid = tvPasswordMsg.equals("");

                if (isUsernameInValid || isPasswordInValid) {
                    AlertDialog dialog = new AlertDialog.Builder(LoginView.this).create();
                    dialog.setMessage("You left out some login information");
                    dialog.setIcon(R.drawable.error);
                    dialog.setTitle("Oops");

                    dialog.show();
                }
            }
        });
    }
}

package projects.synapse.com.autopaymonitors.views.basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.model.LoginInformation;
import projects.synapse.com.autopaymonitors.utility.AlertHelper;
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
        final CheckBox cbxRemember = (CheckBox) findViewById(R.id.cbxRemember);

        TextView tvLoginTxt = (TextView) findViewById(R.id.tvLoginTxt);
        TextView tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        tvUsername.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvForgotPassword.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvPassword.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        tvLoginTxt.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));
        cbxRemember.setTypeface(AssetHelper.getTypefaceByName(this, "fonts/open-san-light.ttf"));

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splashViewIntent = new Intent(LoginView.this, ForgotPassword.class);
                startActivity(splashViewIntent);
                finish();
            }
        });

        //events
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout loginPnl = (LinearLayout) findViewById(R.id.pnlLogin);
                LinearLayout progressPnl = (LinearLayout) findViewById(R.id.pnlProgress);

                loginPnl.setVisibility(View.GONE);
                progressPnl.setVisibility(View.VISIBLE);

                TextView usernameView = (TextView) findViewById(R.id.enterUsername);
                TextView passwordView = (TextView) findViewById(R.id.enterPassword);

                String usernameResult = usernameView.getText().toString();
                String usernamePassword = passwordView.getText().toString();

                boolean isUsernameInValid = usernameResult.equals("");
                boolean isPasswordInValid = usernamePassword.equals("");

                if (isUsernameInValid || isPasswordInValid) {

                    String extraMsg = "";
                    if(isUsernameInValid)
                        extraMsg += "username";
                    if(isUsernameInValid && isPasswordInValid)
                        extraMsg += " and ";
                    if(isPasswordInValid)
                        extraMsg += "password";

                    AlertHelper.Warning("Kindly check your " + extraMsg, "Oops! Something went wrong!", LoginView.this);
                    loginPnl.setVisibility(View.VISIBLE);
                    progressPnl.setVisibility(View.GONE);
                    return;
                }

                LoginInformation info = new LoginInformation();
                info.username = usernameResult;
                info.password = usernamePassword;

                //load landing page intent
                Intent splashViewIntent = new Intent(LoginView.this, Main.class);
                splashViewIntent.putExtra("loginInformation", info);
                startActivity(splashViewIntent);
                finish();
            }
        });
    }
}

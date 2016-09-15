package projects.synapse.com.autopaymonitors.views.basic;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.adapters.AutoPayArrayAdapter;
import projects.synapse.com.autopaymonitors.facade.AppConstants;
import projects.synapse.com.autopaymonitors.model.AutoPayProcessor;
import projects.synapse.com.autopaymonitors.model.LoginInformation;
import projects.synapse.com.autopaymonitors.utility.AlertHelper;
import projects.synapse.com.autopaymonitors.utility.ImageHelper;
import projects.synapse.com.autopaymonitors.utility.NetworkHelper;
import projects.synapse.com.autopaymonitors.utility.ResourceHelper;
import projects.synapse.com.autopaymonitors.utility.RestService;

/**
 */
public class Main extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences config = getSharedPreferences(AppConstants.applicationConfig, Activity.MODE_PRIVATE);
        boolean isFirstBlood = config.getBoolean("isFirstTime", true);
        if (isFirstBlood) {
            AlertHelper.Warning("Still in beta edition!", "Wow! This is your first time!", this);
            SharedPreferences.Editor edit = config.edit();
            edit.putBoolean("isFirstTime", false);
            edit.apply();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //load ui elements
        loadToolBarArtifacts();
        loadDateTime();
        loadEventHandler();
        loadUserInformation();

        //setup adapter.
        ListView listView = (ListView) findViewById(R.id.listOfAutogateProcessors);
        List<AutoPayProcessor> processors = new ArrayList<>();
        AutoPayArrayAdapter autoPayArrayAdapter = new AutoPayArrayAdapter(this, R.layout.autopay_processor_list_item, processors);
        listView.setAdapter(autoPayArrayAdapter);
        autoPayArrayAdapter.notifyDataSetChanged();

        //check if network connection is available
        if (NetworkHelper.isNetworkAvailable(this)) {
            getBankProcessors(processors, autoPayArrayAdapter);
        } else {
            AlertHelper.Warning("Access to the internet is required!", "Oops! No network found!", this);
        }
    }

    /***
     * loadUserInformation
     */
    public void loadUserInformation() {
        TextView msg = (TextView) findViewById(R.id.welcomeMsgTitle);
        Typeface plain = Typeface.createFromAsset(getAssets(), "fonts/opensan-semi-bold.ttf");
        msg.setTypeface(plain);

        LoginInformation info = (LoginInformation) getIntent().getSerializableExtra("loginInformation");

        String msgPrefix = AlertHelper.greeting();
        msgPrefix = String.format(msgPrefix, info.username);
        ((TextView) findViewById(R.id.welcomeMsgTitle)).setText(msgPrefix);
    }

    /***
     * Get bank processors
     *
     * @param processors
     * @param autoPayArrayAdapter
     */
    public void getBankProcessors(final List<AutoPayProcessor> processors, final AutoPayArrayAdapter autoPayArrayAdapter) {
        //use new handler
        RestService restService = new RestService();
        String url = ResourceHelper.getValue(R.string.autopay_api_url_v1, this);

        restService.getCall(url, null, new RestService.IResultHandler() {
            @Override
            public void Handle(RestService.Result data) {
                if (data.statusCode.equals("200")) {
                    try {
                        String str = data.restResponse;
                        JSONObject resp = new JSONObject(str);
                        JSONArray array = resp.getJSONArray("BankHealthInformation");
                        processors.clear();

                        for (int i = 0; i < array.length(); i++) {
                            try {
                                AutoPayProcessor processor = new AutoPayProcessor();
                                JSONObject item = array.getJSONObject(i);
                                processor.name = item.getString("BankDescription");
                                processor.code = item.getString("BankId");
                                processor.lastEventDescription = item.getString("LastEventDescription");
                                processor.isEnabled = Boolean.parseBoolean(item.getString("IsProcessorEnabled"));
                                processor.isPaused = Boolean.parseBoolean(item.getString("IsPaused"));
                                processor.description = item.getString("BankDescription") + " transaction processor";
                                processors.add(processor);
                            } catch (Exception exc) {
                                Log.e("ReadBankProcData", exc.getMessage());
                            }
                        }
                        autoPayArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "api call to autopay: fail " + data.statusCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /***
     * load Event Handler
     */
    private void loadEventHandler() {
        ListView listView = (ListView) findViewById(R.id.listOfAutogateProcessors);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoPayProcessor processor = (AutoPayProcessor) parent.getItemAtPosition(position);
                final Dialog dialog = new Dialog(Main.this);
                dialog.setContentView(R.layout.bank_summary_popup);

                int resID = getResources().getIdentifier("bank_" + processor.code, "drawable", getPackageName());
                if (resID != 0) {
                    ((ImageView) dialog.findViewById(R.id.imgBankLogo)).setImageResource(resID);
                    ((ImageView) dialog.findViewById(R.id.imgBankLogo)).setImageBitmap(ImageHelper.roundCornerImage(BitmapFactory.decodeResource(Main.this.getResources(), resID), 30));
                }


                if(!processor.isEnabled){
                    ((ImageView) dialog.findViewById(R.id.imgBankLogoIsOnline)).setImageResource(R.drawable.red);
                }

                if(processor.isPaused){
                    ((ImageView) dialog.findViewById(R.id.imgBankLogoIsSuspended)).setImageResource(R.drawable.red);
                }

                TextView tv = ((TextView) dialog.findViewById(R.id.tvBankTitle));
                TextView tvLastReponseCodes = ((TextView) dialog.findViewById(R.id.tvLastReponseCodes));
                TextView tvBankDescription = ((TextView) dialog.findViewById(R.id.tvBankDescription));
                tv.setText(processor.name);
                tvBankDescription.setText(processor.description);

                Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/opensan-semi-bold.ttf");
                Typeface plain = Typeface.createFromAsset(getAssets(), "fonts/open-san-light.ttf");
                tv.setTypeface(bold);
                tvBankDescription.setTypeface(plain);
                tvLastReponseCodes.setTypeface(plain);

                Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void loadDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM, dd yyyy");
        String currentDateandTime = sdf.format(new Date());

        TextView tv = ((TextView) findViewById(R.id.welcomeMsgExtra));
        tv.setText(currentDateandTime);

        Typeface plain = Typeface.createFromAsset(getAssets(), "fonts/open-san-light.ttf");
        tv.setTypeface(plain);
    }

    private void loadToolBarArtifacts() {

    }
}

package projects.synapse.com.autopaymonitors.views.basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.Header;
import projects.synapse.com.autopaymonitors.R;
import projects.synapse.com.autopaymonitors.adapters.AutoPayArrayAdapter;
import projects.synapse.com.autopaymonitors.model.AutoPayProcessor;
import projects.synapse.com.autopaymonitors.utility.NetworkHelper;

/**
 */
public class Main extends Activity {

    private Toolbar supportActionBar;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //load ui elements
        loadDateTime();
        loadMenu();
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
            callApi(processors, autoPayArrayAdapter);
        }else{
            Toast.makeText(this, "Network not availabale, check connectivity and refresh", Toast.LENGTH_SHORT);
        }
    }

    public void loadUserInformation(){
        TextView msg = (TextView)findViewById(R.id.welcomeMsgTitle);
        Typeface plain = Typeface.createFromAsset(getAssets(),  "fonts/opensan-semi-bold.ttf");
        msg.setTypeface(plain);
    }

    public void callApi(final List<AutoPayProcessor> processors, final AutoPayArrayAdapter autoPayArrayAdapter) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(5000);

        try {
            client.get("http://paydirectonline.com/paydirect/autopay_paydirect/AutoGateService", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    int len = response.length;
                    try {
                        String str = new String(response, "UTF-8");
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

                            }
                        }

                        autoPayArrayAdapter.notifyDataSetChanged();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Toast.makeText(getApplicationContext(), "api call to autopay: fail " + statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * load Event Handler
     */
    private void loadEventHandler() {
        ListView listView = (ListView)findViewById(R.id.listOfAutogateProcessors);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoPayProcessor processor = (AutoPayProcessor)parent.getItemAtPosition(position);
                Intent bankProcDetailIntent = new Intent(Main.this, BankProcessDetail.class);
                bankProcDetailIntent.putExtra("bankProcessor", processor);
                startActivity(bankProcDetailIntent);
            }
        });
    }

    private void loadDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM, dd yyyy");
        String currentDateandTime = sdf.format(new Date());

        TextView tv = ((TextView)findViewById(R.id.welcomeMsgExtra));
        tv.setText(currentDateandTime);

        Typeface plain = Typeface.createFromAsset(getAssets(),  "fonts/open-san-light.ttf");
        tv.setTypeface(plain);
    }

    private void loadMenu() {

        String msgTemplate = getResources().getString(R.string.welcome_message_template);
        msgTemplate = String.format(msgTemplate, "Adeola, Ojo");
        ((TextView)findViewById(R.id.welcomeMsgTitle)).setText(msgTemplate);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Typeface plain = Typeface.createFromAsset(getAssets(),  "fonts/opensan-semi-bold.ttf");

        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(plain);
        mTitle.setTextSize(16f);
        mTitle.setTextColor(Color.parseColor("#ffffff"));
    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
    }
}

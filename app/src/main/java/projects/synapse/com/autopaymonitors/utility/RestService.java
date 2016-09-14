package projects.synapse.com.autopaymonitors.utility;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/***
 * RestService Class.
 */
public class RestService {

    public interface IResultHandler{
        void Handle(Result data);
    }

    public static class Result{

        public Result(String data, String status){
            this.restResponse = data;
            statusCode = status;
        }
        public String statusCode = "404";
        public String restResponse;
    }

    /***
     * HTTP Get Call
     * @param url
     * @param headers
     * @return
     */
    public Result getCall(String url, Map<String, String> headers, final IResultHandler handler){

        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(60000);
        client.setResponseTimeout(60000);

        if(headers!=null && headers.size() > 0){
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry row = (Map.Entry)iterator.next();
                client.addHeader(row.getKey().toString(), row.getValue().toString());
            }
        }

        client.addHeader("Content-Type", "application/json");
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody, "UTF-8");
                    Result result = new Result(str, String.valueOf(statusCode));
                    handler.Handle(result);
                }catch(Exception exc){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    Result result = new Result("", String.valueOf(statusCode));
                    handler.Handle(result);
                }catch(Exception exc){

                }
            }
        });

        return new Result(null, "404");
    }
}

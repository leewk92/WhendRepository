package net.whend.soodal.whend.util;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class HTTPRestfulUtilizer {

    private String url;
    private static Bundle inputBundle;
    private static JSONObject outputJsonObject;
    private String HTTPRestType;
    private static String outputString;
    private String token;

    public HttpAsyncTask task;
    // Constructors
    public HTTPRestfulUtilizer(){

    }
    // Constructor for POST
    public HTTPRestfulUtilizer(String url, String HTTPRestType, Bundle inputBundle) {

        this.url = url;
        this.HTTPRestType = HTTPRestType;
        this.inputBundle = inputBundle;
        Log.d("HTTP Constructor url", url);
        task = new HttpAsyncTask();
       // new HttpAsyncTask().execute(url,HTTPRestType);
    }
    // Constructor for GET
    public HTTPRestfulUtilizer(String url, String HTTPRestType) {
        this.url = url;
        this.HTTPRestType = HTTPRestType;
        task = new HttpAsyncTask();
        Log.d("HTTP Constructor url",url);
       // new HttpAsyncTask().execute(url,HTTPRestType);
    }

    public void doExecution(){

        task.execute(url,HTTPRestType);
    }

    public static String POST(String url, Bundle bundle){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            for( String key : bundle.keySet()){
                jsonObject.accumulate(key, bundle.get(key));
            }

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.d("HTTP POST ResultStream", result);
            }else {
                result = "Did not work!";
                Log.d("HTTP POST ResultStream", result);
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        outputString = result;
        try {
            outputJsonObject = new JSONObject(outputString);

        }catch (Exception e){
            outputJsonObject = new JSONObject();
        }
        return result;
    }


    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpGet = new HttpGet(url);


            // 7. Set some headers to inform server about the type of the content
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.d("HTTP GET ResultStream", result);
            }
            else {
                result = "Did not work!";
                Log.d("HTTP GET ResultStream", result);
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        outputString = result;
        try {
            outputJsonObject = new JSONObject(outputString);

        }catch (Exception e){
            outputJsonObject = new JSONObject();
        }

        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String sHTTPRestType = strings[1];
            if(sHTTPRestType == "POST") {
                outputString = POST(url, inputBundle);
                try {
                    outputJsonObject = new JSONObject(outputString);

                }catch (Exception e){
                    outputJsonObject = new JSONObject();
                }
                return outputString;
            }
            else if(sHTTPRestType == "GET") {
                outputString = GET(url);
                return outputString;
            }
            else {
                outputString = GET(url);
                return outputString;
            }

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bundle getInputBundle() {
        return inputBundle;
    }

    public void setInputBundle(Bundle inputBundle) {
        this.inputBundle = inputBundle;
    }

    public JSONObject getOutputJsonObject() {
        return outputJsonObject;
    }

    public void setOutputJsonObject(JSONObject outputJsonObject) {
        this.outputJsonObject = outputJsonObject;
    }

    public String getHTTPRestType() {
        return HTTPRestType;
    }

    public void setHTTPRestType(String HTTPRestType) {
        this.HTTPRestType = HTTPRestType;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }
}

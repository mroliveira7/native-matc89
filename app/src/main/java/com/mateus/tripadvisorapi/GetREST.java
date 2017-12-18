package com.mateus.tripadvisorapi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class GetREST extends AsyncTask<String, Boolean, Integer> {

    private static String BASE_URL = "https://api.yelp.com/v3/";

    private String parametro, response;
    private int status;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        HttpURLConnection con = null;
        InputStream is = null;
        String url;

        parametro = params[params.length-1];
        url = BASE_URL;
        Log.i("Saida", "Chegou aqui!");

        for(int i = 0; i < params.length; i++){
            if(i < params.length-1){
                url = url.concat(params[i] + "/");
            }
            else {
                url = url.concat(params[i]);
            }
        }

        try {
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer hkfTztZQy2VkxL3pqE-aCxKxjJnhmlCGf8dMXHidjJl-esX-BM_gapAGW8smb7N9JQdMRLLyVVUxHB49pdeSe574iz3AefKgvNbwv2ncLxGQriKBzpRMV-9cp-grWnYx");
            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("X-Access-Token", "dwvMpq0n9RNC6fp_IzC6eQ");
            con.setReadTimeout(2000);
            con.setConnectTimeout(2000);
            con.setDoInput(true);
            con.setDoOutput(false);
            con.connect();

            status = con.getResponseCode();
//            Log.i("Status: ", String.valueOf(status));

            StringBuffer buffer1 = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ( (line = br.readLine()) != null )
                buffer1.append(line + "rn");

            is.close();
            con.disconnect();
            response = buffer1.toString();
            Log.i("httpResponse", response);
            return 1;

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        super.onPostExecute(result);

        switch (result){
            case 0:
                //ERRO
                break;
            case 1:
                //OK
                break;
        }
    }
}
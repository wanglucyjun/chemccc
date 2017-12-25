package chemccc.com.chemccc;

import android.app.Activity;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {
    Button login;
    Button daka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("Tag","onCreate");
        initView();

    }


    private void initView() {
        login = (Button) findViewById(R.id.login);
        daka=(Button) findViewById(R.id.daka);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        daka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRunable testrunner=new testRunable();
                new Thread(testrunner).start();

            }
        });
    }
    public void exetucedaka(){

            Shell shell=new Shell();
            String cmd="uiautomator runtest CartoonTest.jar -c com.ljk.daka.dakaTest";
            shell.executeUIA(cmd, true, new Shell.OnExecuteListener() {
                @Override
                public void onExecuteSucceed() {
                    Log.d("tag","success");
                }

                @Override
                public void onExecutedFailed() {
                    Log.d("tag","failed");

                }
            }, null);
            Log.d("tag","test");


    }
    public class testRunable implements Runnable{
        @Override
        public void run() {
            exetucedaka();
        }
    }



}

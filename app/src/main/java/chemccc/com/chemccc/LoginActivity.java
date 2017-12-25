package chemccc.com.chemccc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class LoginActivity extends Activity {
    EditText accout;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("Tag","onCreate");
        initView();

    }


    private void initView() {
        accout = (EditText) findViewById(R.id.edit_name);
        password = (EditText) findViewById(R.id.edit_password);
        login = (Button) findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accoutText = accout.getText().toString();
                String passwordText = password.getText().toString();
                //   Toast toast = Toast.makeText(context,accoutText,1000);
                Log.d("Tag", "the account" + accoutText);
                Log.d("Tag", passwordText);
                Log.v("Tag", "just test");

                Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        if(msg.what==0x123)
                        {
                            
                        }
                    }
                };

            }
        });
    }
    private void setHttp() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                   String urltest="http://www.baidu.com";
                    try {
                        URL url=new URL(urltest);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        //第三步，设置连接超时、读取超时
                        connection.setConnectTimeout(8000);
                        connection.setConnectTimeout(8000);
                        int code = connection.getResponseCode();
                        Log.d("Tag","the code is "+String.valueOf(code));
                        if(code==200){
                            Log.d("url", "test");
                        }
                    } catch (ProtocolException e1) {
                        e1.printStackTrace();
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                 finally {
                        //第五步，将这个HTTP 连接关闭掉
                        if(connection != null){
                            connection.disconnect();
                        }

                }
            }

        });


        }
    class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            setHttp();
            return null;
        }
    }


}

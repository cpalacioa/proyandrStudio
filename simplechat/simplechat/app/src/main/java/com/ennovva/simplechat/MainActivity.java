package com.ennovva.simplechat;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zsoft.signala.hubs.HubConnection;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
       // Change to the IP address and matching port of your SignalR server.
        String host = "http://172.25.4.83:49228/home/chat";
        // Create a new console logger
        microsoft.aspnet.signalr.client.hubs.HubConnection connection = new microsoft.aspnet.signalr.client.hubs.HubConnection(host);
        microsoft.aspnet.signalr.client.hubs.HubConnection conn = new microsoft.aspnet.signalr.client.hubs.HubConnection(host,true,)
        HubProxy hub = connection.createHubProxy("ChatHub");


        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
        } catch (InterruptedException e) {
            Log.d("error conectando", e.toString());
        } catch (ExecutionException e) {
            Log.d("error conectando", e.toString());
        }

        hub.subscribe("UpdateStatus");

    /*    try {
            hub.invoke("SendMessage", "Client", "Hello world!").get();
            hub.invoke("SendMessage", "Client", "Hello world!");

        }
         catch (ExecutionException e) {
          Toast.makeText(MainActivity.this.getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
             Log.d("error enviar", e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    // Create a new console logger
    Logger logger = new Logger() {
        @Override
        public void log(String message, LogLevel level) {
            System.out.println(message);
        }
    };


    public void UpdateStatus( String status )
    {
        Handler handler=new Handler();
        final String fStatus = status;
        handler.post(new Runnable(){
            @Override
            public void run() {
               // statusField.setText( fStatus );
                Toast.makeText(MainActivity.this.getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

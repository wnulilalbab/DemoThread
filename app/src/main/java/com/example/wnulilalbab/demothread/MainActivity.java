package com.example.wnulilalbab.demothread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    volatile boolean runningStatus = false;
    volatile Integer number = 0;

    volatile Integer milisecond = 0;
    volatile Integer second = 0;
    volatile Integer minute = 0;
    volatile Integer hour = 0;

    TextView viewNumber;
    Button viewButton;
    Thread watchThread = null;

    //use Handler
    private Handler handler;

    //berhenti watch
    ListView listTime;
    ArrayAdapter listAdapter = null;
    Integer collectNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewNumber = (TextView) findViewById(R.id.number);
        viewButton = (Button) findViewById(R.id.button);

        // collect stopwatch
        listTime = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listTime.setAdapter(listAdapter);

        // use handler runnable
        handler = new Handler();

        // use handler message
        /*handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                viewNumber.setText(Integer.toString(msg.arg1));
                //viewNumber.setText(String.format("%02d", msg.arg1));
            }
        };*/
    }

    public void collectTimeButtonClick(View view){
        listAdapter.add("["+ Integer.toString(collectNumber)+"] "+String.format("%02d", hour)+":"+String.format("%02d", minute)+":"
                +String.format("%02d", second)+":"+String.format("%02d", milisecond));

        collectNumber++;
        listTime.setSelection(listAdapter.getCount() - 1);
    }

    public void startStopButtonClick(View view){

        if (!runningStatus){
            runningStatus = true;
            watchThread = new Thread(new WatchThread());
            watchThread.start();
            viewButton.setText("Stop");
        }else{

            runningStatus = false;
            viewButton.setText("Start");
        }

    }

    private class WatchThread implements Runnable{

        @Override
        public void run() {
            while (runningStatus){
                number++;

                //stopwach counter
                milisecond++;

                if (milisecond == 61){
                    second++;
                    milisecond = 0;
                }

                if (second == 61){
                    minute++;
                    second = 0;
                }

                if (minute == 61){
                    hour++;
                    minute = 0;
                }

                final String displayTime = String.format("%02d", hour)+":"+String.format("%02d", minute)+":"
                        +String.format("%02d", second)+":"+String.format("%02d", milisecond);

                //use runOnUiThread
                /*MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewNumber.setText(Integer.toString(number));
                    }
                });*/

                //use handler runnable
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //viewNumber.setText(Integer.toString(number));

                        viewNumber.setText(displayTime);
                    }
                });

                //use handler message
                /*Message msg = Message.obtain();
                msg.arg1 = number;
                handler.sendMessage(msg);*/

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

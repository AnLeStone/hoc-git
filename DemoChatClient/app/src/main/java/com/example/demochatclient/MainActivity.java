package com.example.demochatclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView lv, lvChat;
    ArrayList<String> listChat;

    private Socket mSocket;
    {
        try{
            mSocket = IO.socket("http://192.168.1.7:3000");
        }catch (URISyntaxException e){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv =(ListView) findViewById(R.id.lv);
        lvChat = (ListView) findViewById(R.id.lvChat);

        mSocket.connect();
        mSocket.on("ketquadangky", onNewMessage_DangkyUsername);
        mSocket.on("server-gui-danhsach", onNewMessage_DanhsachDangky);
        mSocket.on("server-gui-chat", onNewMessage_ServerGuiChat);

        Button btn = (Button) findViewById(R.id.btn);
        final EditText et = (EditText) findViewById(R.id.etUsername);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("client-gui-username", et.getText().toString());
            }
        });

        Button btnChat = (Button) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("client-gui-chat", et.getText().toString());
            }
        });

        listChat = new ArrayList<>();


    }

    private Emitter.Listener onNewMessage_DangkyUsername = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung;
                    try{
                        noidung = data.getString("noidung");

                        if (noidung.equals("true")){

                            Toast.makeText(getBaseContext(), "đăng ký thành công", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getBaseContext(), "đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                        return;
                    }
                }
            });
        }
    };


    private Emitter.Listener onNewMessage_DanhsachDangky= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray noidung;
                    ArrayList<String> array = new ArrayList<>();
                    try{
                        noidung = data.getJSONArray("danhsach");
                        for (int i = 0; i < noidung.length(); i++){
                            array.add(noidung.get(i).toString());
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, array);
                        lv.setAdapter(arrayAdapter);

                    }catch (JSONException e){
                        return;
                    }
                }
            });
        }
    };


    private Emitter.Listener onNewMessage_ServerGuiChat= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung;
                    try{
                        noidung = data.getString("ndchat");
                        listChat.add(noidung);

                        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, listChat);
                        lvChat.setAdapter(arrayAdapter);

                    }catch (JSONException e){
                        return;
                    }
                }
            });
        }
    };
}

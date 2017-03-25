package com.example.chatclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnSend;

    EditText etUsername;
    EditText etMessage;
    EditText etId;

    String myId;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://192.168.1.7:3000");
        } catch (URISyntaxException e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mSocket.connect();
        mSocket.on("server-send-login-result", onNewMessage_LoginResult);
        mSocket.on("server-send-chat-to-id",onNewMessage_GetChatMessage);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSend = (Button) findViewById(R.id.btnSend);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etMessage = (EditText) findViewById(R.id.etMessage);
        etId = (EditText) findViewById(R.id.etId);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gọi hàm ktra tên đăng nhập tới serverkaa
                //nếu đúng tên & mk
                mSocket.emit("client-send-login-name", etUsername.getText().toString());
                //nếu ko trả về sai mk
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = etMessage.getText().toString();
                String id = etId.getText().toString();
                mSocket.emit("client-send-chat-to-id", new String[]{id, msg});
            }
        });



    }

    private Emitter.Listener onNewMessage_LoginResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung;
                    try {
                        noidung = data.getString("data");

                        if (noidung.equals("true")) {

                            Toast.makeText(getBaseContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                            etUsername.setEnabled(false);
                            btnLogin.setVisibility(View.GONE);
                            myId = etUsername.getText().toString();

                        } else {

                            Toast.makeText(getBaseContext(), "Sai tên đăng nhập", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewMessage_GetChatMessage = new Emitter.Listener() {
        @Override
        public void call(final  Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung;
                    String id;
                    try {
                        noidung = data.getString("data");
                        id = data.getString("id");
                        if (myId.equals(id))
                            Toast.makeText(getBaseContext(), noidung, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };


}

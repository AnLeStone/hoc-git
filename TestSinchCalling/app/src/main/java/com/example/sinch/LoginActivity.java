package com.example.sinch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button btnLogin;
    private Button btnSignup;
    private EditText etSdt;
    private EditText etPass;
    private ProgressDialog mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        etSdt = (EditText) findViewById(R.id.etSdt);
        etPass = (EditText) findViewById(R.id.etPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin ();
            }
        });
    }

    private void clickLogin(){

        String sdt = etSdt.getText().toString();

        if (sdt.isEmpty()) {
            Toast.makeText(this, "Hãy nhập số điện thoại", Toast.LENGTH_LONG).show();
            return;
        }

        //Xử lý với server chính

        //Xử lý với server sinch
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(sdt);
            showSpinner();
        } else {
            //chuyển vào đăng nhập thành công
            openActionActivity();
        }

    }

    private void openActionActivity() {
        Intent mainActivity = new Intent(this, ActionActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Đang đăng nhập");
        mSpinner.setMessage("Chờ tí nhe...");
        mSpinner.show();
    }


    //Hàm này bắt buộc khi implements StartSinchListener
    // Kiểm tra kết nối đến dịch vụ
    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, "Lỗi mạng rồi: \n"+ error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    //Hàm này bắt buộc khi implements StartSinchListener
    // onStarted thì mở đến ActionActivity
    @Override
    public void onStarted() {
        openActionActivity();
    }



    // Hàm trong Start, kết nối đến Service
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }
}

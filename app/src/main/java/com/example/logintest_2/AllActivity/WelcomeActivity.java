package com.example.logintest_2.AllActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.logintest_2.R;

public class WelcomeActivity extends AppCompatActivity {

    private Button btn_sign_out;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_sign_out = findViewById(R.id.btn_sign_out);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(WelcomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //忘记用户名、密码
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", "");
                editor.putString("PASSWORD","");
                editor.commit();
                sp.edit().putBoolean("ISCHECK", false).commit();
                sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                startActivity(intent5);
            }
        });
    }
}

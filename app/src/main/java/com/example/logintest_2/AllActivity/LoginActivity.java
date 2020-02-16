package com.example.logintest_2.AllActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.logintest_2.DataBaseHelper.MyDataBaseHelper;
import com.example.logintest_2.R;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private CheckBox cb_rem_password;
    private CheckBox cb_auto_login;
    private String username;
    private String password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //获取实例对象
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        et_username = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        cb_auto_login = findViewById(R.id.cb_auto_login);
        cb_rem_password = findViewById(R.id.cb_rem_password);

        //判断记住密码多选框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            //默认是自动登录的状态
            cb_rem_password.setChecked(true);
            //将输入框中加上上一次登录的用户信息
            et_username.setText(sp.getString("USER_NAME", ""));
            et_password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false)) {
                //默认是自动登录状态
                cb_auto_login.setChecked(true);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, LogoActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
//                if(username.equals("liu") && password.equals("123"))
//                {
//                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
//                    //登录成功和记住密码框为选中状态才保存用户信息
////                    if(cb_rem_password.isChecked())
////                    {
////                        //记住用户名、密码、
////                        SharedPreferences.Editor editor = sp.edit();
////                        editor.putString("USER_NAME", username);
////                        editor.putString("PASSWORD",password);
////                        editor.commit();
////                    }
//                    //跳转界面
//                    Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
//                    LoginActivity.this.startActivity(intent);
//                    //finish();
//
//                }else{
//
//                    Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
//                }
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();

                }else {

                    MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(LoginActivity.this);
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase(); //打开数据库
                    Cursor cursor = database.query("user", new String[]{"username", "password", "id"}, "username=?", new String[]{username}, null, null, null);

                    if (cursor.moveToFirst()) {
                        //如果用户存在，找到表中此用户对应的密码
                        String password_1 = cursor.getString(cursor.getColumnIndex("password"));
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String id_string = String.valueOf(id);
                        //判断密码是否正确
                        if (password.equals(password_1)){

                            //活动跳转到店铺主页面
                            Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                            //登录成功和记住密码框为选中状态才保存用户信息
                            if(cb_rem_password.isChecked())
                            {
                                //记住用户名、密码、
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("USER_NAME", username);
                                editor.putString("PASSWORD",password);
                                editor.commit();
                            }
                            Intent intent = new Intent(LoginActivity.this, LogoActivity.class);

                            SharedPreferences sp1=getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1=sp1.edit();
                            editor1.putString("user_id",id_string);
                            editor1.apply();

                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(LoginActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                    database.close();

                }
            }
        });

        //监听记住密码多选框按钮事件
        cb_rem_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (cb_rem_password.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        cb_auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (cb_auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        //登陆界面 注册 按钮的监听
        Button btn1 = (Button)findViewById(R.id.btn_register);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

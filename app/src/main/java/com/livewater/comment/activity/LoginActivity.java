package com.livewater.comment.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.livewater.comment.R;



/**
 * Created by tiansj on 15/7/31.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

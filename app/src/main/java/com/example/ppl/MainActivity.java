package com.example.ppl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import serviceprovider.activities.GetServiceProviderActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button navigateButton = (Button) findViewById(R.id.button_navigate);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GetServiceProviderActivity.class);
                startActivity(intent);
            }
        });
    }
}

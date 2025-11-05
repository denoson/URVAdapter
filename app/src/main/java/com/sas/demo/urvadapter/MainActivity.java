package com.sas.demo.urvadapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.urvadapter.URVNumberDialogConfig;
import com.sas.urvadapter.URVNumberInputDialog;
import com.sas.urvadapter.URVAdapter;

/**
 * Date: 2024.09.25
 * Author: Den Vigovski
 * Universal RecycleView adapter
 * Licence: This is an experimental project for educational purposes. Not for commercial use.
 */

public class MainActivity extends AppCompatActivity {

    private URVAdapter adapter;
    private RecyclerView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView lbl = findViewById(R.id.lbl);
        lbl.setText("Adapter Version " + URVAdapter.VERSION);

        Button btn = findViewById(R.id.btnDemoList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DemoListActivity.class);
                startActivity(intent);
            }
        });

        btn = findViewById(R.id.btnProps);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DemoPropertyActivity.class);
                startActivity(intent);
            }
        });


        btn = findViewById(R.id.btnDemoGrid);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DemoGridActivity.class);
                startActivity(intent);
            }
        });

        btn = findViewById(R.id.btnDialog);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showMyCustomDialog();
            }
        });
    }

    private void showMyCustomDialog() {

        URVNumberDialogConfig nidCfg = new URVNumberDialogConfig();
        nidCfg.minValue = 10;
        nidCfg.maxValue = 1000;

        URVNumberInputDialog nid = new URVNumberInputDialog(this, nidCfg, new URVNumberInputDialog.INumberInput() {
            @Override
            public void onNumberSet(float number) {
                Toast.makeText(getApplicationContext(), "Введено: " + number, Toast.LENGTH_SHORT).show();
            }
        });
        nid.show();
    }




}
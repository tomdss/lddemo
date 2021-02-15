package com.example.mycustomview1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNumber;
    private ArrayList listNumber;
    private ProgressBar pbLoading;
    public static final String MY_PREFS_VND = "MyPrefsVnd";
    private TextView tvVnd;
    private EditText edtNumberInput;
    private EditText edtVndInput;
    private int mIdVnd;
    private int mVnd;
    private int mNumber;
    private int mVndOld;
    private TextView tvVndTotal;
    private TextView tvResult;
    private EditText edtCode;
    private TextView tvShow;
    private Button btnCode;
    private Boolean isCodeShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initData() {
        isCodeShow = false;
        tvVnd = findViewById(R.id.tvVnd);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_VND, MODE_PRIVATE);
        mIdVnd = prefs.getInt("idVND", 0); //0 is the default value.
        if (mIdVnd == 0) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_VND, MODE_PRIVATE).edit();
            editor.putInt("idVND", 10000000);
            editor.apply();
            mIdVnd = prefs.getInt("idVND", 0);
        }
        tvVnd.setText(formatNumberVnd(mIdVnd));
    }

    private void initViews() {
        tvShow = findViewById(R.id.tvShow);
        edtNumberInput = findViewById(R.id.edtNumber);
        edtVndInput = findViewById(R.id.edtVnd);
        tvNumber = findViewById(R.id.tvNumber);
        pbLoading = findViewById(R.id.pbLoading);
        tvVndTotal = findViewById(R.id.tvVndTotal);
        tvResult = findViewById(R.id.tvResult);
        edtCode = findViewById(R.id.edtCode);
        listNumber = new ArrayList();
        findViewById(R.id.btnSpin).setOnClickListener(this);
        btnCode = findViewById(R.id.btnCode);
        btnCode.setOnClickListener(this);
        tvShow.setOnClickListener(this);
        edtVndInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().isEmpty()) {
                    tvVndTotal.setText(Integer.parseInt(edtVndInput.getText().toString()) * 23000 + "");
                } else {
                    tvVndTotal.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSpin:
                tvResult.setText("RESULT");
                if (isInputValue()) {
                    mNumber = Integer.parseInt(edtNumberInput.getText().toString());
                    mVnd = Integer.parseInt(edtVndInput.getText().toString());
                    if (mVnd * 23000 > mIdVnd) {
                        Toast.makeText(this, "NOT MONEY ENOUGH", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        mVndOld = mIdVnd;
                        mIdVnd = mIdVnd - mVnd * 23000;
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_VND, MODE_PRIVATE).edit();
                        editor.putInt("idVND", mIdVnd);
                        editor.apply();
                        tvVnd.setText(formatNumberVnd(mIdVnd));
                    }
                }
                tvNumber.setText("");
                listNumber.clear();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.setIndeterminate(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 27; i++) {
                            Random random = new Random();
                            int k = random.nextInt(100);
                            compareNumberInput(k);
                            listNumber.add(k);
                        }
                        Collections.sort(listNumber);
                        tvNumber.setText(listNumber.toString());
                        pbLoading.setVisibility(View.GONE);
                        if (mVndOld < mIdVnd) {
                            tvResult.setText("SUCCESS");
                        } else {
                            tvResult.setText("NOT SUCCESS");
                        }
                    }
                }, 5000);
                break;
            case R.id.btnCode:
                if (edtCode.getText() != null && !edtCode.getText().toString().isEmpty()) {
                    if (edtCode.getText().toString().equals("Abc")) {
                        mIdVnd = mIdVnd + 1000000;
                    } else if (edtCode.getText().toString().equals("Abc10000")) {
                        mIdVnd = mIdVnd + 10000000;
                    } else if (edtCode.getText().toString().equals("Abc100000")) {
                        mIdVnd = mIdVnd + 100000000;
                    } else {
                        Toast.makeText(this, "Code not true", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_VND, MODE_PRIVATE).edit();
                    editor.putInt("idVND", mIdVnd);
                    editor.apply();
                    tvVnd.setText(formatNumberVnd(mIdVnd));
                } else {
                    Toast.makeText(this, "Code not true", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvShow:
                if (!isCodeShow) {
                    edtCode.setVisibility(View.VISIBLE);
                    btnCode.setVisibility(View.VISIBLE);
                    tvShow.setText("hide code");
                    isCodeShow = true;
                } else {
                    edtCode.setVisibility(View.GONE);
                    btnCode.setVisibility(View.GONE);
                    tvShow.setText("show code");
                    isCodeShow = false;
                }
                break;
            default:
                break;
        }
    }

    private boolean isInputValue() {
        return edtNumberInput.getText() != null && !edtNumberInput.getText().toString().isEmpty()
                && edtVndInput.getText() != null && !edtVndInput.getText().toString().isEmpty();
    }

    private void compareNumberInput(int k) {
        if (edtNumberInput.getText() != null && !edtNumberInput.getText().toString().isEmpty()
                && mNumber == k) {
            mIdVnd = mIdVnd + (80 / 23) * mVnd * 23000;
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_VND, MODE_PRIVATE).edit();
        editor.putInt("idVND", mIdVnd);
        editor.apply();
        tvVnd.setText(formatNumberVnd(mIdVnd));
    }

    private String formatNumberVnd(int vnd) {
        NumberFormat formatter = new DecimalFormat("#,###,###,###");
        return formatter.format(vnd);
    }
}

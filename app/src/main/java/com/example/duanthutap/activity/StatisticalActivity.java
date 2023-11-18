package com.example.duanthutap.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.StaticalAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StatisticalActivity extends AppCompatActivity implements StaticalAdapter.CalculationCallback {
    private TextInputEditText edStartDate;
    private ImageView imgStartDate;
    private TextInputEditText edEndDate;
    private ImageView imgEndDate;
    private ImageView imgDoanhThu;
    private ImageButton imgBack;
    private TextView tvDoanhThu;
    StaticalAdapter staticalAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int mYear, mMonth, mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        init();

        DatePickerDialog.OnDateSetListener FromDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDate);
            edStartDate.setText(dateFormat.format(calendar.getTime()));
        };
        DatePickerDialog.OnDateSetListener ToDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDate);
            edEndDate.setText(dateFormat.format(calendar.getTime()));
        };
        imgStartDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDate = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog_start = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,FromDate,mYear, mMonth, mDate);
            dialog_start.show();
        });
        imgEndDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDate = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog_end = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,ToDate,mYear, mMonth, mDate);
            dialog_end.show();
        });

        imgDoanhThu.setOnClickListener(view -> {
            String start = edStartDate.getText().toString();
            String end = edEndDate.getText().toString();
            staticalAdapter.calculateRevenue(start, end, this);
        });
        imgBack.setOnClickListener(view -> {
            finish();
        });
    }
    void init(){
        imgBack = (ImageButton) findViewById(R.id.img_back);
        edStartDate = findViewById(R.id.ed_start_date);
        imgStartDate = findViewById(R.id.img_start_date);
        edEndDate = findViewById(R.id.ed_end_date);
        imgEndDate = findViewById(R.id.img_end_date);
        imgDoanhThu = findViewById(R.id.img_doanh_thu);
        tvDoanhThu = findViewById(R.id.tv_doanh_thu);
        staticalAdapter = new StaticalAdapter();
        edStartDate.setFocusable(false);
        edStartDate.setFocusableInTouchMode(false);
        edEndDate.setFocusable(false);
        edEndDate.setFocusableInTouchMode(false);
    }

    @Override
    public void onCalculated(double revenue) {
        tvDoanhThu.setText(""+revenue+" $");
    }

    @Override
    public void onCalculationError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
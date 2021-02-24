package com.example.CalEasy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_0,btn_00, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_ac, btn_history, btn_point, btn_equals, btn_add, btn_minus, btn_times, btn_divide;
    FrameLayout btn_delete;
    EditText et_value;
    //定义map集合封装“按钮-数值”对
    Map<Integer, String> map;
    List<Object> calSym = new ArrayList<>();
    //定义运算过程list，num1，运算符，num2
    List<Object> calList = new ArrayList<>();
    //定义当前进行运算的过程是否结束
    boolean flag = true;
    //定义记录
    List<Object> calHistory = new ArrayList<>();

    //定义按压动作
    boolean prsFlag=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();
    }

    //将按键文本和实际数值对应起来，封装进map中
    private void initData() {
        map = new HashMap<>();
        map.put(R.id.btn_0, "0");
        map.put(R.id.btn_00, "00");
        map.put(R.id.btn_1, "1");
        map.put(R.id.btn_2, "2");
        map.put(R.id.btn_3, "3");
        map.put(R.id.btn_4, "4");
        map.put(R.id.btn_5, "5");
        map.put(R.id.btn_6, "6");
        map.put(R.id.btn_7, "7");
        map.put(R.id.btn_8, "8");
        map.put(R.id.btn_9, "9");
        map.put(R.id.btn_point, ".");

        map.put(R.id.btn_add, "+" + "");
        map.put(R.id.btn_minus, "-" + "");
        map.put(R.id.btn_times, "*" + "");
        map.put(R.id.btn_divide, "÷" + "");
        map.put(R.id.btn_equals, "=" + "");

        Collections.addAll(calSym, "+", "-", "*", "÷", "=");
    }

    //实例化对象,19个控件
    private void initView() {
        btn_0 = findViewById(R.id.btn_0);
        btn_00 = findViewById(R.id.btn_00);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_ac = findViewById(R.id.btn_ac);
        btn_delete = findViewById(R.id.btn_delete);
        btn_point = findViewById(R.id.btn_point);
        btn_equals = findViewById(R.id.btn_equals);
        btn_add = findViewById(R.id.btn_add);
        btn_minus = findViewById(R.id.btn_minus);
        btn_times = findViewById(R.id.btn_times);
        btn_divide = findViewById(R.id.btn_divide);
        et_value = findViewById(R.id.et_value);
    }

    //设置点击事件，18个btn，除了et_value控件
    private void initListener() {
        btn_0.setOnClickListener(this);
        btn_00.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_equals.setOnClickListener(this);
        btn_ac.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_times.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //0-9 . 能成功输入
            case R.id.btn_0:
            case R.id.btn_00:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (flag) {//运算结束
                    et_value.setText(map.get(v.getId()));//直接覆盖数值替代
                    flag = false;
                } else {//运算没有结束，直接进行拼接
                    appendDigital(v.getId());
                }
                break;
            case R.id.btn_point:
                if (et_value.getText().toString().indexOf('.')!=-1 ) {//已经含有小数点
                    return;
                }else {
                    appendDigital(v.getId());
                }
                break;
            case R.id.btn_add:
            case R.id.btn_minus:
            case R.id.btn_times:
            case R.id.btn_divide:
            case R.id.btn_equals:
                calRecord(v.getId());
                break;
            case R.id.btn_ac://清零
                et_value.setText("0");
                calList.clear();
                break;
            case R.id.btn_delete://回退
                deleteDigital();
                break;
        }
    }

    //进行运算
    private void calRes() {
        //先存储et值
        calList.add(et_value.getText().toString());

        //仅当前数，证明没有运算
        if (calList.size() == 1) {
            return;
        } else if (calList.size() == 2) {
            et_value.setText(calList.get(0) + "");
            calList.remove(1);
            return;
        }

        //计算结果,两个数
        String res;
        Double num1 = Double.parseDouble((String) calList.get(0));
        Double num2 = Double.parseDouble((String) calList.get(2));
        if (calList.get(1) == "+") {
            res = BigDecimal.valueOf(num1).add(BigDecimal.valueOf(num2)) + "";
        } else if (calList.get(1) == "-") {
            res = BigDecimal.valueOf(num1).subtract(BigDecimal.valueOf(num2)) + "";
        } else if (calList.get(1) == "*") {
            res = BigDecimal.valueOf(num1).multiply(BigDecimal.valueOf(num2)) + "";
        } else {
            if (num2 == 0) {
                et_value.setText("######");
                flag = true;
                return;
            }
            res = BigDecimal.valueOf(num1).divide(BigDecimal.valueOf(num2), 3, BigDecimal.ROUND_HALF_UP) + "";
        }
        //去除末尾.0000000/.340的结果,正则表达式
        res = res.replaceAll("[.]0{1,9}$", "");
//        res = res.replaceAll("[.]d{1,9}0{1,9}$", "[.]d{1,9}0{1,9}$");
        //回写结果
        if(res.length()>9){
            et_value.setText("过长");
            return;
        }

        if (res.equals("")) {
            res = "0";
        }
        et_value.setText(res);
        calList.clear();
        calList.add(res);
        //计算结果为0
        if (calList.size() < 1) {
            et_value.setText("0");
        }
        flag = true;
    }

    //输入运算符时的运算存储过程
    private void calRecord(int resId) {
        flag = true;//需要切换界面
        if (map.get(resId).equals("=")) {
            calRes();
            calList.clear();
            return;
        }
        calList.add(et_value.getText().toString());
        //判断之前是否有运算过程的存储
        if (calList.size() == 3) {
            //执行运算过程
            calRes();
            if (map.get(resId) == "=") {
                calList.clear();
                return;
            } else {
                calList.add(map.get(resId));
            }
            return;
        }
        if (calList.size() >= 2 && calSym.contains(calList.get(1))) {
            calList.remove(1);
            calList.add(map.get(resId));
            return;
        }
        //执行calStr的存储过程
        calList.add(map.get(resId));
    }

    //根据id值找到0-9按钮所对应的按钮值，除去首位0，并进行拼接输出展示
    private void appendDigital(int resId) {
        //进行当前字符串的拼接
        String temp = et_value.getText().append(map.get(resId)).toString();
        //全输入0的情况只显示一个0
        if (temp.replaceAll("0", "").equals("")) {
            et_value.setText("0");
            return;
        }
        // rm 0
        String newStr = temp.replaceAll("^(0+)", "");//去的掉句首“0”
        if (newStr.charAt(0) == '.') {
            et_value.setText("0" + newStr);
            return;
        }
        et_value.setText(newStr);
    }

    //删除末尾字符并设置显示到et
    private void deleteDigital() {
        Editable temp = et_value.getText();
        if (temp.length() == 1) {
            et_value.setText("0");
        } else if (temp.charAt(temp.length() - 2) == '.') {
            et_value.setText(temp.subSequence(0, temp.length() - 2));
        } else {
            String text = temp.subSequence(0, temp.length() - 1).toString();
            et_value.setText(text);
        }
    }
}

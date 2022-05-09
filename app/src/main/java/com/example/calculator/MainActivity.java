package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    final String ZERO = "0";
    final String ONE = "1";
    final String TWO = "2";
    final String THREE = "3";
    final String FOUR = "4";
    final String FIVE = "5";
    final String SIX = "6";
    final String SEVEN = "7";
    final String EIGHT = "8";
    final String NINE = "9";
    final String EMPTY = "";
    final String ADD = "+";
    final String SUB = "-";
    final String MUL = "*";
    final String DIV = "/";
    final int EQ = 10;
    final int BS = 11;
    final int CE = 12;
    final int C = 13;
    final int CM = 14;

    TextView topText;
    TextView bottomText;
    Button buttonZero;
    String topStr = EMPTY;
    String bottomStr = ZERO;
    final ArrayList<String> list = new ArrayList<>();
    int last = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topText = findViewById(R.id.text_top);
        bottomText = findViewById(R.id.text_bottom);
        buttonZero = findViewById(R.id.button_0);

        buttonZero.setOnClickListener(v -> enterNumber(ZERO));

        findViewById(R.id.button_1).setOnClickListener(v -> enterNumber(ONE));
        findViewById(R.id.button_2).setOnClickListener(v -> enterNumber(TWO));
        findViewById(R.id.button_3).setOnClickListener(v -> enterNumber(THREE));
        findViewById(R.id.button_4).setOnClickListener(v -> enterNumber(FOUR));
        findViewById(R.id.button_5).setOnClickListener(v -> enterNumber(FIVE));
        findViewById(R.id.button_6).setOnClickListener(v -> enterNumber(SIX));
        findViewById(R.id.button_7).setOnClickListener(v -> enterNumber(SEVEN));
        findViewById(R.id.button_8).setOnClickListener(v -> enterNumber(EIGHT));
        findViewById(R.id.button_9).setOnClickListener(v -> enterNumber(NINE));

        findViewById(R.id.button_Equal).setOnClickListener(v -> enterOperator1(EQ));
        findViewById(R.id.button_CM).setOnClickListener(v -> enterOperator1(CM));
        findViewById(R.id.button_BS).setOnClickListener(v -> enterOperator1(BS));

        findViewById(R.id.button_CE).setOnClickListener(v -> enterClear(CE));
        findViewById(R.id.button_C).setOnClickListener(v -> enterClear(C));

        findViewById(R.id.button_Add).setOnClickListener(v -> enterOperator2(ADD));
        findViewById(R.id.button_Sub).setOnClickListener(v -> enterOperator2(SUB));
        findViewById(R.id.button_Mul).setOnClickListener(v -> enterOperator2(MUL));
        findViewById(R.id.button_Div).setOnClickListener(v -> enterOperator2(DIV));
    }

    private void enterClear(int op) {

        if (op == C) {

            topStr = "";
            list.clear();
        }

        bottomStr = ZERO;
        last = 15;
        topText.setText(topStr);
        bottomText.setText(bottomStr);
    }

    private void enterOperator1(int op) {

        if (!bottomStr.equals("0")) {

            if (op == BS) {

                if (last < 10) {

                    int len = bottomStr.length() - 1;

                    last = bottomStr.charAt(len) - '0';

                    if (bottomStr.charAt(0) == '-') {

                        bottomStr = len > 1 ? bottomStr.substring(0, len) : "0";
                    } else {

                        bottomStr = len > 0 ? bottomStr.substring(0, len) : "0";
                    }
                }
            }
            else if (op == CM) {

                if (bottomStr.charAt(0) == '-') {

                    bottomStr = bottomStr.substring(1);
                }
                else {

                    bottomStr = "-" + bottomStr;
                }

                last = bottomStr.charAt(bottomStr.length() - 1) - '0';
            }
            else {

                list.add(bottomStr);

                bottomStr = solve();

                topStr = "";

                list.clear();

                last = bottomStr.charAt(bottomStr.length() - 1) - '0';
            }
        }

        setButtonZero();
        topText.setText(topStr);
        bottomText.setText(bottomStr);
    }

    private void enterOperator2(@NonNull String op) {

        if (op.equals("/")) buttonZero.setClickable(false);
        if (last < 15) {
            topStr += bottomStr + op;
            list.add(bottomStr);
            list.add(op);
            bottomStr = solve();
        }
        else {
            list.remove(list.size() - 1);
            list.add(op);
            topStr = topStr.substring(0, topStr.length() -1) + op;
        }

        last = 15;
        if (!op.equals("/")) buttonZero.setClickable(true);
        topText.setText(topStr);
        bottomText.setText(bottomStr);
    }

    @NonNull
    private String solve() {

        AtomicInteger ans = new AtomicInteger(0);
        AtomicInteger op = new AtomicInteger(1);
        for (String s: list) {

            switch (s) {
                case "+":
                    op.set(1);
                    break;
                case "-":
                    op.set(2);
                    break;
                case "*":
                    op.set(3);
                    break;
                case "/":
                    op.set(4);
                    break;
                default:
                    try {
                        switch (op.get()) {
                            case 1:
                                ans.set(ans.get() + Integer.parseInt(s));
                                break;
                            case 2:
                                ans.set(ans.get() - Integer.parseInt(s));
                                break;
                            case 3:
                                ans.set(ans.get() * Integer.parseInt(s));
                                break;
                            default:
                                ans.set(ans.get() / Integer.parseInt(s));
                        }
                    } catch (NumberFormatException ex) {
                        ans.set(0);
                    }
            }
        }

        return String.valueOf(ans.get());
    }

    private void enterNumber(String num) {

        bottomStr = (bottomStr.equals(ZERO) || last >= 10) ? num : bottomStr + num;

        last = Integer.parseInt(num);

        setButtonZero();

        bottomText.setText(bottomStr);
    }

    private void setButtonZero() {
        if (topStr.equals(EMPTY)) {
            buttonZero.setClickable(true);
        }
        else {
            buttonZero.setClickable(topStr.charAt(topStr.length() - 1) != '/');
        }
    }
}
package com.sas.urvadapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


/**
 * ......................................................
 * Constructor ..........................................
 * ......................................................
 */
public class URVNumberInputDialog extends Dialog  {

    public interface INumberInput {
        void onNumberSet(float number);
    }

    public final URVNumberDialogConfig cfg;
    private final INumberInput listener;

    private TextView numberDisplay;
    private final StringBuilder currentInput;
    private boolean hasDecimalPoint = false;
    private boolean isNegative = false;


    public URVNumberInputDialog(Context context, URVNumberDialogConfig cfg, INumberInput listener) {
        super(context);
        this.cfg =  (cfg != null) ? cfg : new URVNumberDialogConfig();
        currentInput = new StringBuilder();

        if(this.cfg.useFloatNumber) currentInput.append(this.cfg.defaultValue);
         else currentInput.append(Math.round(this.cfg.defaultValue));

        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.number_input_dialog);

        numberDisplay = findViewById(R.id.d_number_display);
        updateDisplay();

        setupNumberButton(R.id.d_btn_0, "0");
        setupNumberButton(R.id.d_btn_1, "1");
        setupNumberButton(R.id.d_btn_2, "2");
        setupNumberButton(R.id.d_btn_3, "3");
        setupNumberButton(R.id.d_btn_4, "4");
        setupNumberButton(R.id.d_btn_5, "5");
        setupNumberButton(R.id.d_btn_6, "6");
        setupNumberButton(R.id.d_btn_7, "7");
        setupNumberButton(R.id.d_btn_8, "8");
        setupNumberButton(R.id.d_btn_9, "9");

        findViewById(R.id.d_btn_decimal).setOnClickListener(v -> addDecimalPoint());
        findViewById(R.id.d_btn_negative).setOnClickListener(v -> toggleNegative());
        findViewById(R.id.d_btn_clear).setOnClickListener(v -> clearInput());
        findViewById(R.id.d_btn_backspace).setOnClickListener(v -> backspace());

        findViewById(R.id.d_btn_ok).setOnClickListener(v -> confirmNumber());
        findViewById(R.id.d_btn_cancel).setOnClickListener(v -> cancel());

        if(!cfg.useFloatNumber) findViewById(R.id.d_btn_decimal).setEnabled(false);
        if(!cfg.useNegative) findViewById(R.id.d_btn_negative).setEnabled(false);

        View v = findViewById(R.id.dialog_digits);
        v.setBackgroundColor(cfg.colorBackground);

        setupButtonStyle(R.id.d_btn_0, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_1, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_2, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_3, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_4, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_5, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_6, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_7, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_8, cfg.colorDigits);
        setupButtonStyle(R.id.d_btn_9, cfg.colorDigits);

        setupButtonStyle(R.id.d_btn_decimal, cfg.colorOperation);
        setupButtonStyle(R.id.d_btn_negative, cfg.colorOperation);

        setupButtonStyle(R.id.d_btn_clear, cfg.colorFunction);
        setupButtonStyle(R.id.d_btn_backspace, cfg.colorFunction);

        setupButtonStyle(R.id.d_btn_ok, cfg.colorOk);
        setupButtonStyle(R.id.d_btn_cancel, cfg.colorCancel);
    }

    private void setupNumberButton(int buttonId, String number) {
        findViewById(buttonId).setOnClickListener(v -> addDigit(number));
    }

    private void setupButtonStyle(int buttonId, int bckColor) {
        Button button = findViewById(buttonId);
        button.setBackgroundColor(bckColor);
        button.setTextColor(cfg.colorButtonText);
    }

    private void addDigit(String digit) {
        if (currentInput.toString().equals("0")) {
            currentInput.setLength(0);
        }
        currentInput.append(digit);
        updateDisplay();
    }

    private void addDecimalPoint() {
        if (!hasDecimalPoint) {
            if (currentInput.length() == 0) {
                currentInput.append("0");
            }
            currentInput.append(".");
            hasDecimalPoint = true;
            updateDisplay();
        }
    }

    private void toggleNegative() {
        isNegative = !isNegative;
        updateDisplay();
    }

    private void clearInput() {
        currentInput.setLength(0);
        hasDecimalPoint = false;
        isNegative = false;
        updateDisplay();
    }

    private void backspace() {
        int length = currentInput.length();
        if (length > 0) {
            char lastChar = currentInput.charAt(length - 1);
            if (lastChar == '.') {
                hasDecimalPoint = false;
            }
            currentInput.deleteCharAt(length - 1);
        }
        updateDisplay();
    }

    private void updateDisplay() {
        String numberStr = currentInput.toString();
        if (numberStr.isEmpty()) {
            numberStr = "0";
        }
        if (isNegative) {
            numberStr = "-" + numberStr;
        }
        numberDisplay.setText(numberStr);
    }


    private void confirmNumber() {
        if (currentInput.length() == 0) {
            showError(cfg.textTitle);
            return;
        }

        String numberStr = numberDisplay.getText().toString();

        try {
            float number = Float.parseFloat(numberStr);

            if(cfg.minValue != cfg.maxValue) {
                if (number < cfg.minValue || number > cfg.maxValue) {
                    showError(String.format(Locale.getDefault(), cfg.textValidateError, cfg.minValue, cfg.maxValue));
                    return;
                }
            }

            if (listener != null) listener.onNumberSet(number);
            dismiss();

        } catch (NumberFormatException e) {
            showError(cfg.textUnknownFormat);
        }
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

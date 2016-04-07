package com.example.shuweizhao.wokba;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by shuweizhao on 4/6/16.
 */
public class PaymentFormFragment extends android.app.Fragment implements PaymentForm {

    Button saveButton;
    EditText cardNumber;
    EditText cvc;
    Spinner monthSpinner;
    Spinner yearSpinner;
    Spinner currencySpinner;
    private static final String CURRENCY_UNSPECIFIED = "Unspecified";
    String a;
    int keyDel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_form_fragment, container, false);

        this.saveButton = (Button) view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveForm(view);
            }
        });

        this.cardNumber = (EditText) view.findViewById(R.id.number);
        this.cvc = (EditText) view.findViewById(R.id.cvc);
        this.monthSpinner = (Spinner) view.findViewById(R.id.expMonth);
        this.yearSpinner = (Spinner) view.findViewById(R.id.expYear);
        this.currencySpinner = (Spinner) view.findViewById(R.id.currency);
        this.cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean flag = true;

                String eachBlock[] = cardNumber.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {
                    cardNumber.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((cardNumber.getText().length() + 1) % 5) == 0) {

                            if (cardNumber.getText().toString().split("-").length <= 3) {
                                cardNumber.setText(cardNumber.getText() + "-");
                                cardNumber.setSelection(cardNumber.getText().length());
                            }
                        }
                        a = cardNumber.getText().toString();
                    } else {
                        a = cardNumber.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    cardNumber.setText(a);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Insert char where needed.
                for (int i = 4; i < s.length(); i += 5) {
                    if (s.toString().charAt(i) != ' ') {
                        s.insert(i, " ");
                    }
                }
            }
        });
        return view;
    }

    @Override
    public String getCardNumber() {
        return this.cardNumber.getText().toString();
    }

    @Override
    public String getCvc() {
        return this.cvc.getText().toString();
    }

    @Override
    public Integer getExpMonth() {
        return getInteger(this.monthSpinner);
    }

    @Override
    public Integer getExpYear() {
        return getInteger(this.yearSpinner);
    }

    @Override
    public String getCurrency() {
        if (currencySpinner.getSelectedItemPosition() == 0) return null;
        String selected = (String) currencySpinner.getSelectedItem();
        if (selected.equals(CURRENCY_UNSPECIFIED))
            return null;
        else
            return selected.toLowerCase();
    }

    public void saveForm(View button) {
        ((BindCardActivity)getActivity()).saveCreditCard(this);
    }


    private Integer getInteger(Spinner spinner) {
        try {
            return Integer.parseInt(spinner.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

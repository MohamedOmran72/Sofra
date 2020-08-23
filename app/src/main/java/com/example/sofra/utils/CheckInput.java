package com.example.sofra.utils;

import android.widget.EditText;

import com.example.sofra.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckInput {
    private static Pattern p;
    private static Matcher m;

    /**
     * Called to check if entered email is valid
     *
     * @param email is EditText that contain input user Email
     * @return boolean
     */
    public static boolean isEmailValid(EditText email) {
        Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(email.getText().toString());
        if (m.matches()) {
            email.setError(null);
            return true;
        } else {
            email.setError(email.getContext().getText(R.string.invalid_email));
            email.requestFocus();
            return false;
        }
    }

    /**
     * Called to check if entered phone is valid
     *
     * @param phone is EditText that contain input user Phone number
     * @return boolean
     */
    public static boolean isPhoneSet(EditText phone) {
        p = Pattern.compile("[0-9]{11}");
        m = p.matcher(phone.getText().toString());
        if (m.matches()) {
            phone.setError(null);
            return true;
        } else {
            phone.setError(phone.getContext().getText(R.string.invalid_phone_number));
            return false;
        }
    }

    /**
     * Called to check if user enter required fields
     *
     * @param editTexts that need to check if user enter
     * @return boolean
     */
    public static boolean isEditTextSet(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().equals("")) {
                editText.setError(editText.getContext().getText(R.string.required_field));
                return false;
            }
        }
        return true;
    }
}

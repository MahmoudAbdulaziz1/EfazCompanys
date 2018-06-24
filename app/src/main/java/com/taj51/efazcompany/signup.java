package com.taj51.efazcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class signup extends AppCompatActivity {
    private TextView sup, act, sin, fbook;
    private EditText pswd, cpswd, mail;
    private Button next;
    private SharedPreferences save;
    private SharedPreferences visible;
    private boolean isVisible;
    private boolean isVisible2;
    private ImageButton visibleBtn;
    private ImageButton visibleBtn2;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        visible = getSharedPreferences("pvisible", MODE_PRIVATE);
        save = getSharedPreferences("signup", MODE_PRIVATE);
        String reEmail = save.getString("email", "");
        String rePassword = save.getString("pass", "");
        String cRePassword = save.getString("pass2", "");
        SharedPreferences.Editor editor = save.edit();
        editor.putString("email", "");
        editor.putString("pass", "");
        editor.putString("pass2", "");
        editor.commit();

        sup = (TextView) findViewById(R.id.sup);
        sin = (TextView) findViewById(R.id.sin);
        fbook = (TextView) findViewById(R.id.fboook);
        act = (TextView) findViewById(R.id.act);
        mail = (EditText) findViewById(R.id.mal);
        pswd = (EditText) findViewById(R.id.pswd);
        cpswd = (EditText) findViewById(R.id.cpswd);

        mail.setText(reEmail);
        pswd.setText(rePassword);
        cpswd.setText(cRePassword);
        fbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor2 = save.edit();
                editor2.putString("email", mail.getText().toString().trim());
                editor2.putString("pass", pswd.getText().toString().trim());
                editor2.putString("pass2", cpswd.getText().toString().trim());
                editor2.commit();
                Intent it = new Intent(signup.this, login.class);
                startActivity(it);
            }
        });
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor2 = save.edit();
                editor2.putString("email", mail.getText().toString().trim());
                editor2.putString("pass", pswd.getText().toString().trim());
                editor2.putString("pass2", cpswd.getText().toString().trim());
                editor2.commit();
                Intent it = new Intent(signup.this, login.class);
                startActivity(it);
            }
        });

        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signup.this, CompanyData.class);
                if (validateEmail() && validate(pswd) && confirmPassword(cpswd) && validateConfirm(pswd, cpswd)) {
                    it.putExtra("email", mail.getText().toString().trim());
                    it.putExtra("password", pswd.getText().toString().trim());
                    startActivity(it);
                }
            }
        });


        visibleBtn = (ImageButton) findViewById(R.id.sign_v_p);
        visibleBtn.setEnabled(false);
        visibleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible = visible.getBoolean("v1", false);

                if (isVisible) {
                    pswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    SharedPreferences.Editor vi1 = visible.edit();
                    vi1.putBoolean("v1", false);
                    vi1.commit();
                    visibleBtn.setImageDrawable(getResources().getDrawable(R.drawable.show));

                } else {
                    pswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    SharedPreferences.Editor vi2 = visible.edit();
                    vi2.putBoolean("v1", true);
                    vi2.commit();
                    visibleBtn.setImageDrawable(getResources().getDrawable(R.drawable.hide));

                }

            }
        });

        visibleBtn2 = (ImageButton) findViewById(R.id.sign_v_cp);
        visibleBtn2.setEnabled(false);
        visibleBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible2 = visible.getBoolean("v2", false);

                if (isVisible2) {
                    cpswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    SharedPreferences.Editor vi3 = visible.edit();
                    vi3.putBoolean("v2", false);
                    vi3.commit();
                    visibleBtn2.setImageDrawable(getResources().getDrawable(R.drawable.show));

                } else {
                    cpswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    SharedPreferences.Editor vi4 = visible.edit();
                    vi4.putBoolean("v2", true);
                    vi4.commit();
                    visibleBtn2.setImageDrawable(getResources().getDrawable(R.drawable.hide));

                }

            }
        });

        pswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pswd.getText().toString().trim().equals("")) {
                    visibleBtn.setEnabled(false);
                } else {
                    visibleBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cpswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cpswd.getText().toString().trim().equals("")) {
                    visibleBtn2.setEnabled(false);
                } else {
                    visibleBtn2.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private boolean validateEmail() {
        String email = mail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mail.setError(getResources().getString(R.string.email_valid));
            mail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validate(EditText editText) {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().length() >= 8) {
            return true; // returns true if field is not empty
        }
        editText.setError(getResources().getString(R.string.pass_valid));
        editText.requestFocus();
        return false;
    }

    private boolean confirmPassword(EditText editText) {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().length() >= 8) {
            return true; // returns true if field is not empty
        } else if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(getResources().getString(R.string.pass_confirm2));
            editText.requestFocus();
            return false;
        }
        editText.setError(getResources().getString(R.string.pass_valid));
        editText.requestFocus();
        return false;
    }

    private boolean validateConfirm(EditText editText, EditText editText2) {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().equals(editText2.getText().toString().trim())) {
            return true; // returns true if field is not empty
        }
        editText.setError(getResources().getString(R.string.pass_confirm));
        editText.requestFocus();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = save.edit();
        editor.putString("email", "");
        editor.putString("pass", "");
        editor.putString("pass2", "");
        editor.commit();
        SharedPreferences s = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = s.edit();
        editor2.putString("email", "");
        editor2.putString("pass", "");
        editor2.commit();
    }
}

package com.taj51.efazcompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CompanyDataActivity extends AppCompatActivity {


    private TextView next;
    private EditText companyName;
    private EditText userName;
    private EditText address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_comapny_data);


        companyName = (EditText) findViewById(R.id.organization_name);
        userName = (EditText) findViewById(R.id.usrusr);
        address = (EditText) findViewById(R.id.address);

        next = (TextView) findViewById(R.id.company_data_nxt);
        Intent intent = getIntent();
        final String mail = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CompanyDataActivity.this, CompanyContactsActivity.class);
                if (validate(companyName) && validate(userName) && validate(address)) {
                    it.putExtra("email", mail);
                    it.putExtra("password", password);
                    it.putExtra("company", companyName.getText().toString().trim());
                    it.putExtra("userName", userName.getText().toString().trim());
                    it.putExtra("address", address.getText().toString().trim());

                    startActivity(it);
                }

            }
        });
    }


    private boolean validate(EditText editText) {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().length() > 0) {
            return true; // returns true if field is not empty
        }
        editText.setError(getResources().getString(R.string.fill_data));
        editText.requestFocus();
        return false;
    }
}

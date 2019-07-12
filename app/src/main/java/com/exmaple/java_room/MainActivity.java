package com.exmaple.java_room;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.exmaple.java_room.db.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        TextView resultTextView = findViewById(R.id.result_text);

        viewModel.users.observe(this, users -> {
            if (users == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();

            for(User user : users) {
                sb.append(user.toString());
                sb.append("\n");
            }

            resultTextView.setText(sb.toString());
        });

        EditText nameEditText = findViewById(R.id.name_edit);

        findViewById(R.id.insert_button).setOnClickListener(view -> {
            viewModel.insert(new User(nameEditText.getText().toString()));
        });

    }
}

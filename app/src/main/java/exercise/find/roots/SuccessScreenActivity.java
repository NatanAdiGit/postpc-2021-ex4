package exercise.find.roots;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SuccessScreenActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_screen);

        TextView originalNumberTextView = findViewById(R.id.originalNumberTextView);
        TextView factorsTextView = findViewById(R.id.factorsTextView);
        TextView timeToComputeTextView = findViewById(R.id.timeToComputeTextView);
        Button okButton = findViewById(R.id.buttonOk);

        Intent initialCreateMe = getIntent();
        String originalNumber = Long.toString(initialCreateMe.getLongExtra("original_number", 0));
        String firstRoot =  Long.toString(initialCreateMe.getLongExtra("root1", 0));
        String secondRoot =  Long.toString(initialCreateMe.getLongExtra("root2", 0));
        String calcTime =  Long.toString(initialCreateMe.getLongExtra("calculation_time_in_seconds", -1));

        originalNumberTextView.setText(originalNumber);
        factorsTextView.setText(originalNumber + "=" + firstRoot + "*" + secondRoot);
        timeToComputeTextView.setText(calcTime);

        okButton.setOnClickListener(v -> {
            originalNumberTextView.setText("");
            factorsTextView.setText("");
            timeToComputeTextView.setText("");
            finish();
        });
//


    }
}
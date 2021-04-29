package exercise.find.roots;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class SuccessScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayAlert();
    }

    private void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setInverseBackgroundForced(true);

        Intent initialCreateMe = getIntent();
        long originalNumber = initialCreateMe.getLongExtra("original_number", 0);
        long firstRoot =  initialCreateMe.getLongExtra("root1", 0);
        long secondRoot =  initialCreateMe.getLongExtra("root2", 0);
        long calcTime =  initialCreateMe.getLongExtra("root2", 0);


        builder.setMessage(Long.toString(originalNumber) + '=' +
                Long.toString(firstRoot) + '*' + Long.toString(secondRoot) + '\n' +
                "calculation time: " + calcTime + "seconds" ).setCancelable(
                false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // code here
                        SuccessScreenActivity.this.finish();
                        dialog.cancel();
                    }
                }).setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SuccessScreenActivity.this.finish();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
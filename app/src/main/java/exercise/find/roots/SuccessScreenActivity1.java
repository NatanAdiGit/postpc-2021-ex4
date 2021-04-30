package exercise.find.roots;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class SuccessScreenActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog alertDialog = new AlertDialog.Builder(SuccessScreenActivity1.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();


//        displayAlert();
    }
//
//    private void displayAlert()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setInverseBackgroundForced(true);
//        AlertDialog alert = builder.create();
//        alert.show();
//
////        Intent initialCreateMe = getIntent();
////        long originalNumber = initialCreateMe.getLongExtra("original_number", 0);
////        long firstRoot =  initialCreateMe.getLongExtra("root1", 0);
////        long secondRoot =  initialCreateMe.getLongExtra("root2", 0);
////        long calcTime =  initialCreateMe.getLongExtra("calculation_time_in_seconds", -1);
//
//
//        builder.setMessage(originalNumber + '=' + firstRoot + '*' + secondRoot + '\n' +
//                "calculation time: " + calcTime + " seconds" ).setCancelable(
//                false).setPositiveButton("OK",
//                (dialog, id) -> {
//
//                    // code here
//                    SuccessScreenActivity.this.finish();
//                    dialog.cancel();
//                }).setNegativeButton("Close",
//                (dialog, id) -> {
//
//                    SuccessScreenActivity.this.finish();
//                    dialog.cancel();
//                });
//    }
}
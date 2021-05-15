package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

  public static final String ABORT_CAL_MESS = "calculation aborted after 20 seconds";

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForAbortCalculating = null;
  // TODO: add any other fields to the activity as you want
  
  /* marks if we are currently waiting for a result calculation. */
  private boolean waitingForResults = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);

    if (savedInstanceState != null) {

      String originalNumber = savedInstanceState.getString("original number");

      // at the beginning the button should be disabled
      buttonCalculateRoots.setEnabled(false);

      waitingForResults = savedInstanceState.getBoolean("is waiting for calculation");

      if (waitingForResults) {
        editTextUserInput.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
      } else {
        if (isPositiveLong(originalNumber))
          buttonCalculateRoots.setEnabled(true);

        editTextUserInput.setEnabled(true);
        progressBar.setVisibility(View.GONE);

      }
    }
    else {

      // set initial UI:
      progressBar.setVisibility(View.GONE); // hide progress
      editTextUserInput.setText(""); // cleanup text in edit-text
      editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
      buttonCalculateRoots.setEnabled(false); // set button as disabled (user can't click)
    }

    // set listener on the input written by the keyboard to the edit-text
    editTextUserInput.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      public void afterTextChanged(Editable s) {
        // text did change
        String newText = editTextUserInput.getText().toString();
        // todo: check conditions to decide if button should be enabled/disabled (see spec below)

        if (!isPositiveLong(newText)) {
          buttonCalculateRoots.setEnabled(false);
        }
        else {
          // if we are waiting for result unable the button.
          buttonCalculateRoots.setEnabled(!waitingForResults);
        }
      }
    });

    // set click-listener to the button
    buttonCalculateRoots.setOnClickListener(v -> {
      Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
      String userInputString = editTextUserInput.getText().toString();
      // todo: check that `userInputString` is a number. handle bad input. convert `userInputString` to long
      if (isPositiveLong(userInputString)) {
        long userInputLong = Long.parseLong(userInputString); // todo this should be the converted string from the user
        intentToOpenService.putExtra("number_for_service", userInputLong);
        startService(intentToOpenService);
        waitingForResults = true;
        // todo: set views states according to the spec (below)
        progressBar.setVisibility(View.VISIBLE);
        buttonCalculateRoots.setEnabled(false);
        editTextUserInput.setEnabled(false);
      }
      else {
        buttonCalculateRoots.setEnabled(false);
        progressBar.setVisibility(View.GONE);
      }
    });

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForSuccess = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots")) {
          Log.e("intent_is_null", "null");
          return;
        }
        // success finding roots!
        /*
         TODO: handle "roots-found" as defined in the spec (below).
          also:
           - the service found roots and passed them to you in the `incomingIntent`. extract them.
           - when creating an intent to open the new-activity, pass the roots as extras to the new-activity intent
             (see for example how did we pass an extra when starting the calculation-service)
         */
        waitingForResults = false;

        editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
        buttonCalculateRoots.setEnabled(true); // if we got roots then the number is valid number and the button should be enable.
        progressBar.setVisibility(View.GONE); // there is no calculation in the BG.

        Intent intentSuccessScreen = new Intent(MainActivity.this, SuccessScreenActivity.class);

        long serviceNumber = incomingIntent.getLongExtra("original_number", 0);
        long firstRoot =  incomingIntent.getLongExtra("root1", 0);
        long secondRoot =  incomingIntent.getLongExtra("root2", 0);
        long calcTime = incomingIntent.getLongExtra("calculation_time_in_seconds", 0);

        intentSuccessScreen.putExtra("original_number", serviceNumber);
        intentSuccessScreen.putExtra("root1", firstRoot);
        intentSuccessScreen.putExtra("root2", secondRoot);
        intentSuccessScreen.putExtra("calculation_time_in_seconds", calcTime);

        Log.e("success_in_receiving", "roots are received ");

        startActivity(intentSuccessScreen);
      }
    };

    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));

    // register a broadcast-receiver to handle action "stopped_calculations"
    broadcastReceiverForAbortCalculating = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        Log.e("the_stopped_received", "received");
        if (incomingIntent == null || !incomingIntent.getAction().equals("stopped_calculations")) {
          Log.e("intent_is_null", "null");
          return;
        }

        waitingForResults = false;

        editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
        buttonCalculateRoots.setEnabled(true); // if we got roots then the number is valid number and the button should be enable.
        progressBar.setVisibility(View.GONE); // there is no calculation in the BG.

        Toast.makeText(MainActivity.this, ABORT_CAL_MESS, Toast.LENGTH_SHORT).show();
      }
    };

    registerReceiver(broadcastReceiverForAbortCalculating, new IntentFilter("stopped_calculations"));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // todo: remove ALL broadcast receivers we registered earlier in onCreate().
    //  to remove a registered receiver, call method `this.unregisterReceiver(<receiver-to-remove>)`
    this.unregisterReceiver(broadcastReceiverForSuccess);
    this.unregisterReceiver(broadcastReceiverForAbortCalculating);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    // TODO: put relevant data into bundle as you see fit
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    outState.putBoolean("is waiting for calculation", waitingForResults);
    outState.putString("original number", editTextUserInput.getText().toString());
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    // TODO: load data from bundle and set screen state (see spec below)
    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);
    String originalNumber = savedInstanceState.getString("original number");
    // set the text to the last input
    editTextUserInput.setText(originalNumber);

    // at the beginning the button should be disabled
    buttonCalculateRoots.setEnabled(false);

    if (savedInstanceState.getBoolean("is waiting for calculation")) {
      editTextUserInput.setEnabled(false);
      progressBar.setVisibility(View.VISIBLE);
    }
    else {
      if (isPositiveLong(originalNumber))
        buttonCalculateRoots.setEnabled(true);

      editTextUserInput.setEnabled(true);
      progressBar.setVisibility(View.GONE);

    }
  }

  public static boolean isPositiveLong(String s) {
    return isPositiveLong(s,10);
  }

  public static boolean isPositiveLong(String s, int radix) {
    if(s.isEmpty()) return false;
    for(int i = 0; i < s.length(); i++) {
      if(Character.digit(s.charAt(i),radix) < 0) return false;
    }

    // check that the number is no longer then the max long.
    BigInteger bigInt = new BigInteger(s);
    return (bigInt.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0);
  }
}


/*

TODO:
the spec is:

upon launch, Activity starts out "clean":
* progress-bar is hidden
* "input" edit-text has no input and it is enabled
* "calculate roots" button is disabled

the button behavior is:
* when there is no valid-number as an input in the edit-text, button is disabled
* when we triggered a calculation and still didn't get any result, button is disabled
* otherwise (valid number && not calculating anything in the BG), button is enabled

the edit-text behavior is:
* when there is a calculation in the BG, edit-text is disabled (user can't input anything)
* otherwise (not calculating anything in the BG), edit-text is enabled (user can tap to open the keyboard and add input)

the progress behavior is:
* when there is a calculation in the BG, progress is showing
* otherwise (not calculating anything in the BG), progress is hidden

when "calculate roots" button is clicked:
* change states for the progress, edit-text and button as needed, so user can't interact with the screen

when calculation is complete successfully:
* change states for the progress, edit-text and button as needed, so the screen can accept new input
* open a new "success" screen showing the following data:
  - the original input number
  - 2 roots combining this number (e.g. if the input was 99 then you can show "99=9*11" or "99=3*33"
  - calculation time in seconds

when calculation is aborted as it took too much time:
* change states for the progress, edit-text and button as needed, so the screen can accept new input
* show a toast "calculation aborted after X seconds"


upon screen rotation (saveState && loadState) the new screen should show exactly the same state as the old screen. this means:
* edit-text shows the same input
* edit-text is disabled/enabled based on current "is waiting for calculation?" state
* progress is showing/hidden based on current "is waiting for calculation?" state
* button is enabled/disabled based on current "is waiting for calculation?" state && there is a valid number in the edit-text input


 */
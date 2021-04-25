package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;


public class CalculateRootsService extends IntentService {

  public static final int TWENTY_SECOND_IN_MILLI_SEC = 20000;

  public CalculateRootsService() {
    super("CalculateRootsService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null) return;
    long timeStartMs = System.currentTimeMillis();
    long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
    if (numberToCalculateRootsFor <= 0) {
      Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
      return;
    }

    /*
    TODO:
     calculate the roots.
     check the time (using `System.currentTimeMillis()`) and stop calculations if can't find an answer after 20 seconds
     upon success (found a root, or found that the input number is prime):
      send broadcast with action "found_roots" and with extras:
       - "original_number"(long)
       - "root1"(long)
       - "root2"(long)
     upon failure (giving up after 20 seconds without an answer):
      send broadcast with action "stopped_calculations" and with extras:
       - "original_number"(long)
       - "time_until_give_up_seconds"(long) the time we tried calculating

      examples:
       for input "33", roots are (3, 11)
       for input "30", roots can be (3, 10) or (2, 15) or other options
       for input "17", roots are (17, 1)
       for input "829851628752296034247307144300617649465159", after 20 seconds give up

     */
  }

  Pair<Long, Long> calculateFactors(long number) {
    long startTime = System.currentTimeMillis();
    // if the number is even then 2 is a factor
    if (number % 2 == 0)
      return new Pair<>((long)2 ,number / 2);
    // number is odd
    int firstFactor = 0;
    long numberSqr = (long) Math.sqrt(number);

    for(long i = 3; i <= numberSqr; i = i + 2) {
      // if we passed 20 second then return null
      if (System.currentTimeMillis() - startTime >= TWENTY_SECOND_IN_MILLI_SEC)
        return null;

      if (number % i == 0)
        return new Pair<>(i, number / i);
        }
    return new Pair<>((long)1, number);
  }
}
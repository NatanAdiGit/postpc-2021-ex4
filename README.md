1)  I pledge the highest level of ethical principles in support of academic excellence. 
    I ensure that all of my work reflects my own abilities and not those of someone else.
    
2) I would add a new extra to the intent of the CalculateRootsService with the tag
   "production_mode" that is a boolean var. If we are in production mode - an actual 
   activity component or any other file that contribute to the functionality, we will
   mark the extra as true and let the service run for 20 second max. If we are in test
   mode - we start the  CalculateRootsService from a test unit, we will start the service
   with a false extra and let the service run for 200 ms max. In the CalculateRootsService
   component we will handle the intent on the onHandleIntent method, and run the actual
   calculation according to the "production_mode" extra we can extract from the intent.
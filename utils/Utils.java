package utils;

/**
 * Created by jcozar on 28/01/16.
 */

public class Utils {
    public static int wait = 200;
    public static int fps = 15;
    public static int stopFps = 10;

    /*public static float minTemp = 450.0f;
    public static float maxTemp = 600.0f;
    public static float minPres = 135.0f;
    public static float maxPres = 165.0f;*/

    private static int timeSlowdown = 5;
    public static int getTimeSlowdown(){return timeSlowdown;}
    public static void incTimeSLowdown(){
        if(timeSlowdown<10)
            timeSlowdown++;
    }
    public static void decTimeSLowdown(){
        if(timeSlowdown>1)
            timeSlowdown--;
    }
}

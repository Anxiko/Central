package exe;

import frbs.FRBS;
import org.jfree.ui.RefineryUtilities;
import reactor.Behaviour;
import utils.Utils;
import window.*;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jcozar on 25/01/16.
 */
public class Main{

    private static FRBS frbs;
    private static Behaviour reactor;
    private static Interface window;
    private static Timer t;
    private static Long lastActivation = null;
    private static int lastSlowdown;

    public static void main(String args[]){
        Random r = new Random();
        r.setSeed(1234);

        reactor = new Behaviour(r);
        reactor.changeStatus(Integer.parseInt(args[0]));
        frbs = new FRBS();
        window = new Interface("Nuclear station", reactor, frbs);

        window.pack();
        window.setResizable(false);
        RefineryUtilities.centerFrameOnScreen( window );
        window.setVisible( true );

        startLifeCycle();
    }

    private static class LifeCycle extends TimerTask{

        @Override
        public void run() {
            frbs.control(reactor.getTemperature(),reactor.getPressure());
            reactor.update(frbs.getValveTemperature(),frbs.getValvePressure());
            window.update();
            lastActivation = System.currentTimeMillis();
            lastSlowdown = Utils.getTimeSlowdown();

        }
    }

    public static void stopLifeCycle(){
        t.cancel();
    }
    public static void startLifeCycle(){
        // In order to allow the needle animation ends, we do not allow to start until
        // 500*Utils.timeSlowdown long from the last pause.
        long timeRemaining = 0;
        if(lastActivation!=null) {
            long timeElapsed = System.currentTimeMillis() - lastActivation;
            timeRemaining = Utils.wait  * Utils.getTimeSlowdown() - timeElapsed;
            // If timeRemaining is below 0 it will starts at if it is 0
            if (timeRemaining < 0)
                timeRemaining = 0;
        }
        t = new Timer();
        t.schedule(new LifeCycle(), timeRemaining, Utils.wait * Utils.getTimeSlowdown());
    }
    public static void changeLifeCycleTime()
    {
        t.cancel();
        // In order to allow the needle animation ends, we do not allow to start until
        // 500*Utils.timeSlowdown long from the last pause.
        long timeRemaining = 0;
        if(lastActivation!=null) {
            long timeElapsed = System.currentTimeMillis() - lastActivation;
            timeRemaining = Utils.wait  * lastSlowdown - timeElapsed;
            // If timeRemaining is below 0 it will starts at if it is 0
            if (timeRemaining < 0)
                timeRemaining = 0;
        }
        t = new Timer();
        t.schedule(new LifeCycle(), timeRemaining, Utils.wait * Utils.getTimeSlowdown());
    }
}

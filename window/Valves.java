package window;

import frbs.FRBS;
import utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jcozar on 26/01/16.
 */
public class Valves extends JPanel {

    public final static int TEMPERATURE_SLIDER = 0;
    public final static int PRESSURE_SLIDER = 1;

    private JSlider tempSlider;
    private JSlider presSlider;

    private FRBS frbs;

    public Valves(FRBS frbs) {
        super(new GridLayout(1, 2));
        this.frbs = frbs;

        tempSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, frbs.getValveTemperature());
        tempSlider.setMajorTickSpacing(10);
        tempSlider.setMinorTickSpacing(5);
        tempSlider.setPaintTicks(true);
        tempSlider.setPaintLabels(true);
        tempSlider.setEnabled(false);
        presSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, frbs.getValvePressure());
        presSlider.setMajorTickSpacing(10);
        presSlider.setMinorTickSpacing(5);
        presSlider.setPaintTicks(true);
        presSlider.setPaintLabels(true);
        presSlider.setEnabled(false);
        add(tempSlider, BorderLayout.WEST);
        add(presSlider, BorderLayout.EAST);

        tempSlider.setPreferredSize(new Dimension(200,50));
        presSlider.setPreferredSize(new Dimension(200,50));
    }

    public void update(){
        new Thread(new Animation(frbs.getValveTemperature(),frbs.getValvePressure())).start();
    }

    private class Animation implements Runnable{

        private int newTemperature, newPressure;

        public Animation(int temperature, int pressure){
            newTemperature = temperature;
            newPressure = pressure;
        }

        @Override
        public void run() {
            int timeSlowdown = Utils.getTimeSlowdown();
            int waitDivided = (Utils.wait/(Utils.fps+Utils.stopFps));

            float prevTemp = tempSlider.getValue();
            float prevPres = presSlider.getValue();

            float stepTemp = (newTemperature - prevTemp)/Utils.fps;
            float stepPres = (newPressure - prevPres)/Utils.fps;

            for(int i=1;i<=Utils.fps;i++){
                tempSlider.setValue(Math.round(prevTemp+i*stepTemp));
                presSlider.setValue(Math.round(prevPres+i*stepPres));
                try {
                    Thread.sleep(waitDivided * timeSlowdown);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

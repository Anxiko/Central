package window;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.*;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import javax.swing.*;
import java.awt.*;

import reactor.Behaviour;
import utils.Utils;


/**
 * Created by jcozar on 26/01/16.
 */
public class Dials extends JPanel{

    private DefaultValueDataset tempDataset;
    private DefaultValueDataset presDataset;

    private Behaviour reactor;

    public Dials(Behaviour reactor){
        super(new GridLayout(1,2));
        this.reactor = reactor;

        tempDataset = new DefaultValueDataset(reactor.getTemperature());
        presDataset = new DefaultValueDataset(reactor.getPressure());

        DialPlot tempPlot = setUp("Temperature (ºC)",tempDataset,450f,600f);
        DialPlot presPlot = setUp("Pressure (at)",presDataset,135f,165f);

        JFreeChart tempChart = new JFreeChart(tempPlot);
        JFreeChart presChart = new JFreeChart(presPlot);
        ChartPanel tempChartPanel = new ChartPanel(tempChart);
        ChartPanel presChartPanel = new ChartPanel(presChart);

        tempChartPanel.setPreferredSize(new Dimension(300,300));
        presChartPanel.setPreferredSize(new Dimension(300,300));

        add(tempChartPanel);
        add(presChartPanel);
    }

    public void update(){
        new Thread(new Animation(reactor.getTemperature(),reactor.getPressure())).start();
    }

    private DialPlot setUp(String name, DefaultValueDataset ds, float min, float max){
        DialPlot dialplot = new DialPlot();

        dialplot.setDataset(ds);

        StandardDialFrame standarddialframe = new StandardDialFrame();
        standarddialframe.setBackgroundPaint(Color.lightGray);
        standarddialframe.setForegroundPaint(Color.darkGray);
        dialplot.setDialFrame(standarddialframe);

        GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
        DialBackground dialbackground = new DialBackground(gradientpaint);

        dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
        dialplot.setBackground(dialbackground);

        DialTextAnnotation dialtextannotation = new DialTextAnnotation(name);
        dialtextannotation.setFont(new Font("Dialog", 1, 14));
        dialtextannotation.setRadius(0.69999999999999996D);
        dialplot.addLayer(dialtextannotation);

        DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
        dialvalueindicator.setFont(new Font("Dialog", 0, 10));
        dialvalueindicator.setOutlinePaint(Color.darkGray);
        dialvalueindicator.setRadius(0.59999999999999998D);
        dialvalueindicator.setAngle(-90D);
        dialplot.addLayer(dialvalueindicator);

        StandardDialScale standarddialscale = new StandardDialScale(min, max, -150D, -240D, Math.round((max-min)/6), Math.round((max-min)/30));
        standarddialscale.setTickRadius(0.88D);
        standarddialscale.setTickLabelOffset(0.2D);
        standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
        dialplot.addScale(0, standarddialscale);

        float step = (max-min)/3;
        StandardDialRange standarddialrange1 = new StandardDialRange(min, min+step, Color.red);
        standarddialrange1.setInnerRadius(0.9D);
        standarddialrange1.setOuterRadius(0.9D);
        dialplot.addLayer(standarddialrange1);

        StandardDialRange standarddialrange2 = new StandardDialRange(max-step, max, Color.red);
        standarddialrange2.setInnerRadius(0.9D);
        standarddialrange2.setOuterRadius(0.9D);
        dialplot.addLayer(standarddialrange2);

        org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin(1);
        pin.setRadius(0.55000000000000004D);
        dialplot.addPointer(pin);

        org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer(0);
        dialplot.addPointer(pointer);

        DialCap dialcap = new DialCap();
        dialcap.setRadius(0.10000000000000001D);
        dialplot.setCap(dialcap);

        return dialplot;
    }


    private class Animation implements Runnable{

        private float newTemperature, newPressure;

        public Animation(float temperature, float pressure){
            newTemperature = temperature;
            newPressure = pressure;
        }

        @Override
        public void run() {
            int timeSlowdown = Utils.getTimeSlowdown();
            int waitDivided = (Utils.wait/(Utils.fps+Utils.stopFps));

            float prevTemp = tempDataset.getValue().floatValue();
            float prevPres = presDataset.getValue().floatValue();

            float stepTemp = (newTemperature - prevTemp)/Utils.fps;
            float stepPres = (newPressure - prevPres)/Utils.fps;

            for(int i=1;i<=Utils.fps;i++){
                tempDataset.setValue(prevTemp+i*stepTemp);
                presDataset.setValue(prevPres+i*stepPres);
                try {
                    Thread.sleep(waitDivided * timeSlowdown);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

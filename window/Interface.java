package window;

import exe.Main;
import frbs.FRBS;
import org.jfree.ui.ApplicationFrame;
import reactor.Behaviour;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by jcozar on 25/01/16.
 */
public class Interface extends ApplicationFrame{

    public final static int TEMPERATURE = 0;
    public final static int PRESSURE = 1;

    public boolean playMode = true;

    private Behaviour reactor;
    private FRBS frbs;

    private Dials topPanel;
    private Valves middlePanel;
    private InfoFRBS bottomPanel;


    private JLabel statusLabel;

    public Interface(String title, Behaviour reactor, FRBS frbs){
        super(title);
        this.reactor = reactor;
        this.frbs = frbs;
        setContentPane(createPanel(reactor.getStatus()));
    }

    private JPanel createPanel(int reactorStatus){
        JPanel mainPanel = new JPanel(new BorderLayout());
        //PAGE_START: The main controllers.
        mainPanel.add(buildStatusPanel(), BorderLayout.PAGE_START);

        //CENTER: The dials, valves and FRBS information.
        JPanel middleContainer = new JPanel(new BorderLayout());
        mainPanel.add(middleContainer,BorderLayout.CENTER);
        //Top level: two dial charts (humidity and pressure)
        topPanel = new Dials(reactor);
        middleContainer.add(topPanel, BorderLayout.NORTH);
        //Middle level: two controllers (sliders)
        middlePanel = new Valves(frbs);
        middleContainer.add(middlePanel, BorderLayout.CENTER);
        //Bottom level: FRBS information and management.
        bottomPanel = new InfoFRBS(frbs);
        middleContainer.add(bottomPanel, BorderLayout.SOUTH);

        //PAGE_END: General information about the app status.
        statusLabel = new JLabel(" Status: Behaviour of the nuclear station "+reactorStatus);
        mainPanel.add(statusLabel, BorderLayout.PAGE_END);

        return mainPanel;
    }

    private JPanel buildStatusPanel(){
        JPanel topContainer = new JPanel(new FlowLayout());
        topContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        topContainer.setPreferredSize(new Dimension(200,100));
        JLabel workingMode = new JLabel("Mode 1");
        workingMode.setEnabled(false);
        topContainer.add(workingMode);

        JButton btnPlay = new JButton(new ImageIcon("img/play.png"));
        btnPlay.setPreferredSize(new Dimension(32,32));
        btnPlay.setBorder(BorderFactory.createEmptyBorder());
        btnPlay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                playMode = !playMode;
                if(playMode) {
                    btnPlay.setIcon(new ImageIcon("img/play.png"));
                    exe.Main.startLifeCycle();
                }
                else {
                    btnPlay.setIcon(new ImageIcon("img/pause.png"));
                    exe.Main.stopLifeCycle();
                }
            }
        });
        JButton btnPlus = new JButton(new ImageIcon("img/more.png"));
        btnPlus.setPreferredSize(new Dimension(32,32));
        btnPlus.setBorder(BorderFactory.createEmptyBorder());
        JTextField timeSlowdownText = new JTextField(""+(11-Utils.getTimeSlowdown()),2);
        timeSlowdownText.setEditable(false);
        btnPlus.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.decTimeSLowdown();
                timeSlowdownText.setText(""+(11-Utils.getTimeSlowdown()));
                if(playMode)
                    Main.changeLifeCycleTime();
            }
        });


        JButton btnMinus = new JButton(new ImageIcon("img/minus.png"));
        btnMinus.setPreferredSize(new Dimension(32,32));
        btnMinus.setBorder(BorderFactory.createEmptyBorder());
        btnMinus.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.incTimeSLowdown();
                timeSlowdownText.setText(""+(11-Utils.getTimeSlowdown()));
                if(playMode)
                    Main.changeLifeCycleTime();
            }
        });


        topContainer.add(btnPlay);
        topContainer.add(btnMinus);
        topContainer.add(timeSlowdownText);
        topContainer.add(btnPlus);
        return topContainer;
    }

    public void update(){
        topPanel.update();
        middlePanel.update();
        bottomPanel.update();
    }
}

package window;

import frbs.FRBS;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jcozar on 26/01/16.
 */
public class InfoFRBS extends JPanel {

    private FRBS frbs;
    private JEditorPane infoArea;

    public InfoFRBS(FRBS frbs){
        super(new FlowLayout());
        this.frbs = frbs;

        infoArea = new JEditorPane("text/html", "");
        infoArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        add(scrollPane);
    }

    public void update(){
        ArrayList<String> info = frbs.getLastInferenceProcess();
        String s = "";
        for(String l : info){
            s += l;
        }
        infoArea.setText(s);
    }
}

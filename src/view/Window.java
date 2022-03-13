package view;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Panel panel;

    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("UHK FIM KPGR2 : " + this.getClass().getName());

        panel = new Panel();

        add(panel, BorderLayout.CENTER);
        pack();

        setLocationRelativeTo(null);

        // better at the end so that the focus is not taken by another component in a possibly more complex UI
        panel.setFocusable(true);
        panel.grabFocus(); // important for keyboard control
    }

    public Panel getPanel() {
        return panel;
    }

}

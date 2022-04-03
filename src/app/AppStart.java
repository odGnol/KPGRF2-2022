package app;

import control.Controller3D;
import view.Okno;

import javax.swing.*;

public class AppStart {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Okno okno = new Okno();
            new Controller3D(okno.getPanel());
            okno.setVisible(true);
        });
        // https://www.google.com/search?q=SwingUtilities.invokeLater
        // https://www.javamex.com/tutorials/threads/invokelater.shtml
    }

}

package main;

import javax.swing.SwingUtilities;
import presentador.MainPresenter;
import vista.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame vista = new MainFrame();
            new MainPresenter(vista);
            vista.setVisible(true);
        });
    }
}
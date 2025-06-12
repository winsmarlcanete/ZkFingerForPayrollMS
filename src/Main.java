package src;

import src.entity.Employee;
import src.screen.HomeScreen;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> {
            new HomeScreen().setVisible(true);
        });

        new Thread(() -> {
            ZkFinger zkFinger = new ZkFinger();
            String result = zkFinger.init();

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, result, "Verification Result", JOptionPane.INFORMATION_MESSAGE);
            });
        }).start();


    }
}

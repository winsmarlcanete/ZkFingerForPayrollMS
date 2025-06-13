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
            while (true) {
                ZkFinger zkFinger = new ZkFinger();
                String result = zkFinger.init(); // scan and log

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, result, "Verification Result", JOptionPane.INFORMATION_MESSAGE);
                });

                try {
                    Thread.sleep(2000); // delay before next scan cycle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }
}

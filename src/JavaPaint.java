package org.suai.java;


import javax.swing.*;
import org.suai.java.server.*;
import org.suai.java.client.*;

public class JavaPaint {
    public static void main(String[] args) {
        if (args.length != 0) {
            if (args[0].toUpperCase().equals("-S")&& args.length == 2) {
                System.out.println("SERVER IS RUNNING");
                Server server = new Server(Integer.parseInt(args[1]));
            } else if (args[0].toUpperCase().equals("-C") && args.length == 3) {
                System.out.println("CLIENT IS RUNNING");
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Client client = new Client(args[1], Integer.parseInt(args[2]));
                    }
                });
            } else {
                System.out.println("Arguments required: \nIf starting Server: -S PortNumber \nIf starting Client: -C IP-adress PortNumber");
            }
        } else {
            System.out.println("Arguments required: \nIf starting Server: -S PortNumber \nIf starting Client: -C IP-adress PortNumber");
        }
    }
}
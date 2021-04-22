package org.suai.java.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class SizeOptions extends JPanel{
    JButton size10;
    JButton size40;
    JButton size80;
    public int size = 10;

    public SizeOptions() {
        setLayout(new GridLayout(1, 3,0,0));
        setBackground(Color.lightGray);

        size10 = new JButton(createIcon("resources/size15.png"));
        size10.setPreferredSize(new Dimension(90, 90));
        size10.setBorderPainted(false);
        size10.setBackground(Color.lightGray);
        size10.setOpaque(true);
        size10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                size = 15;
            }
        });
        this.add(size10);

        size40 = new JButton(createIcon("resources/size40.png"));
        size40.setBorderPainted(false);
        size40.setPreferredSize(new Dimension(90, 90));
        size40.setBackground(Color.lightGray);
        size40.setOpaque(false);
        size40.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                size = 40;
            }
        });
        this.add(size40);

        size80 = new JButton(createIcon("resources/size80.png"));
        size80.setBorderPainted(false);
        size80.setPreferredSize(new Dimension(90, 90));
        size80.setBackground(Color.lightGray);
        size80.setOpaque(false);
        size80.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                size = 80;
            }
        });
        this.add(size80);
    }

    public int getBrushSize(){
        return size;
    }

    private ImageIcon createIcon(String path) {
        URL imgURL = SizeOptions.class.getResource(path);
        return new ImageIcon(imgURL);
    }
}

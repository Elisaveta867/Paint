package org.suai.java.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ColorOptions extends JPanel {
    Color mainColor = Color.black;
    Color orange = new Color(255, 121, 39);
    Color green = new Color(14, 209, 69);
    Color blue = new Color(0, 161, 243);
    Color indigo = new Color(63, 72, 204);
    Color purple = new Color(184, 61, 186);
    public ColorOptions() {
        setLayout(new GridLayout(2, 5,0,0));
        setBackground(Color.lightGray);

        JButton redButton = new JButton(createIcon("resources/red.png"));
        redButton.setPreferredSize(new Dimension(54, 54));
        redButton.setBorderPainted(false);
        redButton.setOpaque(false);
        redButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = Color.red;
            }
        });
        this.add(redButton);

        JButton orangeButton = new JButton(createIcon("resources/orange.png"));
        orangeButton.setPreferredSize(new Dimension(54, 54));
        orangeButton.setBorderPainted(false);
        orangeButton.setOpaque(false);
        orangeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = orange;
            }
        });
        this.add(orangeButton);

        JButton yellowButton = new JButton(createIcon("resources/yellow.png"));
        yellowButton.setPreferredSize(new Dimension(54, 54));
        yellowButton.setBorderPainted(false);
        yellowButton.setOpaque(false);
        yellowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = Color.yellow;
            }
        });
        this.add(yellowButton);

        JButton greenButton = new JButton(createIcon("resources/green.png"));
        greenButton.setPreferredSize(new Dimension(54, 54));
        greenButton.setBorderPainted(false);
        greenButton.setOpaque(false);
        greenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = green;
            }
        });
        this.add(greenButton);

        JButton blueButton = new JButton(createIcon("resources/blue.png"));
        blueButton.setPreferredSize(new Dimension(54, 54));
        blueButton.setBorderPainted(false);
        blueButton.setOpaque(false);
        blueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = blue;
            }
        });
        this.add(blueButton);

        JButton indigoButton = new JButton(createIcon("resources/indigo.png"));
        indigoButton.setPreferredSize(new Dimension(54, 54));
        indigoButton.setBorderPainted(false);
        indigoButton.setOpaque(false);
        indigoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = indigo;
            }
        });
        this.add(indigoButton);

        JButton purpleButton = new JButton(createIcon("resources/purple.png"));
        purpleButton.setPreferredSize(new Dimension(54, 54));
        purpleButton.setBorderPainted(false);
        purpleButton.setOpaque(false);
        purpleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = Color.magenta;
            }
        });
        this.add(purpleButton);

        JButton blackButton = new JButton(createIcon("resources/size40.png"));
        blackButton.setPreferredSize(new Dimension(54, 54));
        blackButton.setBorderPainted(false);
        blackButton.setOpaque(false);
        blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = Color.black;
            }
        });
        this.add(blackButton);

        JButton whiteButton = new JButton(createIcon("resources/white.png"));
        whiteButton.setPreferredSize(new Dimension(54, 54));
        whiteButton.setBorderPainted(false);
        whiteButton.setOpaque(false);
        whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = Color.white;
            }
        });
        this.add(whiteButton);

        JButton eraser = new JButton(createIcon("resources/erase.png"));
        eraser.setPreferredSize(new Dimension(54, 54));
        eraser.setBorderPainted(false);
        eraser.setOpaque(false);
        eraser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mainColor = Color.white;
            }
        });
        this.add(eraser);
    }

    public Color getColor(){
        return mainColor;
    }

    private ImageIcon createIcon(String path) {
        URL imgURL = ColorOptions.class.getResource(path);
        return new ImageIcon(imgURL);
    }
}

package org.suai.java.client;

import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.Random;

public class Client {
    boolean isConnected = false;
    String serverHost = null;
    int serverPort;
    Socket clientSocket;
    BufferedReader readSocket;
    BufferedWriter writeSocket;


    JFrame frame;
    JMenuBar topBar;
    JMenu sizeBar;
    JMenu colourBar;
    JMenu otherBar;
    JPanel menu;
    JPanel joinMenu;
    JLabel top;
    JLabel notFoundLabel;
    BoardPanel boardPanel; // отображение доски
    BufferedImage board = null; // доска
    Graphics2D graphics;
    String name = "jjjj";
    Color mainColor;
    Color greyColor = new Color(220, 220, 220);
    Color borderColor = new Color(180, 180, 180);
    SizeOptions sizeOptions;
    ColorOptions colorOptions;
    OtherOptions otherOptions;

    class BoardPanel extends JPanel implements Serializable {
        private static final long serialVersionUID = -109728024865681281L;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(board, 0, 0, this);
        }
    }

    class ReaderThread extends Thread {
        String message;
        String[] splitMessage;

        public ReaderThread() {
            this.start();
        }

        public void run() {
            try {
                try {
                    while (true) {
                        message = readSocket.readLine();
                        splitMessage = message.split(" ", 2);
                        if (splitMessage[0].equals("CREATE")) {
                            if (splitMessage[1].equals("OK")) {
                                board = new BufferedImage(810, 600, BufferedImage.TYPE_INT_RGB);
                                graphics = board.createGraphics();
                                graphics.setColor(Color.white);
                                graphics.fillRect(0, 0, 810, 600);
                                isConnected = true;
                                frame.remove(menu);
                                frame.remove(top);
                                frame.add(topBar);
                                frame.add(boardPanel);
                                frame.repaint();
                            } else if (splitMessage[1].equals("EXISTS")) {
                                frame.repaint();
                            }
                        } else if (splitMessage[0].equals("CONNECT")) {
                            if (splitMessage[1].equals("OK")) {
                                int[] rgbArray = new int[486000];
                                for (int i = 0; i < rgbArray.length; i++) {
                                    message = readSocket.readLine();
                                    rgbArray[i] = Integer.parseInt(message);
                                }
                                board = new BufferedImage(810, 600, BufferedImage.TYPE_INT_RGB);
                                board.setRGB(0, 0, 810, 600, rgbArray, 0, 810);
                                graphics = board.createGraphics();
                                isConnected = true;
                                frame.remove(joinMenu);
                                frame.remove(top);
                                frame.add(topBar);
                                frame.add(boardPanel);
                                frame.repaint();
                            } else if (splitMessage[1].equals("NOT FOUND")) {
                                joinMenu.add(notFoundLabel);
                                frame.repaint();
                            }
                        } else {
                            splitMessage = message.split(" ", 4);
                            int color = Integer.parseInt(splitMessage[0]);
                            int coordX = Integer.parseInt(splitMessage[1]);
                            int coordY = Integer.parseInt(splitMessage[2]);
                            int size = Integer.parseInt(splitMessage[3]);

                            graphics.setColor(new Color(color));
                            graphics.fillOval(coordX, coordY, size, size);
                            boardPanel.repaint();
                        }
                    }
                } catch (IOException err) {
                    System.out.println(err.toString());
                    readSocket.close();
                    writeSocket.close();
                }
            } catch (IOException err) {
                System.out.println(err.toString());
            }
        }
    }

    public Client(String serverHost, int serverPort) {
        try {
            try {
                this.serverHost = serverHost;
                this.serverPort = serverPort;
                clientSocket = new Socket(serverHost, serverPort);
                readSocket = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writeSocket = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                new ReaderThread();
            } catch (IOException err) {
                System.out.println(err.toString());
                readSocket.close();
                writeSocket.close();
            }
        } catch (IOException err) {
            System.out.println(err.toString());
        }

        frame = new JFrame("NotPaint");
        frame.setSize(810, 640);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);

        boardPanel = new BoardPanel();
        boardPanel.setBounds(0, 40, 810, 600);
        boardPanel.setOpaque(true);
        mainColor = Color.black;

        top = new JLabel("WELCOME TO THE MULTI-USER PAINT");
        top.setBounds(0, 0, 810, 40);
        top.setBackground(greyColor);
        top.setFont(new Font( Font.SANS_SERIF, Font.PLAIN, 20));
        top.setHorizontalAlignment(JLabel.CENTER);
        top.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        frame.add(top);

        menu = new JPanel();
        menu.setBounds(0, 40, 810, 600);
        menu.setBackground(Color.white);
        menu.setLayout(null);
        frame.add(menu);

        joinMenu = new JPanel();
        joinMenu.setBounds(0, 40, 810, 600);
        joinMenu.setBackground(Color.white);
        joinMenu.setLayout(null);

        ImageIcon notFound = createIcon("resources/notFound.png");
        notFoundLabel = new JLabel(notFound);
        notFoundLabel.setBounds(170, 405, 470, 100);


        JButton create = new JButton(createIcon("resources/create.png"));
        create.setBounds(150, 80, 510, 180);
        create.setBorderPainted(false);
        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Random random = new Random();
                int num = random.nextInt(999999);
                if (num < 100000) num += 100000;
                name = "" + num;
                otherOptions.setDeskName(name);
                if (name.equals("")) {
                    frame.repaint();
                    return;
                }

                if (menu.isAncestorOf(notFoundLabel)) {
                    menu.remove(notFoundLabel);
                    frame.repaint();
                }

                try {
                    try {
                        writeSocket.write("CREATE " + name + "\n");
                        writeSocket.flush();
                    } catch (IOException err) {
                        System.out.println(err.toString());
                        readSocket.close();
                        writeSocket.close();
                    }
                } catch (IOException err) {
                    System.out.println(err.toString());
                }
            }
        });
        menu.add(create);


        JButton join = new JButton(createIcon("resources/join.png"));
        join.setBounds(150, 300, 510, 180);
        join.setBorderPainted(false);
        join.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.remove(menu);
                frame.add(joinMenu);
                frame.repaint();
            }
        });
        menu.add(join);


        JTextField textField = new JTextField();
        textField.setBounds(170, 265, 470, 100);
        textField.setFont(new Font("myBigFont", Font.BOLD, 35));
        textField.setBorder(BorderFactory.createLineBorder(greyColor, 3));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText("");
        joinMenu.add(textField);

        JButton joinB = new JButton(createIcon("resources/join.png"));
        joinB.setBounds(150, 60, 510, 180);
        joinB.setBorderPainted(false);
        joinB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                name = textField.getText();
                otherOptions.setDeskName(name);

                if (name.equals("")) {
                    System.out.println();
                    frame.repaint();
                    return;
                }
                if (menu.isAncestorOf(notFoundLabel)) {
                    menu.remove(notFoundLabel);
                    frame.repaint();
                }
                try {
                    try {
                        writeSocket.write("CONNECT " + name + "\n");
                        writeSocket.flush();
                    } catch (IOException err) {
                        System.out.println(err.toString());
                        readSocket.close();
                        writeSocket.close();
                    }
                } catch (IOException err) {
                    System.out.println(err.toString());
                }
            }
        });
        joinMenu.add(joinB);

        sizeBar = new JMenu();
        sizeBar.setBounds(0, 0, 270, 40);
        sizeBar.setIcon(createIcon("resources/size.png"));
        sizeBar.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        sizeBar.setFocusPainted(false);
        sizeOptions = new SizeOptions();
        sizeBar.add(sizeOptions);

        colourBar = new JMenu();
        colourBar.setBounds(270, 0, 270, 40);
        colourBar.setIcon(createIcon("resources/colour.png"));
        colourBar.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        colourBar.setFocusPainted(false);
        colorOptions = new ColorOptions();
        colourBar.add(colorOptions);

        otherBar = new JMenu("OTHER");
        otherBar.setBounds(540, 0, 270, 40);
        otherBar.setIcon(createIcon("resources/other.png"));
        otherBar.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        otherBar.setFocusPainted(false);
        otherOptions = new OtherOptions(name);
        otherOptions.setDeskName(name);
        otherBar.add(otherOptions);

        topBar = new JMenuBar();
        topBar.setBounds(0, 0, 810, 40);
        topBar.setBackground(greyColor);
        topBar.add(sizeBar);
        topBar.add(colourBar);
        topBar.add(otherBar);

        boardPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                try {
                    try {
                        int size = sizeOptions.getBrushSize();
                        mainColor = colorOptions.getColor();
                        String message = mainColor.getRGB() + " " + (e.getX() - size / 2) + " " + (e.getY() - size / 2)
                                + " " + size;
                        writeSocket.write(message + "\n");
                        writeSocket.flush();
                    } catch (IOException err) {
                        System.out.println(err.toString());
                        readSocket.close();
                        writeSocket.close();
                    }
                } catch (IOException err) {
                    System.out.println(err.toString());
                }

            }
        });

        boardPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    try {
                        int size = sizeOptions.getBrushSize();
                        mainColor = colorOptions.getColor();
                        String message = mainColor.getRGB() + " " + (e.getX() - size / 2) + " " + (e.getY() - size / 2)
                                + " " + size;
                        writeSocket.write(message + "\n");
                        writeSocket.flush();
                    } catch (IOException err) {
                        System.out.println(err.toString());
                        readSocket.close();
                        writeSocket.close();
                    }
                } catch (IOException err) {
                    System.out.println(err.toString());
                }
            }
        });
    }

    private class OtherOptions extends JPanel{
        String deskName = "ggg";
        JLabel ID;

        public OtherOptions(String n){
            setLayout(new GridLayout(3, 1,0,1));

            deskName = n;
            ID = new JLabel();
            ID.setPreferredSize(new Dimension(260, 40));
            ID.setBackground(greyColor);
            ID.setFont(new Font( Font.SANS_SERIF, Font.PLAIN, 20));
            ID.setHorizontalAlignment(JLabel.CENTER);
            this.add(ID);

            JButton clearButton = new JButton("CLEAR");
            clearButton.setPreferredSize(new Dimension(260, 40));
            clearButton.setFocusPainted(false);
            clearButton.setBorderPainted(false);
            clearButton.setBackground(greyColor);
            clearButton.setFont(new Font( Font.SANS_SERIF, Font.PLAIN, 20));
            clearButton.setHorizontalAlignment(JLabel.CENTER);
            clearButton.setOpaque(false);
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    board = new BufferedImage(810, 600, BufferedImage.TYPE_INT_RGB);
                    graphics = board.createGraphics();
                    graphics.setColor(Color.white);
                    graphics.fillRect(0, 0, 810, 600);
                    frame.repaint();
                    try {
                        try {
                            String message = Color.white.getRGB() + " " + -200 + " " + -300 + " " + 1500;
                            writeSocket.write(message + "\n");
                            writeSocket.flush();
                        } catch (IOException err) {
                            System.out.println(err.toString());
                            readSocket.close();
                            writeSocket.close();
                        }
                    } catch (IOException err) {
                        System.out.println(err.toString());
                    }
                }
            });
            this.add(clearButton);

            JButton exitButton = new JButton("EXIT");
            exitButton.setPreferredSize(new Dimension(260, 40));
            exitButton.setBackground(greyColor);
            exitButton.setFocusPainted(false);
            exitButton.setBorderPainted(false);
            exitButton.setFont(new Font( Font.SANS_SERIF, Font.PLAIN, 20));
            exitButton.setHorizontalAlignment(JLabel.CENTER);
            exitButton.setOpaque(false);
            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    frame.remove(boardPanel);
                    frame.add(menu);
                    frame.remove(topBar);
                    frame.add(top);
                    frame.repaint();
                }
            });
            this.add(exitButton);
        }

        public void setDeskName(String n){
            deskName = n;
            ID.setText("DESK ID: " + deskName);
        }
    }

    private ImageIcon createIcon(String path) {
        URL imgURL = Client.class.getResource(path);
        return new ImageIcon(imgURL);
    }
}
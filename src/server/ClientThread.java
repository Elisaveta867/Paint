package org.suai.java.server;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import static org.suai.java.server.Server.boards;
import static org.suai.java.server.Server.clients;

public class ClientThread extends Thread {

        private Socket clientSocket = null;
        private BufferedReader bufferedReader = null;
        private BufferedWriter bufferedWriter = null;
        private Object consoleSynch;
        public static String boardName = null;
        public static String command ="";

        Graphics2D graphics = null;

        public ClientThread(Socket clientSocket) {
            consoleSynch = new Object();
            this.clientSocket = clientSocket;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            } catch (IOException err) {
                synchronized (consoleSynch) {
                    System.out.println(err.getMessage());
                }
            }
        }

        public void run() {
            synchronized (consoleSynch) {
                System.out.println("New guest connected");
                synchronized (clients) {
                    System.out.println("Currently having " + clients.size() + " user(s) on server \n");
                }
            }
            try {
                try {
                    while (true) {
                        String message = bufferedReader.readLine();
                        String[] splitMessage = message.split(" ", 2);
                        if (splitMessage[0].equals("CREATE")) {
                            boolean сontains;
                            synchronized (boards) {
                                сontains = boards.containsKey(splitMessage[1]);
                            }
                            if (сontains) {
                                synchronized (this) {
                                    bufferedWriter.write("CREATE EXISTS\n");
                                    bufferedWriter.flush();
                                }
                            } else {
                                synchronized (this) {
                                    bufferedWriter.write("CREATE OK\n");
                                    bufferedWriter.flush();
                                }
                                String boardNameOld = boardName;

                                boardName = splitMessage[1];
                                synchronized (boards) {
                                    boards.put(boardName, new BufferedImage(810, 600, BufferedImage.TYPE_INT_RGB));
                                    graphics = boards.get(boardName).createGraphics();
                                }
                                synchronized (boards.get(boardName)) {
                                    graphics.setColor(Color.white);
                                    graphics.fillRect(0, 0, 810, 600);
                                }
                                synchronized (consoleSynch) {
                                    System.out.println("New desk \"" + boardName + "\" created");
                                    synchronized (boards) {
                                        System.out.println("Currently having  " + boards.size() + " desks\n");
                                    }
                                }

                                checkBoards(boardNameOld);
                            }
                        } else if (splitMessage[0].equals("CONNECT")) {
                            boolean сontains;
                            synchronized (boards) {
                                сontains = boards.containsKey(splitMessage[1]);
                            }
                            if (сontains) {
                                synchronized (this) {
                                    bufferedWriter.write("CONNECT OK\n");
                                    bufferedWriter.flush();
                                }
                                String boardNameOld = boardName;

                                boardName = splitMessage[1];
                                synchronized (boards.get(boardName)) {
                                    graphics = boards.get(boardName).createGraphics();
                                }
                                int[] rgbArray = new int[486000];
                                synchronized (boards.get(boardName)) {
                                    boards.get(boardName).getRGB(0, 0, 810, 600, rgbArray, 0, 810);
                                }
                                synchronized (this) {
                                    for (int i = 0; i < rgbArray.length; i++) {
                                        bufferedWriter.write(rgbArray[i] + "\n");
                                        bufferedWriter.flush();
                                    }
                                }
                                checkBoards(boardNameOld);
                            } else {
                                synchronized (this) {
                                    bufferedWriter.write("CONNECT NOT FOUND\n");
                                    bufferedWriter.flush();
                                }
                            }
                        } else if (boardName != null) {
                            splitMessage = message.split(" ", 4);
                            int color = Integer.parseInt(splitMessage[0]);
                            int coordX = Integer.parseInt(splitMessage[1]);
                            int coordY = Integer.parseInt(splitMessage[2]);
                            int size = Integer.parseInt(splitMessage[3]);
                            synchronized (boards.get(boardName)) {
                                graphics.setColor(new Color(color));
                                graphics.fillOval(coordX, coordY, size, size);
                            }

                            //same board
                            synchronized (clients) {
                                for (ClientThread iClient : clients) {
                                    if (iClient.boardName != null && iClient.boardName.equals(boardName)) {
                                        synchronized (iClient) {
                                            iClient.bufferedWriter.write(message + "\n");
                                            iClient.bufferedWriter.flush();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    clientSocket.close();
                    bufferedReader.close();
                    bufferedWriter.close();
                    synchronized (clients) {
                        clients.remove(this);
                        synchronized (consoleSynch) {
                            System.out.println("User left");
                            System.out.println("Currently having " + clients.size() + " users on server");
                        }
                    }
                    checkBoards(boardName);
                }
            } catch (Exception err) {
                synchronized (consoleSynch) {
                    System.out.println(err.toString() + "\n");
                }
            }
        }

        void checkBoards(String boardName) {
        if (boardName == null) {
            return;
        }
        for (ClientThread iClient : clients) {
            synchronized (iClient) {
                if (iClient.boardName != null && iClient.boardName.equals(boardName)) {
                    break;
                }
            }
        }
        /*
        if (!boardUsed) {
            synchronized (boards) {
                boards.remove(boardName);
                synchronized (consoleSynch) {
                    System.out.println("desk \"" + boardName + "\" deleted");
                    System.out.println("Currently having " + boards.size() + " desks \n");
                }
            }
        }
         */
    }

}

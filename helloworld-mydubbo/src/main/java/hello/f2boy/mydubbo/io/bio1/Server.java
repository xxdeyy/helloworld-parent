package hello.f2boy.mydubbo.io.bio1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static boolean isShutDown = false;

    private static List<Socket> sockets = new ArrayList<>();

    private static ServerSocket serverSocket;

    private static void start() {

        int backlog = 1;
        try {
            serverSocket = new ServerSocket(1234, backlog);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {

            if (isShutDown) {
                for (Socket socket : sockets) {
                    if (socket == null || socket.isClosed()) continue;
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            }

            try {
                Socket socket = serverSocket.accept();
                sockets.add(socket);

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                new Thread(() -> {
                    InetSocketAddress remoteAddress = ((InetSocketAddress) socket.getRemoteSocketAddress());
                    String client = remoteAddress.getHostName() + ":" + remoteAddress.getPort();

                    try {
                        String message = "";
                        int c;
                        while ((c = is.read()) != -1) {
                            if (c == '\n') {
                                System.out.println(client + " send: " + message);
                                os.write(("your message is: " + message + "\n").getBytes());
                                if (message.equals("bye")) {
                                    socket.close();
                                    break;
                                }
                                message = "";
                            } else {
                                message += (char) c;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void stop() throws IOException {
        serverSocket.close();
        isShutDown = true;
    }

    public static void main(String[] args) throws Exception {

        new Thread(Server::start).start();

        Thread.sleep(500000L);
        Server.stop();
    }

}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    public static void main(String[] args) throws IOException {


        JFrame jFrame = new JFrame("Server");
        jFrame.setSize(400, 400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTitle = new JLabel("File Receiver");
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(1234);
       // InetAddress localhost = InetAddress.getLocalHost();
       // String ipAddress = localhost.getHostAddress();

       // ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Server_Thread serverThread = new Server_Thread(socket, jPanel, jFrame);
                serverThread.start();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }
}
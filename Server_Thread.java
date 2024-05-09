import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Server_Thread extends Thread {
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    private static int fileId = 0;
    Socket socket;
    JPanel jPanel;
    JFrame jFrame;

    public Server_Thread(Socket socket, JPanel jPanel, JFrame jFrame) throws IOException {
        this.socket = socket;
        this.jPanel = jPanel;
        this.jFrame = jFrame;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int fileNameLength = dataInputStream.readInt();

            if (fileNameLength > 0) {
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                String fileName = new String(fileNameBytes);

                int fileContentLength = dataInputStream.readInt();

                if (fileContentLength > 0) {
                    byte[] fileContentBytes = new byte[fileContentLength];
                    dataInputStream.readFully(fileContentBytes, 0, fileContentLength);
                    System.out.println("Received file: " + fileName );
                    JPanel jpFileRow = new JPanel();
                    jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                    JLabel jlFileName = new JLabel(fileName);
                    jlFileName.setFont(new Font("Arial", Font.BOLD, 25));

                    jpFileRow.setName(String.valueOf(fileId));

                    jpFileRow.addMouseListener(getMyMouseListener());

                    jpFileRow.add(jlFileName);
                    jPanel.add(jpFileRow);
                    jFrame.validate();

                    myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                    fileId++;

                } else {
                    System.out.println("File content length is 0 or less: " + fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                int clickedFileId = Integer.parseInt(jPanel.getName());
                System.out.println(clickedFileId);
                for (MyFile myFile : myFiles) {
                    if (myFile.getId() == clickedFileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), clickedFileId); // Pass fileId here
                        jfPreview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
    }

    public JFrame createFrame(String fileName, byte[] fileData, int fileId) { // Receive fileId here

        JFrame jFrame = new JFrame("File Downloader");
        jFrame.setSize(600,450);
        jFrame.setLocationRelativeTo(null);
         

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setBackground(Color.WHITE);

        JLabel jlTitle = new JLabel("File Downloader");
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(30, 0, 10, 0));


        JLabel jlPrompt = new JLabel("Are you sure you want to download " + fileName); // Use fileId here
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jlPrompt.setBorder(new EmptyBorder(50, 0, 40, 0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        JButton jbYes = new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(150, 75));
        jbYes.setFont(new Font("Arial", Font.BOLD, 20));
        jbYes.setBackground(new Color(220, 20, 60)); 
        jbYes.setForeground(Color.WHITE);
        jbYes.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));


        JButton jbNo = new JButton("No");
        jbNo.setPreferredSize(new Dimension(150, 75));
        jbNo.setFont(new Font("Arial", Font.BOLD, 20));
        jbNo.setBackground(new Color(173, 216, 230)); 
        jbNo.setForeground(Color.WHITE);
        jbNo.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 250), 2));


        JLabel jlFileContent = new JLabel();
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel jpButton = new JPanel();
        jpButton.setBackground(Color.WHITE); 
        jpButton.setBorder(new EmptyBorder(20, 0, 10, 0));
        jpButton.add(jbYes);
        jpButton.add(jbNo);


        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownload = new File(fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    jFrame.dispose();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });


        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });


        jPanel.add(jlTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jlFileContent);
        jPanel.add(jpButton);


        jFrame.add(jPanel);


        return jFrame;
    }

    public String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return "No extension found";
        }
    }
}

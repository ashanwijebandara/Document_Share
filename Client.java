import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Client {
    public static void main(String[] args) {
        final File[] fileToSend = new File[1];
        

        JFrame jFrame = new JFrame("Client");
        jFrame.setSize(650,450);
        //jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout(10, 10));
        jFrame.setLocationRelativeTo(null);
        
        JPanel imagePanel = new JPanel(new BorderLayout());
       
        
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("example2.jpg"); 
        Image image = imageIcon.getImage(); 
        int width = 250; 
        int height = 450;
        //int height = (int) (width * (double) image.getHeight(null) / image.getWidth(null));
        Image newimg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg); 
        imageLabel.setIcon(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        jFrame.add(imageLabel, BorderLayout.WEST);
        jFrame.setBackground(Color.WHITE); 

        imagePanel.setBackground(Color.WHITE); 
        

        // Main panel for other components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE); 
        
        jFrame.add(mainPanel, BorderLayout.CENTER);
        
       
        JLabel jlTitle = new JLabel("File Sender");
        jlTitle.setFont(new Font("Arial",Font.BOLD,28));
        jlTitle.setBorder(new EmptyBorder(30,0,10,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("Choose a file to send  ");
        jlFileName.setFont(new Font("Arial",Font.BOLD,20));
        jlFileName.setBorder(new EmptyBorder(50,0,0,0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75,0,10,0));
        jpButton.setBackground(Color.WHITE); 
        
        JButton jbSendFile = new JButton("Send File");
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));
        jbSendFile.setBackground(new Color(220, 20, 60)); 
        jbSendFile.setForeground(Color.WHITE);
        jbSendFile.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));

        
        JButton jbChooseFile = new JButton("Choose File");
        jbChooseFile.setPreferredSize(new Dimension(150, 75));
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));
        jbChooseFile.setBackground(new Color(173, 216, 230)); 
        jbChooseFile.setForeground(Color.WHITE);
        jbChooseFile.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 250), 2));
      
        
        
        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);

        
        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to send");

                if(jFileChooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION){
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    String fileNameText = "<html><div style='margin-left: 50px;'>The file you want to send is:<br><span style='font-size: 14px;'>" + fileToSend[0].getName() + "</span></div></html>";
                    jlFileName.setText(fileNameText);
                }
            }
        });
        
       

        
        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileToSend[0]==null){
                    jlFileName.setText("Please choose a file");
                    jlFileName.setForeground(Color.RED);
                }else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost",1234);
                        //Socket socket = new Socket("192.168.0.143",1234);
                        String fileName = fileToSend[0].getName();
                        byte[] fileContentBytes = new byte[(int)fileToSend[0].length()];

                        Client_Thread clientThread = new Client_Thread(socket,fileName, fileContentBytes,fileInputStream);
                        clientThread.start();

                    }
                    catch (IOException error){
                        error.printStackTrace();
                    }
                }
            }
        });

        //jFrame.add(jlTitle);
        //jFrame.add(jlFileName);
        //jFrame.add(jpButton);
        mainPanel.add(jlTitle);
        mainPanel.add(jlFileName);
        mainPanel.add(jpButton);
        jFrame.setVisible(true);

    }
}

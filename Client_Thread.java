import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client_Thread extends Thread{


    Socket socket;
    FileInputStream fileInputStream;
    String fileName;
    byte[] fileContentBytes;
    public Client_Thread(Socket socket,String fileName,byte[] fileContentBytes, FileInputStream fileInputStream) throws IOException{
        this.socket = socket;
        this.fileName = fileName;
        this.fileContentBytes=fileContentBytes;
        this.fileInputStream = fileInputStream;

    }

    @Override
    public void run(){
        try{
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] fileNameBytes = fileName.getBytes();
            fileInputStream.read(fileContentBytes);
            dataOutputStream.writeInt(fileNameBytes.length);
            dataOutputStream.write(fileNameBytes);

            dataOutputStream.writeInt(fileContentBytes.length);
            dataOutputStream.write(fileContentBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

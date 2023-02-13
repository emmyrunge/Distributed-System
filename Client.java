import java.io.*;
import java.net.*;

class Client
{

    public static void main(String args[])
            throws Exception
    {

        // Create client socket
        Socket clientSocket = new Socket("localhost", 4289);

        DataInputStream inStreamFromMasterToClient
                = new DataInputStream(clientSocket.getInputStream());

        // to send data to the server AKA Master
        DataOutputStream dataOutputStreamSendToMaster
                = new DataOutputStream(
                clientSocket.getOutputStream());

        // to read data coming from the server
        BufferedReader bufferedReaderReadFromMaster
                = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));

        // to read data from the keyboard
        BufferedReader bufferedReaderReadFromKeyboard
                = new BufferedReader(
                new InputStreamReader(System.in));

        String str, str1;

        System.out.println(bufferedReaderReadFromMaster.readLine());
        // repeat as long as exit
        // is not typed at client
        while (!(str = bufferedReaderReadFromKeyboard.readLine()).equals("exit"))
        {
            // send to the server AKA master
            dataOutputStreamSendToMaster.writeBytes(str + "\n");

            System.out.println(bufferedReaderReadFromMaster.readLine());

            System.out.println(inStreamFromMasterToClient.readUTF());
        }

        // close connection.
        dataOutputStreamSendToMaster.close();
        bufferedReaderReadFromMaster.close();
        bufferedReaderReadFromKeyboard.close();
        clientSocket.close();
    }
}
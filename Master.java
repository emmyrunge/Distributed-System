import java.io.*;
import java.net.*;


class Master
{

    public static boolean gateA = false;
    public static boolean gateB = false;


    public static void main(String args[]) throws Exception
    {

        // Create server Socket AKA Master socket
        ServerSocket masterSocket = new ServerSocket(4289);
        System.out.println("Waiting for connection to Client");

        // connect it to client socket
        Socket connectToClientSocket = masterSocket.accept();
        System.out.println("Connection to Client established");
        System.out.println();

        DataOutputStream dataOutputStreamSendToClient
                = new DataOutputStream(connectToClientSocket.getOutputStream());

        // to send data to the client
        PrintStream printStreamSendToClient
                = new PrintStream(connectToClientSocket.getOutputStream());

        // to read data coming from the client
        BufferedReader bufferedReaderReadFromClient
                = new BufferedReader(
                new InputStreamReader(
                        connectToClientSocket.getInputStream()));

        // to read data from the keyboard in Client
        BufferedReader bufferedReaderReadFromKeyboard
                = new BufferedReader(
                new InputStreamReader(System.in));

        printStreamSendToClient.println("Connected to Master");

        // Master executes continuously
        while (true)
        {

            String job;

            //Establish connection to SlaveA
            ServerSocket SlaveASocket = new ServerSocket(5693);
            System.out.println("Master waiting for connection to Slave A");

            Socket connectToSlaveASocket = SlaveASocket.accept();
            System.out.println("Connection to SlaveA accepted");
            System.out.println();

            //To send data to SlaveA
            PrintStream printStreamSendToSlaveA
                    = new PrintStream(connectToSlaveASocket.getOutputStream());

            // to read data coming from SlaveA
            BufferedReader bufferedReaderReadFromSlaveA
                    = new BufferedReader(
                    new InputStreamReader(connectToSlaveASocket.getInputStream()));

            DataOutputStream dataOutputStreamSendToSlaveA
                    = new DataOutputStream(connectToSlaveASocket.getOutputStream());

            DataInputStream inStreamFromSlaveA
                    = new DataInputStream(connectToSlaveASocket.getInputStream());

            //Let SlaveA know that Master is connected
            printStreamSendToSlaveA.println("Master is connected to Slave A");


            //Establish connection to SlaveB
            ServerSocket slaveBSocket = new ServerSocket(4487);
            System.out.println("Master waiting for connection to Slave B");

            Socket connectToSlaveBSocket = slaveBSocket.accept();
            System.out.println("Connection to Slave B accepted\n Master is ready to read jobs");
            System.out.println();

            //To send data to SlaveB
            PrintStream printStreamSendToSlaveB =
                    new PrintStream(connectToSlaveBSocket.getOutputStream());

            DataOutputStream dataOutputStreamSendToSlaveB
                    = new DataOutputStream(connectToSlaveBSocket.getOutputStream());

            //To read data coming from SlaveB
            BufferedReader bufferedReaderReadFromSlaveB
                    = new BufferedReader(
                    new InputStreamReader(connectToSlaveBSocket.getInputStream()));

            DataInputStream inStreamFromSlaveB
                    = new DataInputStream(connectToSlaveBSocket.getInputStream());

            //Let SlaveB know that Master is connected
            printStreamSendToSlaveB.println("Master is connected to Slave B");


            // READ JOBS FROM CLIENT
            while ((job = bufferedReaderReadFromClient.readLine()) != null)
            {

                dataOutputStreamSendToSlaveA.flush();
                System.out.println("Master: received job from Client: " + job);//message from client AKA job

                printStreamSendToClient.println("Master received job " + job);

                //FOR EVERY JOB RECEIVED, CALCULATE WHICH SLAVE TO SEND TO
                //CREATE THAT OBJECT'S THREAD
                //START THE PROCESS

                //Split the job by the ID & type
                String jobType = job.substring(job.length() - 1); //last value of the job
                String jobID = job.substring(0, 3);


                String slaveToSend = calculateSlaveType(jobType);

                if (slaveToSend.equals("A"))
                {
                    dataOutputStreamSendToSlaveA.writeUTF(job);//MY CODE LINE 97 SLAVE A
                    gateA = true;
                    System.out.println("Job " + jobID + " sent to Slave A");

                    String completeMessage = inStreamFromSlaveA.readUTF();
                    System.out.println(completeMessage);//READS FROM MY CODE LINE 112 IN SLAVE A

                    if (completeMessage.contains("completed by Slave A"))
                    {
                        dataOutputStreamSendToClient.writeUTF("Job done by Master using Slave A");
                    }
                    else
                    {
                        dataOutputStreamSendToClient.writeUTF("Job not completed by Master yet");
                    }

                    //Call a method within SlaveA that send the job completion message to master

                    System.out.println(inStreamFromSlaveA.readUTF());
                }
                else if (slaveToSend.equals("B"))
                {
                    dataOutputStreamSendToSlaveB.writeUTF(job);
                    gateB = true;

                    System.out.println("Job " + jobID + " sent to Slave B");

                    System.out.println(inStreamFromSlaveB.readUTF());//READS FROM MY CODE LINE 112 IN SLAVEA


                    //Call a method within SlaveA that send the job completion message to master

                    String completeMessage = inStreamFromSlaveB.readUTF();
                    System.out.println(completeMessage);

                    if (completeMessage.contains("completed by Slave B"))
                    {
                        dataOutputStreamSendToClient.writeUTF("Job done by Master using Slave B");
                    }
                    else
                    {
                        dataOutputStreamSendToClient.writeUTF("Job not completed by Master yet");
                    }

                }

                //As long as Master has something to read from Slaves, print it
                //while (inStreamFromSlaveA.readUTF() != null)


            }

            // close connection
            printStreamSendToClient.close();
            bufferedReaderReadFromClient.close();
            bufferedReaderReadFromKeyboard.close();
            masterSocket.close();
            connectToClientSocket.close();

            // terminate application
            System.exit(0);

        } // end of while
    }

    public static String calculateSlaveType(String jobType)
    {
        String slave = null;

        if ((jobType.equalsIgnoreCase("A")) && (gateA))
        {
            slave = "B";
        }
        else if ((jobType.equalsIgnoreCase("A")) && (!gateA))
        {
            slave = "A";
        }

        else if ((jobType.equalsIgnoreCase("B")) && (gateB))
        {
            slave = "A";
        }
        else if ((jobType.equalsIgnoreCase("B")) && (!gateB))
        {
            slave = "B";
        }

        return slave;
    }
}
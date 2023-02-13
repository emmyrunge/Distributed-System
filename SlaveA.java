import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveA extends Thread
{

    public static boolean jobDoneSlaveA = false; //true for finished false for still in process
    public static String jobSlaveA;


    static ArrayList<String> jobCompletedListSlaveA;

    private int sleepTime;
    public static String jobID;


    public static DataOutputStream dataOutputStreamSendToMasterFromA;

    public static PrintStream printStreamSendToMaster;

    public SlaveA(String theJobType, int amountToSleep, String theJobID)
    {
        super(theJobType);
        sleepTime = amountToSleep;
        jobID = theJobID;

    }

    //method that will be executed by the SlaveA thread from the master
    public void run()
    {

        try
        {
            Thread.sleep(sleepTime);

        } catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }

        Master.gateA = false;
        jobDoneSlaveA = true;
    }


    public static void main(String[] args) throws Exception
    {
        jobCompletedListSlaveA = new ArrayList<>();


        // Create SlaveA socket
        Socket slaveASocket = new Socket("localhost", 5693);

        // to send data to the server AKA Master
        dataOutputStreamSendToMasterFromA
                = new DataOutputStream(
                slaveASocket.getOutputStream());

        printStreamSendToMaster
                = new PrintStream(slaveASocket.getOutputStream());

        DataInputStream inStreamFromMasterToSlaveA
                = new DataInputStream(slaveASocket.getInputStream());


        // to read data coming from the server
        BufferedReader slaveAbufferedReaderReadFromMaster
                = new BufferedReader(
                new InputStreamReader(
                        slaveASocket.getInputStream()));


        //Read that the connection was made
        System.out.println(slaveAbufferedReaderReadFromMaster.readLine());

        //SlaveA should execute continuously
        while (true)
        {
            //While there is a job to read from Master AKA Master is sending SlaveA a job
            while (true)
            {

                System.out.println("Reached the point to read jobs from Slave A");
                jobSlaveA = inStreamFromMasterToSlaveA.readUTF();
                System.out.println("Slave A: received job " + jobSlaveA);
                jobCompletedListSlaveA.add(jobSlaveA);

                //Split the job by the type & ID
                String jobType = jobSlaveA.substring(jobSlaveA.length() - 1); //last value of the job
                String jobID = jobSlaveA.substring(0, 3);


                if (jobType.equals("A"))//Optimal
                {
                    System.out.println("Slave A: Reached optimal section in Slave A"); //remove later

                    SlaveA slaveAOptimal = new SlaveA(jobType, 10000, jobID);
                    slaveAOptimal.start();

                    dataOutputStreamSendToMasterFromA.writeUTF("Sent " + jobID + " to sleep in Slave A for 2 seconds");//being read on line 147 in Master

                }
                else if (jobType.equals("B"))//Not optimal
                {
                    SlaveA slaveANonOptimal = new SlaveA(jobType, 20000, jobID);
                    slaveANonOptimal.start();
                    //Master.gateA = true;
                    dataOutputStreamSendToMasterFromA.writeUTF("Sent " + jobID + " to sleep in Slave A for 10 seconds");//being read on line 147 in Master

                }

                //System.out.println(jobCompletedListSlaveA);
                if (jobDoneSlaveA)
                {
                    sendCompletionMessage(jobCompletedListSlaveA.get(0));
                    jobCompletedListSlaveA.remove(0);
                }
                else
                {
                    sendNonCompletionMessage();
                }
            }
        }
    }

    private static void sendCompletionMessage(String jobThatCompleted) throws IOException
    {
        dataOutputStreamSendToMasterFromA.writeUTF("Slave A: job " + jobThatCompleted.substring(0,3) + " was completed by Slave A\n");
    }

    private static void sendNonCompletionMessage() throws IOException
    {
        dataOutputStreamSendToMasterFromA.writeUTF("Slave A: Not completed yet\n");
    }

}
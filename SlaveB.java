import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveB extends Thread
{
    public static boolean jobDoneSlaveB = false; //true for finished false for still in process
    public static String jobSlaveB;

    static ArrayList<String> jobCompletedListSlaveB;

    private int sleepTime;
    public static String jobID;

    public static DataOutputStream dataOutputStreamSendToMasterFromB;

    public static PrintStream printStreamSendToMasterFromB;

    public SlaveB(String jobType, int amountToSleep, String theJobID)
    {
        super(jobType);
        sleepTime = amountToSleep;
        jobID = theJobID;
    }

    public void run()
    {
        try
        {
            Thread.sleep(sleepTime);

        } catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }

        Master.gateB = false;
        jobDoneSlaveB = true;
    }

    public static void main(String[] args) throws Exception
    {
        jobCompletedListSlaveB = new ArrayList<>();

        // Create SlaveA socket
        Socket slaveBSocket = new Socket("localhost", 4487);

        // to send data to the server AKA Master
        dataOutputStreamSendToMasterFromB
                = new DataOutputStream(
                slaveBSocket.getOutputStream());

        printStreamSendToMasterFromB
                = new PrintStream(slaveBSocket.getOutputStream());

        DataInputStream inStreamFromMasterToSlaveB
                = new DataInputStream(slaveBSocket.getInputStream());

        // to read data coming from the server
        BufferedReader SlaveBbufferedReaderReadFromMaster
                = new BufferedReader(
                new InputStreamReader(
                        slaveBSocket.getInputStream()));


        System.out.println(SlaveBbufferedReaderReadFromMaster.readLine());

        //SlaveB should execute continuously
        while (true)
        {
            //While there is a job to read from Master AKA Master is sending SlaveB a job
            while (true)
            {

                System.out.println("Reached the point to read jobs from Slave B");
                jobSlaveB = inStreamFromMasterToSlaveB.readUTF();
                System.out.println("Slave B: received job " + jobSlaveB);

                jobCompletedListSlaveB.add(jobSlaveB);



                //Split the job by the type & ID
                String jobType = jobSlaveB.substring(jobSlaveB.length() - 1); //last value of the job
                String jobID = jobSlaveB.substring(0, 3);


                if (jobType.equals("B"))//Optimal
                {
                    System.out.println("Slave B: Reached optimal section in Slave A"); //remove later

                    SlaveB slaveBOptimal = new SlaveB(jobType, 10000, jobID);
                    slaveBOptimal.start();
                    //Master.gateB = true;

                    //jobComplete.add(slaveAOptimal);
                    //sizeOfJobComplete = jobComplete.size();

                    dataOutputStreamSendToMasterFromB.writeUTF("Sent " + jobID + " to sleep in Slave B for 2 seconds");//being read on line 147 in Master

                }
                else if (jobType.equals("A"))//Not optimal
                {
                    SlaveB slaveBNonOptimal = new SlaveB(jobType, 20000, jobID);
                    slaveBNonOptimal.start();

                    //Master.gateB = true;

                    dataOutputStreamSendToMasterFromB.writeUTF("Sent " + jobID + " to sleep in Slave B for 10 seconds");//being read on line 147 in Master


                }

                System.out.println(jobCompletedListSlaveB);

                if (jobDoneSlaveB)
                {
                    sendCompletionMessage(jobCompletedListSlaveB.get(0));
                    jobCompletedListSlaveB.remove(0);
                }
                else
                {
                    sendNonCompletionMessage();
                }
                System.out.println(jobCompletedListSlaveB);
            }
        }
    }

    private static void sendCompletionMessage(String jobThatCompleted) throws IOException
    {
        dataOutputStreamSendToMasterFromB.writeUTF("Slave B: job " + jobThatCompleted.substring(0,3) + " was completed by Slave A\n");
    }

    private static void sendNonCompletionMessage() throws IOException
    {
        dataOutputStreamSendToMasterFromB.writeUTF("Slave B: Not completed yet\n");
    }

}
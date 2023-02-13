/*
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PracticeMaster
{
    public static boolean gateA = false;
    public static boolean gateB = false;
    Socket socket = null;
    InputStreamReader inputReader = null;
    OutputStreamWriter outputWriter = null;
    BufferedReader fastReader = null;
    BufferedWriter fastWriter = null;
    ServerSocket masterSocket = null;
    String messageFromClient;
    ArrayList<String> clientJobs = new ArrayList<>();
    ServerSocket masterToSlaveASocket; //must match port number client uses
    ServerSocket masterToSlaveBSocket;

    int jobNum = 0;

    public static void main(String [] args)
    {


    }

    private void start(int port) throws IOException
    {
        //masterSocket = new ServerSocket(6789); //must match port number client uses

        while (true) //infinite loop
        {
            try
            {
                System.out.println("Waiting for connection...");

                socket = masterSocket.accept(); //creates a new socket when connected
                System.out.println("Connection acquired");

                inputReader = new InputStreamReader(socket.getInputStream());
                outputWriter = new OutputStreamWriter(socket.getOutputStream());

                fastReader = new BufferedReader(inputReader);
                fastWriter = new BufferedWriter(outputWriter);


                messageFromClient = fastReader.readLine();
                clientJobs.add(messageFromClient);
                System.out.println(clientJobs); //remove later


            } catch (IOException e)
            {
                socket.close();
                inputReader.close();
                outputWriter.close();
                fastReader.close();
                fastWriter.close();
                throw new RuntimeException(e);
            }
        }
    }

    public void start() throws IOException
    {

        start(6789);

        while (true)
        {
            while (jobNum < clientJobs.size())
            {
                String jobType = getJob(clientJobs, jobNum).substring(getJob(clientJobs, jobNum).length() - 1);
                String jobID = getJob(clientJobs, jobNum).substring(4, 8);
                String sendToSlave = calculateSlaveType(jobType);

                masterToSlaveASocket = new ServerSocket(1234);
                masterToSlaveBSocket = new ServerSocket(5678);


                if (sendToSlave.equals("A"))
                {
                    Socket SASocket;
                    SASocket = masterToSlaveASocket.accept(); //creates a new socket when connected
                    InputStreamReader inputSlaveReader = new InputStreamReader(SASocket.getInputStream());
                    OutputStreamWriter outputSlaveWriter = new OutputStreamWriter(SASocket.getOutputStream());

                    fastReader = new BufferedReader(inputSlaveReader);
                    fastWriter = new BufferedWriter(outputSlaveWriter);

                    if (jobType.equals("A"))
                    {
                        SlaveA jobA = new SlaveA(jobID, 20000);
                        jobA.start();
                        System.out.println("Job ID: " + jobID + " going to sleep in Slave A for " + 20000 + " milliseconds");
                        gateA = true;

                        clientJobs.remove(jobNum); //could be problem
                        System.out.println("Removed: " + clientJobs); //also remove later
                        jobNum--;

                        fastReader.readLine();

                    }
                    else if (jobType.equals("B"))
                    {
                        SlaveA jobA = new SlaveA(jobID, 40000);
                        jobA.start();
                        System.out.println("Job ID: " + jobID + " going to sleep in Slave A for " + 40000 + " milliseconds");
                        gateA = true;

                        clientJobs.remove(jobNum); //could be problem
                        System.out.println("Removed: " + clientJobs); //also remove later
                        jobNum--;
                    }
                }
                else if (sendToSlave.equals("B"))
                {
                    Socket SBSocket;
                    SBSocket = masterToSlaveASocket.accept(); //creates a new socket when connected
                    InputStreamReader inputSlaveReader = new InputStreamReader(SBSocket.getInputStream());
                    OutputStreamWriter outputSlaveWriter = new OutputStreamWriter(SBSocket.getOutputStream());

                    fastReader = new BufferedReader(inputSlaveReader);
                    fastWriter = new BufferedWriter(outputSlaveWriter);
                    if (jobType.equals("B"))
                    {
                        SlaveB jobB = new SlaveB(jobID, 20000);
                        jobB.start();
                        System.out.println("Job ID: " + jobID + " going to sleep in Slave B for " + 20000 + " milliseconds");
                        gateB = true;

                        clientJobs.remove(jobNum); //could be problem
                        System.out.println("Removed: " + clientJobs); //also remove later
                        jobNum--;
                    }
                    else if (jobType.equals("A"))
                    {
                        SlaveB jobB = new SlaveB(jobID, 40000);
                        jobB.start();
                        System.out.println("Job ID: " + jobID + " going to sleep in Slave B for " + 40000 + " milliseconds");
                        gateB = true;

                        clientJobs.remove(jobNum); //could be problem
                        System.out.println("Removed: " + clientJobs); //also remove later
                        jobNum--;
                    }
                }
                //System.out.println(jobID + " Finished its job"); //send this line to client later
                jobNum++;
            }


            if (messageFromClient.equalsIgnoreCase("BYE"))
            {
                break;
            }
        }
    }


    public static String getJob(ArrayList<String> jobs, int jobNumIndex)
    {
        return "job " + jobs.get(jobNumIndex);
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
*/

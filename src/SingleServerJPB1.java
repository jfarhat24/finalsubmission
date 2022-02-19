
import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;


public class SingleServerJPB1 {
    
    private static final int SERVER_PORT = 8765;
    private static String username = "root";

    public static void main(String[] args) {
        createCommunicationLoop();
    }//end main
    
    public static void createCommunicationLoop() {
        try {
            //create server socket
            ServerSocket serverSocket =
                    new ServerSocket(SERVER_PORT);

            System.out.println("Server started at " +
                    new Date() + "\n");
            //listen for a connection
            //using a regular *client* socket
            Socket socket = serverSocket.accept();

            //now, prepare to send and receive data
            //on output streams
            DataInputStream inputFromClient =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream outputToClient =
                    new DataOutputStream(socket.getOutputStream());

            FilenameFilter textFilter = new FilenameFilter() { //this would be what we would use to sort through all the lists.
                public boolean accept(File f, String name)
                {
                    return name.endsWith("solutions.txt");
                }
            };

            //login would happen here, replacing the username with the one loaded from the logins.txt file

            String newListName = username +"_solutions.txt"; //creates the file of the username with the solutions.
            File newList = new File(newListName);
            String returningAnswers = null;
            String numbersOnly = null;
            Scanner giveList = new Scanner(newList);
            FileOutputStream theList = new FileOutputStream(newList);
            DataOutputStream listWrite = new DataOutputStream(new BufferedOutputStream(theList)); //sorts through the text lists
            String givingList = "";




            //server loop listening for the client 
            //and responding
            while (true) { //Attempts to create a login feature ended in failure.
                double num1 = 0.00;
                double num2 = 0.00;
                double answer = 0.00;
                double answer2 = 0.00; //these set up the variables for later
                String strReceived = inputFromClient.readUTF();

                System.out.println("Client says: " + strReceived);
                if (strReceived.equalsIgnoreCase("hello")) { //start of the
                    System.out.println("Sending hello to client");
                    outputToClient.writeUTF("hello client!");
                } else if (strReceived.equalsIgnoreCase("SHUTDOWN")) {
                    System.out.println("200 OK");
                    outputToClient.writeUTF("quit");
                    serverSocket.close();
                    socket.close();
                    break;  //get out of loop
//            } else if (strReceived.equalsIgnoreCase("LOGOUT")) { //Could not figure out how to implement multiple clients with server
//                System.out.println("Shutting down client...");
//                outputToClient.writeUTF("quit");
//                socket.close();
                } else if (strReceived.contains("SOLVE") || (strReceived.contains("solve"))) {


                    numbersOnly = strReceived.replaceAll("[^0-9]", "");


                    if (strReceived.contains("-r"))
                    {
                        if(numbersOnly.length() == 2) //checks if there's two numbers
                        {
                            num1 = Character.getNumericValue(numbersOnly.charAt(0));
                            num2 = Character.getNumericValue(numbersOnly.charAt(1));

                            answer = (num1 * 2) + (num2 * 2);
                            answer2 = num1 * num2;
                            returningAnswers = "Sides " + num1 + " " + num2 + ": Rectangle’s perimeter and area is: " + answer + ", " + answer2;
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }
                        else if(numbersOnly.length() == 1) //checks if there's one number
                        {
                            num1 = Character.getNumericValue(numbersOnly.charAt(0));
                            num2 = Character.getNumericValue(numbersOnly.charAt(0));
                            answer = (num1 * 2) + (num2 * 2);
                            answer2 = num1 * num2;
                            returningAnswers = "Sides " + num1 + " " + num2 + ": Square’s perimeter and area is: " + answer + ", " + answer2;
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }
                        else
                        {
                            System.out.println("Wrong numbers");
                            returningAnswers = "Error: No sides found.";
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }
                    } else if (strReceived.contains("-c")) {
                        double pi = 3.14;

                        if(numbersOnly.length() == 1) //checks if there's one number
                        {
                            num1 = Character.getNumericValue(numbersOnly.charAt(0));
                            answer = 2 * pi * num1;
                            returningAnswers = "Circumference of circle is: " + answer;
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);

                        }
                        else
                        {
                            System.out.println("Wrong numbers");

                            returningAnswers = "Error: No radius found or other error.";
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }

                    }
                    else
                    {
                        System.out.println("No numbers");
                        outputToClient.writeUTF("Error: Please append -r or -c to the statement you're attempting.");
                    }

                } else if (strReceived.contains("LIST") || (strReceived.contains("list"))) {
                    {
                        if ((strReceived.contains("-all")) && (username.equalsIgnoreCase("root"))) { //root access works but program does not know how to make other lists or sort through them.
//                            String appended = "_solutions.txt";
//
//                            File f = new File("src");
//                            File[] textFiles = f.listFiles(textFilter);
//                            for(int j = 0; j < textFiles.length(); j++)
//                            {
//
//                            }
                            outputToClient.writeUTF("You have root access, but this file does not have the ability to have other users or access their files. Please try again without the -all.");

                        } else if ((strReceived.contains("-all")) && (!username.equalsIgnoreCase("root"))) {
                            outputToClient.writeUTF("Error: you are not the root user");
                        } else {
                            givingList += username + "\n";

                            while(giveList.hasNextLine())
                            {
                                givingList += giveList.nextLine() + "\n";
                            }
                            outputToClient.writeUTF(givingList); //writing into root_solutions.txt will cause this line to work as intended.
                        }
                    }
                    }
            else
                {
                        System.out.println("Unknown command received: "
                                + strReceived);
                        outputToClient.writeUTF("300 invalid command");
                    }
                }//end server loop
            }

        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
    }//end createCommunicationLoop
}
//below is our attempts to get login to work with our project.
//            while (loginTest = false) {
//                String un = null;
//                boolean loginTest2 = false;
//                System.out.println(loginTest);
//                while (textFile.hasNextLine()) {
//                    String line = textFile.nextLine();
//                    if (strReceived.equals(line)) {
//                        loginTest2 = true;
//                        un = line.split("\\s")[0];
//                    }
//                }
//                if (loginTest2 == true) {
//                    username = un;
//                    outputToClient.writeUTF("working");
//                    loginTest = true;
//                    System.out.println(loginTest);
//                } else {
//                    outputToClient.writeUTF("NOTworking");
//                    System.out.println(loginTest + " this might be a problem.");
//                }
//            }
//            while (loginTest) {
//                if (strReceived.equalsIgnoreCase("hello")) {
//                    System.out.println("Sending hello to client");


//          while(sc.hasNextLine())
//            {
//
//                if(sc.nextLine().equalsIgnoreCase(strReceived))
//                {
//                    correctPassword = true;
//                }
//            }
//            if(correctPassword == true)
//            {
//                System.out.println("working");
//                outputToClient.writeUTF("working");
//username = un;
//            }

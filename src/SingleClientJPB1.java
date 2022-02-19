
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class SingleClientJPB1 {

    private static final int SERVER_PORT = 8765;

    public static void main(String[] args) {

        DataOutputStream toServer;
        DataInputStream fromServer;
        Scanner input =
                new Scanner(System.in);
        String message;

        //attempt to connect to the server
        try {
            Socket socket =
                    new Socket("localhost", SERVER_PORT);

            //create input stream to receive data
            //from the server
            fromServer =
                    new DataInputStream(socket.getInputStream());

            toServer =
                    new DataOutputStream(socket.getOutputStream());


            while(true) {
                System.out.print("Send command to server:\t");
                message = input.nextLine();
                toServer.writeUTF(message);
                if(message.equalsIgnoreCase("quit")) {
                    break;
                }

                //received message:
                message = fromServer.readUTF();
                System.out.println("Server says: " + message);
                if(message.equals("quit")) {
                    System.out.print("200 OK"); //Because the server does not recognize the above quit, we have to put 200 OK here
                    break;
                }
            }

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch


    }//end main
}
//below is our attempts to get login to work with our project.
//            while(!loginTest) {
//                System.out.println("Please type in your username followed by your password.");
//                message = input.nextLine();
//                toServer.writeUTF(message);
//                message = fromServer.readUTF();
//                if (message.equalsIgnoreCase("working")) {
//                    loginTest = true;
//                } else {
//                    System.out.println("Incorrect username and/or password. Please try again.");
//                }
//                System.out.println(loginTest);
//                System.out.print(message);
//            }
//            while (loginTest) {
//                System.out.print("Send command to server:\t");
//                message = input.nextLine();
//                toServer.writeUTF(message);
//                if (message.equalsIgnoreCase("200 OK")) {
//                    System.out.print(message);
//                    break;
//                }
//
//                //received message:
//                message = fromServer.readUTF();
//                System.out.print(message);
//            }
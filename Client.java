
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * <p>This class starts the client GUI for the chatroom and starts communication with the server.
 */

/*  Package separation information
    ===================
    =   Client        =
    =   ClientFrame   =
    =   ServerThread  =
    ===================

    ===================
    =   Server        =
    =   ServerFrame   =
    =   ClientThread  =
    ===================
 */
    
public class Client {

    private int port;
    private String name;

    /**
     * This method initiates a new client
     * @param port port of server
     * @param name user name
     */
    public Client(int port, String name) {
        this.port = port;
        this.name = name;
        startClient();
    }

    /**
     * This method creates a socket for the client to communicate to.
     */
    private void startClient(){

        try{
            Socket client = new Socket("localhost", port);
            Thread.sleep(1000);

            ServerThread server_thread = new ServerThread(client, name);
            Thread server = new Thread(server_thread);
            server.start();
            Scanner scan = new Scanner(System.in);

            while(server.isAlive()){
                if(scan.hasNextLine()){
                    server_thread.newMessage(scan.nextLine());
                }
            }

        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * This main starts the client suite
     */
    public static void main(String[] args) {

        int port;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a port number -> ");
        port= Integer.parseInt(scan.nextLine());

        String name = null;
        System.out.print("Enter a username -> ");
        name = scan.nextLine();

        Client client = new Client(port, name);
    }
}

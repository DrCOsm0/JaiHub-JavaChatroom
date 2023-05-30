
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * <p>This class creates the chatroom server and initializes the server socket
 */

public class Server {

    private int port;
    private ArrayList<ClientThread> clients;
    public HashMap<String, String> userList = new HashMap<>();

    /**
     * This method initiates a new server instance
     * @param port server port
     */
    public Server(int port){
        this.port = port;
        clients = new ArrayList<>();

        ServerSocket server_socket = null;
        try{
            server_socket = new ServerSocket(port);

            acceptClients(server_socket);
        }
        catch(IOException e){
            System.out.println(
                    "Failed to start the server on port : " +
                    port
            );
            e.printStackTrace();
        }
    }

    /**
     * This method returns the connected clients
     * @return clients
     */
    public ArrayList<ClientThread> getClients() {
        return clients;
    }

    /**
     * This method accepts new cliends into the server
     * @param server_socket server socket
     */
    private void acceptClients(ServerSocket server_socket) {
        while(true){
            try{
                Socket client = server_socket.accept();
                System.out.println(
                        "Accepted new connection from: " +
                        client.getRemoteSocketAddress()
                );
                ClientThread client_thread = new ClientThread(this, client);

                Thread new_client = new Thread(client_thread);
                new_client.start();
                clients.add(client_thread);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This main creates the new server instance
     */
    public static void main(String[] args) {
        int port;
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a port number-> ");
        port = scan.nextInt();
        scan.close();
        Server server = new Server(port);
    }
}

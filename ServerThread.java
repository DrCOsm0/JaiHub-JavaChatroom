
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * <p>This class creates the communication between the client and the server using threads and sockets
 */

public class ServerThread implements Runnable {

    private Socket client;
    private String name;
    private final LinkedList<String> messages;
    public ClientFrame chatFrame;

    /**
     * This method initiates a new server thread
     * @param client client socket
     * @param name username
     */
    public ServerThread(Socket client, String name) {
        this.client = client;
        this.name = name;
        messages = new LinkedList<String>();
        chatFrame = new ClientFrame();
    }

    /**
     * This method sets a new message
     * @param new_message new message string
     */
    public void newMessage(String new_message){
        synchronized(messages){
            messages.push(new_message);
        }
    }

    /**
     * This method houses the communication to the server and the GUI
     */
    @Override
    public void run() {
        System.out.println("Welcome " + name);
        chatFrame.messageWindow.append("Welcome " + name + "\n");

        try{
            PrintWriter out_stream = new PrintWriter(client.getOutputStream(), false);
            InputStream in_stream = client.getInputStream();
            Scanner in = new Scanner(in_stream);

            out_stream.println("init" + " " + name + " " + client.getLocalSocketAddress());
            out_stream.flush();

            while(!client.isClosed()){

                if(in_stream.available() > 0){
                    if(in.hasNextLine()){
                        String message = in.nextLine();
                        chatFrame.messageWindow.append(message + "\n");
                        System.out.println(message + "\n");
                    }
                }

                if(chatFrame.message != null) {
                    newMessage(chatFrame.message);
                    chatFrame.message = null;
                }

                if(!messages.isEmpty()){
                    String next = null;
                    synchronized(messages){
                        next = messages.pop();
                    }

                    out_stream.println(name + " > " + next);
                    out_stream.flush();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

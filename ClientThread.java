
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * <p>This class houses the communication with the server and the client as well as the GUI
 */

public class ClientThread implements Runnable{

    private Server server;
    private Socket client;
    private PrintWriter out;

    /**
     * This method initiates a new client thread
     * @param server server instance
     * @param client client socket
     */
    public ClientThread(Server server, Socket client) {
        this.server = server;
        this.client = client;
    }

    /**
     * This method returns a new print writer
     * @return out
     */
    public PrintWriter getWriter(){
        return out;
    }

    /**
     * This method handles all the private messaging and initialization with the client and the GUI
     */
    @Override
    public void run() {
        try{
            this.out = new PrintWriter(client.getOutputStream(), false);
            Scanner in = new Scanner(client.getInputStream());

            while(!client.isClosed()){
                if(in.hasNextLine()){
                    String[] initInput;
                    String input = in.nextLine();
                    initInput = input.split(" ");

                    if(initInput.length <= 2) { //uncomment to have null messages
                        /*for(ClientThread c : server.getClients()){
                            PrintWriter cout = c.getWriter();

                            if(cout != null){
                                cout.write(input + "\r\n");
                                cout.flush();
                            }
                        }*/
                        continue;
                    }

                    if(initInput[0].equals("init")) {
                        server.userList.put(initInput[1], initInput[2]);
                        continue;
                    }

                    for(ClientThread c : server.getClients()){

                        String[] directMessage;
                        directMessage = input.split(" ");

                        if(directMessage[2].equals("/msg")) {

                            String sender;
                            String reciever;
                            String message;

                            sender = directMessage[0];
                            reciever = directMessage[3];
                            message = String.join(" ", Arrays.copyOfRange(directMessage, 4, directMessage.length));

                            if(server.userList.containsKey(reciever)) {

                                String portS = server.userList.get(sender);
                                if(portS.equals(c.client.getRemoteSocketAddress().toString())) {
                                    PrintWriter cout = c.getWriter();
                                    if(cout != null){
                                        cout.write(sender + " > " + "[private message] " + "<" + reciever + "> " + message +"\r\n");
                                        cout.flush();
                                    }
                                }
                                String portR = server.userList.get(reciever);
                                if(portR.equals(c.client.getRemoteSocketAddress().toString())) {
                                    PrintWriter cout = c.getWriter();

                                    if(cout != null){
                                        cout.write("[private message] " + sender + " >> " + message +"\r\n");
                                        cout.flush();
                                    }
                                }

                            }else{

                                String portS = server.userList.get(sender);
                                if(portS.equals(c.client.getRemoteSocketAddress().toString())) {
                                    PrintWriter cout = c.getWriter();
                                    if(cout != null){
                                        cout.write("User: " + reciever + " Does not exist!" +"\r\n");
                                        cout.flush();
                                    }
                                }
                            }
                            continue;
                        }

                        PrintWriter cout = c.getWriter();

                        if(cout != null){
                            cout.write(input + "\r\n");
                            cout.flush();
                        }
                    }
                }
            }

        } catch (IOException | NullPointerException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}

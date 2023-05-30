

import javax.swing.*;
import java.awt.*;

/**
 * <p>This class creates the client frame and text window for the GUI.
 */

public class ClientFrame extends JFrame {

    public String message;
    public JTextArea messageWindow; //user sees message
    public JTextField textWindow; //user writes message
    public JButton sendButton;

    /**
     * This method constructs the client GUI and sets frame attributes
     */
    public ClientFrame() {

        JPanel UI = new JPanel();
        JPanel messagePanel = new JPanel();

        sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            message = textWindow.getText();
            textWindow.setText("");
        });

        textWindow = new JTextField();
        textWindow.setPreferredSize(new Dimension(350,30));

        messageWindow = new JTextArea(10,40);
        messageWindow.setEditable(false);

        UI.add(textWindow, BorderLayout.WEST);
        UI.add(sendButton,BorderLayout.EAST);
        messagePanel.add(messageWindow);
        add(UI, BorderLayout.SOUTH);
        add(messagePanel, BorderLayout.NORTH);
        add(new JScrollPane(messageWindow), BorderLayout.CENTER);

        setFont(new Font("Serif", Font.BOLD, 20));
        setTitle("JMessenger");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

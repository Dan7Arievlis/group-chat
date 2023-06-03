import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginScreen {
  protected static String userName, serverIp, serverPort;

  public void start() {
    JDialog frame = new JDialog();
    frame.setModal(true);
    frame.setLocationRelativeTo(null);
    frame.setSize(360, 230);
    frame.setResizable(false);
    frame.setLocation(frame.getLocation().x - 180, frame.getLocation().y - 115);

    JPanel panel = new JPanel();
    panel.setLayout(null);

    JLabel user_lbl = new JLabel("User name");
    user_lbl.setBounds(20, 30, 80, 20);
    panel.add(user_lbl);

    JTextField user_tf = new JTextField(40);
    user_tf.setBounds(100, 30, 200, 20);
    panel.add(user_tf);
    //
    JLabel server_lbl = new JLabel("Server IP");
    server_lbl.setBounds(20, 70, 80, 20);
    panel.add(server_lbl);

    JTextField server_tf = new JTextField(40);
    server_tf.setText("localhost");
    server_tf.setBounds(100, 70, 200, 20);
    panel.add(server_tf);
    //
    JLabel port_lbl = new JLabel("Server Port");
    port_lbl.setBounds(20, 110, 80, 20);
    panel.add(port_lbl);

    JTextField port_tf = new JTextField(40);
    port_tf.setText("8080");
    port_tf.setBounds(100, 110, 45, 20);
    panel.add(port_tf);
    //
    JButton login_bt = new JButton("Login");
    login_bt.setBounds(220, 150, 80, 20);
    panel.add(login_bt);
    login_bt.addActionListener((action) -> {
      userName = user_tf.getText().trim();
      serverIp = server_tf.getText().trim();
      serverPort = port_tf.getText().trim();
      
      if(!userName.isEmpty() && !serverIp.isEmpty() && !serverPort.isEmpty())
        frame.dispose();
      else
      JOptionPane.showMessageDialog(null, "Campos preenchidos indevidamente!",
      "ERRO", JOptionPane.ERROR_MESSAGE);
    });

    frame.add(panel);
    frame.setVisible(true);
  }

  public String getUserName() {
    return userName;
  }

  public String getServerIp() {
    return serverIp;
  }

  public String getServerPort() {
    return serverPort;
  }
}

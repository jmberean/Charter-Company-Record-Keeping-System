import java.awt.EventQueue;

//import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPasswordField;
import java.awt.SystemColor;
public class GUI {

	
	
	private JFrame frame;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.menu);
		frame.setBounds(100, 100, 718, 501);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setBackground(Color.WHITE);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DataBase db = new DataBase();
				
				String username = txtUsername.getText();
				char[] password = txtPassword.getPassword();
				//username.contains("123") && password.contains("123")
						
				try {
					if(db.login(username, password))
					{
						frame.dispose();
						MainMenu menu = new MainMenu();
						menu.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Invalid Login Details", "Login Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				catch(SQLException se) {
					JOptionPane.showMessageDialog(null, se.toString(), "Login Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		
		btnLogin.setBounds(301, 237, 98, 25);
		frame.getContentPane().add(btnLogin);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(301, 105, 116, 22);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblUserName = new JLabel("Username");
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUserName.setBounds(191, 108, 73, 16);
		frame.getContentPane().add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPassword.setBounds(191, 165, 73, 16);
		frame.getContentPane().add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(301, 162, 116, 22);
		frame.getContentPane().add(txtPassword);
		
		JLabel lblicon = new JLabel("");
		//Image img = new ImageIcon(this.getClass().getResource("/login-icon.png")).getImage(;)
		//lblIcon.setIcon(new ImageIcon(img));
		lblicon.setBounds(12, 108, 222, 154);
		frame.getContentPane().add(lblicon);
		frame.getRootPane().setDefaultButton(btnLogin);
	}
}

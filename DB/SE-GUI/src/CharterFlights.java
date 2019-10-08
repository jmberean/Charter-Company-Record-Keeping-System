import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JOptionPane;

import java.awt.SystemColor;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class CharterFlights extends JFrame {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CharterFlights frame = new CharterFlights();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CharterFlights() {
		JFrame frame = new JFrame("Charter Flight");
		JPanel contentPane = new JPanel();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.setLayout(new GridLayout(2,3,5,5));
		frame.setBounds(100, 100, 718, 501);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		//Title
		/*
		JLabel lblNewLabel = new JLabel("Charter Flight");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(298, 41, 103, 38);
		contentPane.add(lblNewLabel);
		*/
		//Input fields
			//Char_Date field
			JLabel lblChar_Date = new JLabel("Charter Date");
			lblChar_Date.setFont(new Font("Tahoma", Font.BOLD, 13));
			contentPane.add(lblChar_Date);
	
			JTextField txtChar_Date = new JTextField();
			txtChar_Date.setColumns(10);
			contentPane.add(txtChar_Date);
			
			//Destination field
			JLabel lblDestination = new JLabel("Destination");
			lblDestination.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblDestination.setBounds(191, 108, 73, 16);
			contentPane.add(lblDestination);
	
			JTextField txtDestination = new JTextField();
			txtDestination.setBounds(301, 105, 116, 22);
			txtDestination.setColumns(10);
			contentPane.add(txtDestination);
	
			//Distance field
			JLabel lblDistance = new JLabel("Distance");
			lblDistance.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblDistance.setBounds(191, 108, 73, 16);
			contentPane.add(lblDistance);
	
			JTextField txtDistance = new JTextField();
			txtDistance.setBounds(301, 105, 116, 22);
			txtDistance.setColumns(10);
			contentPane.add(txtDistance);
			
			//Aircraft field
			JLabel lblAircraft = new JLabel("Aircraft");
			lblAircraft.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblAircraft.setBounds(191, 108, 73, 16);
			contentPane.add(lblAircraft);
	
			JTextField txtAircraft = new JTextField();
			txtAircraft.setBounds(301, 105, 116, 22);
			txtAircraft.setColumns(10);
			contentPane.add(txtAircraft);
		

		
		JButton btnLogin = new JButton("Charter Flight");
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setBackground(Color.WHITE);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnLogin.setBounds(301, 237, 98, 25);
		frame.getContentPane().add(btnLogin);
		
		
		
		// Set the window to be visible as the default to be false
		frame.add(contentPane);
		frame.setVisible(true);	
	}
	
}

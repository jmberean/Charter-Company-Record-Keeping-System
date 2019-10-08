import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

public class MainMenu extends JFrame {
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 803, 553);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Main Menu");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(353, 32, 78, 40);
		contentPane.add(lblNewLabel);
		
		JButton btnCharterNewFlight = new JButton("Charter New Flight");
		btnCharterNewFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CharterFlights charter = new CharterFlights();
				charter.setVisible(true);
			}
		});
		btnCharterNewFlight.setBounds(311, 126, 162, 25);
		contentPane.add(btnCharterNewFlight);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI logout = new GUI();
				//logout.setVisible(true);
			}
		});
		btnLogout.setBounds(353, 419, 78, 25);
		contentPane.add(btnLogout);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Chartered Flights", 
				"Aircraft", "Model", "Employee", "Crews", "Pilots", "Rating", "Customer", "Education", "Flight Assignment",
				"Job", "License", "Payment", "Pilot License", "Pilot Rating", "Qualification", "Result"}));
		comboBox.setBounds(311, 239, 162, 25);
		comboBox.setEditable(true);
		contentPane.add(comboBox);
		
		JLabel lblAccessRecords = new JLabel("Access Records");
		lblAccessRecords.setHorizontalAlignment(SwingConstants.CENTER);
		lblAccessRecords.setBounds(311, 214, 162, 20);
		contentPane.add(lblAccessRecords);
		
		JButton btnSelectRecord = new JButton("Go");
		btnSelectRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*if(comboBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "Please select an option!");
				}else {*/
					String table = "";
					switch(comboBox.getSelectedItem().toString()) {
					case "Chartered Flights":
						table = "charter";
						break;
					case "Aircraft":
						table = "aircraft";
						break;
					case "Model":
						table = "model";
						break;
					case "Employee":
						table = "employee";
						break;
					case "Crews":
						table = "crew";
						break;
					case "Pilots":
						table = "pilot";
						break;
					case "Rating":
						table = "rating";
						break;
					case "Customer":
						table = "customer";
						break;
					case "Education":
						table = "education";
						break;
					case "Flight Assignment":
						table = "flt_assignment";
						break;
					case "Job":
						table = "job";
						break;
					case "License":
						table = "license";
						break;
					case "Payment":
						table = "payment";
						break;
					case "Pilot License":
						table = "pil_license";
						break;
					case "Pilot Rating":
						table = "pil_rating";
						break;
					case "Qualification":
						table = "qualification";
						break;
					case "Result":
						table = "result";
						break;
					}
					
					RecordView recordView = new RecordView(table, comboBox.getSelectedItem().toString());
					recordView.setVisible(true);
				//}
				
				
			}
		});
		btnSelectRecord.setBounds(353, 273, 78, 23);
		contentPane.add(btnSelectRecord);
	}
}

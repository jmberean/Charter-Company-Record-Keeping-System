import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.WindowConstants;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import java.awt.FlowLayout;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.ComboBoxModel;

public class RecordView extends JFrame {

	private DataBase db = new DataBase();
	private JPanel contentPane;
	private JPanel panel;
	private JTable table;
	private JFrame frame;
	// private final DefaultTableModel tableModel = new DefaultTableModel();

	private final MyModel tableModel = new MyModel();

	private final DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
	// private String selectedRowKeyId;
	private int selectedRowIndex, selectedColIndex;
	private List<String> columnNameList = new ArrayList();
	private List<String> columnTypeList = new ArrayList();
	private String[] primaryKeys;
	private String[][] foreignKeys;
	private JTextField textFieldFilter;
	private JButton btnFilter;
	private JComboBox comboBoxFilter;
	private JButton btnAddRecord;
	private JPanel panelEdit;
	private JSeparator separator_1;
	private JButton btnUpdateRecord;
	private JButton btnDeleteRecord;

	/*
	 * Launch the application.
	 * 
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { RecordView frame = new RecordView();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 * 
	 * /** Create the frame.
	 */
	public RecordView(String selectedTable, String friendlyTableName) {

		setTitle(friendlyTableName + " Records");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 718, 501);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 20));

		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBackground(Color.LIGHT_GRAY);
		contentPane.add(panel, BorderLayout.NORTH);

		comboBoxFilter = new JComboBox(comboBoxModel);
		panel.add(comboBoxFilter);

		textFieldFilter = new JTextField();
		panel.add(textFieldFilter);
		textFieldFilter.setColumns(10);

		btnFilter = new JButton("Filter");
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBoxFilter.getSelectedIndex() != 0 && !textFieldFilter.getText().trim().equals("")) {
					loadData(selectedTable, comboBoxFilter.getSelectedItem().toString(),
							textFieldFilter.getText().trim());
					textFieldFilter.setText("");
				} else {
					loadData(selectedTable);
				}
			}
		});
		panel.add(btnFilter);

		btnAddRecord = new JButton("Add Record");
		btnAddRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String[] foreignKeyNames = new String[foreignKeys.length];
				for(int i = 0; i < foreignKeys.length; i++)
				{
					System.out.println("adding to f key names" + foreignKeys[i][0]);
					foreignKeyNames[i] = foreignKeys[i][0];
				}
				
				List<JComponent[]> inputs = new ArrayList();
				List<String> entries = new ArrayList();

				JComponent[][] components = new JComponent[columnNameList.size()][2];

				for (int i = 0; i < columnNameList.size(); i++) {
					// columnBoxes.add(new JTextField());
					// inputs.add(new JComponent[] { new JLabel(columnNameList.get(i)),
					// columnBoxes.get(i) });
					// inputs.add(new JComponent[] { new JLabel(columnNameList.get(i)), new
					// JTextField() });

					components[i][0] = new JLabel(columnNameList.get(i) + " - " + columnTypeList.get(i) );

					if (Arrays.asList(foreignKeyNames).contains(columnNameList.get(i).trim())) {
						System.out.println("FK adding combobox");
						String foreignParent = "";
						List<String> foreignKeyEntries = new ArrayList();
						
						for(int j = 0; j < foreignKeyNames.length; j++) {
							if(foreignKeyNames[j].equals(columnNameList.get(i))) {
								foreignParent = foreignKeys[j][1];
								
								try (Connection conn = DriverManager.getConnection(db.getDbAddress(), db.getDbUser(), db.getDbPass());
										Statement stmt = conn.createStatement()) {
									String sqlCommand = "SELECT " + columnNameList.get(i) + " FROM " + foreignParent;
									ResultSet rs = stmt.executeQuery(sqlCommand);
									while(rs.next()) {
										foreignKeyEntries.add(rs.getString(columnNameList.get(i)));
									}
								} 
								catch(SQLException eSQL) {
									System.out.println(eSQL.toString());
									JOptionPane.showMessageDialog(null, "There was an error connecting to the database.",
											"Connection Error", JOptionPane.ERROR_MESSAGE);
								}
								
								break;
							}
						}
						
						
						JComboBox foreignComboBox = new JComboBox(new DefaultComboBoxModel(foreignKeyEntries.toArray(new String[0])));
						//foreignComboBox.setEditable(true);
						
						components[i][1] = foreignComboBox;
					} else {
						System.out.println("Not FK adding textfield");
						components[i][1] = new JTextField();
					}

				}

				int result = JOptionPane.showConfirmDialog(null, components, "Create Record",
						JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {

					String title = "Confirm Record Creation";
					String message = "You are about to add a new record to the \"" + selectedTable + "\" table.\n\n"
							+ "Are you sure you want to continue?";

					int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);

					if (reply == JOptionPane.YES_OPTION) {
						System.out.println("You entered: ");
						/*
						 * for (int i = 0; i < columnBoxes.size(); i++) {
						 * entries.add(columnBoxes.get(i).getText().toString());
						 * System.out.println(columnBoxes.get(i).getText().toString()); }
						 */

						for (int i = 0; i < components.length; i++) {

							if (Arrays.asList(foreignKeyNames).contains(columnNameList.get(i))) {
								JComboBox combo = (JComboBox) components[i][1];
								entries.add(combo.getSelectedItem().toString());
								System.out.println(combo.getSelectedItem().toString());
							} else {
								JTextField text = (JTextField) components[i][1];
								entries.add(text.getText().toString());
								System.out.println(text.getText().toString());
							}
							
						}

						try {
							db.insert(selectedTable, columnNameList.toArray(new String[0]),
									entries.toArray(new String[0]));

						} catch (SQLException eSQL) {
							System.out.println(eSQL.toString());
							JOptionPane.showMessageDialog(null, "There was an error connecting to the database.",
									"Connection Error", JOptionPane.ERROR_MESSAGE);
						}

						// Load the data into the JTable
						loadData(selectedTable);

					}

				} else {
					System.out.println("User canceled / closed the dialog, result = " + result);
				}
			}
		});
		panel.add(btnAddRecord);

		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		// table.setModel(new MyModel());
		// table.setEditable(false);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

		panelEdit = new JPanel();
		FlowLayout fl_panelEdit = (FlowLayout) panelEdit.getLayout();
		fl_panelEdit.setAlignment(FlowLayout.LEFT);
		panelEdit.setBackground(Color.LIGHT_GRAY);
		contentPane.add(panelEdit, BorderLayout.SOUTH);
		panelEdit.setVisible(false);

		btnDeleteRecord = new JButton("Delete Record");
		btnDeleteRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String title = "Delete Record";
				// String primaryKeyColName = primaryKeys[0];

				String[] primaryKeyColValues = new String[primaryKeys.length];
				for (int i = 0; i < primaryKeys.length; i++) {
					primaryKeyColValues[i] = table
							.getValueAt(selectedRowIndex, table.getColumn(primaryKeys[i]).getModelIndex()).toString();
				}

				// String selectedRowKeyId = table.getValueAt(selectedRowIndex,
				// table.getColumn(primaryKeys[0]).getModelIndex()).toString();

				String message = "You are about to delete the record containing \"" + primaryKeyColValues[0]
						+ "\" from \"" + primaryKeys[0] + "\" ";
				if (primaryKeys.length > 1) {
					for (int i = 1; i < primaryKeys.length; i++) {
						message += "AND \"" + primaryKeys[i] + "\" from \"" + primaryKeyColValues[i] + "\" ";
					}
				}

				message += " in the \"" + selectedTable + "\" table.\n\n"
						+ "This action can't be undone are you sure you want to continue?";

				int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);

				if (reply == JOptionPane.YES_OPTION) {
					// System.out.println(selectedRowKeyId);
					// System.out.println(selectedTable);
					// System.out.println(primaryKeyColName);
					try {
						db.delete(selectedTable, primaryKeys, primaryKeyColValues);
						panelEdit.setVisible(false);
						loadData(selectedTable);
					} catch (SQLException eSQL) {
						System.out.println(eSQL.toString());
						JOptionPane.showMessageDialog(null, "There was an error connecting to the database.",
								"Connection Error", JOptionPane.ERROR_MESSAGE);

					}

				}

			}
		});
		panelEdit.add(btnDeleteRecord);

		btnUpdateRecord = new JButton("Update Record");
		panelEdit.add(btnUpdateRecord);
		/*
		 * table.getModel().addTableModelListener(new TableModelListener() {
		 * 
		 * public void tableChanged(TableModelEvent args0) { // table.get
		 * System.out.println(args0); } });
		 */

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				// do some actions here, for example
				// print first column value from selected row

				selectedRowIndex = table.getSelectedRow();
				selectedColIndex = table.getSelectedColumn();

				if (selectedRowIndex == -1) {
					// selectedRowKeyId = "";
					panelEdit.setVisible(false);
					comboBoxFilter.setSelectedIndex(0);
					textFieldFilter.setText("");
				} else {
					// selectedRowKeyId = table.getValueAt(rowIndex, 0).toString();
					panelEdit.setVisible(true);
					comboBoxFilter.setSelectedIndex(selectedColIndex + 1);
					textFieldFilter.setText(table.getValueAt(selectedRowIndex, selectedColIndex).toString());
				}
				// System.out.println(selectedRow);
				// System.out.println(selectedTable);
				// System.out.println(firstColumnName);

			}

		});

		// frame.getRootPane().setDefaultButton(btnFilter);
		loadData(selectedTable);

		// Get the primary keys of a table
		try {
			// System.out.println("Load Primary Keys");
			primaryKeys = db.getPrimaryKeys(selectedTable);

			for (int i = 0; i < primaryKeys.length; i++) {
				// System.out.println(primaryKeys[i]);
			}

			foreignKeys = db.getForeignKeys(selectedTable);
			if (foreignKeys.length > 0) {
				for (int i = 0; i < foreignKeys.length; i++) {
					// System.out.println("fk:" + foreignKeys[i][0] + " and Parent: " +
					// foreignKeys[i][1]);
				}
			}
		} catch (SQLException eSQL) {
			System.out.println(eSQL.toString());
			JOptionPane.showMessageDialog(null, "There was an error connecting to the database.", "Connection Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/*
	 * Load Data uses the chosen table from the dropdown to pull the records for the
	 * chosen table from the database. There is an overload so that this can
	 * optionally be filtered. This window can be used to display all records.
	 */
	private void loadData(String dbTable) {
		loadData(dbTable, "", "");
	}

	private void loadData(String dbTable, String filterCol, String filterParam) {
		// LOG.info("START loadData method");
		System.out.println("START loadData method");

		String sqlCommand = "select * from " + dbTable + " ";

		// If the filter and param are set to "" then they were not set, so don't look
		// for them.
		if (!filterCol.equals("") && !filterParam.equals("")) {
			System.out.println("Load Filters");
			sqlCommand += "WHERE " + filterCol + " = '" + filterParam + "' ";
		}
		table.setRowSelectionAllowed(false);
		comboBoxModel.removeAllElements();
		comboBoxModel.addElement("Select a column");
		tableModel.setRowCount(0);

		// button.setEnabled(false);

		// Connect to db using credentials and prepared sql statement
		try (Connection conn = DriverManager.getConnection(db.getDbAddress(), db.getDbUser(), db.getDbPass());
				Statement stmt = conn.createStatement()) {

			ResultSet rs = stmt.executeQuery(sqlCommand);
			ResultSetMetaData metaData = rs.getMetaData();

			// Names of columns
			Vector<String> columnNames = new Vector<String>();
			int columnCount = metaData.getColumnCount();

			// To be used in another method

			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(metaData.getColumnName(i));
				columnNameList.add(metaData.getColumnName(i));
				comboBoxModel.addElement(metaData.getColumnName(i));
				columnTypeList.add(metaData.getColumnClassName(i));
				//System.out.println(metaData.getColumnClassName(i));
			}

			// Data of the table
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int i = 1; i <= columnCount; i++) {
					vector.add(rs.getObject(i));
				}
				data.add(vector);
			}

			// The jtable model is set to be the table model, and will reflect this after it
			// updates.
			tableModel.setDataVector(data, columnNames);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Out of bounds error: " + e.toString());
		} catch (Exception e) {
			// LOG.log(Level.SEVERE, "Exception in Load Data", e);
			System.out.println(e.toString());
		}

		// button.setEnabled(true);

		// LOG.info("END loadData method");
		table.setRowSelectionAllowed(true);

		System.out.println("END loadData method");
	}

	// Used to replace certain jtable default values
	public class MyModel extends DefaultTableModel {
		@Override
		public boolean isCellEditable(int row, int column) {
			// all cells false
			return false;
		}

	}
}

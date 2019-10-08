
//import java.io.IOException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
//import java.util.Scanner;
import java.util.Scanner;

public class DataBase {
	public static String dbName = "se_18f_g01_db";
	//public static String dbAddress = "jdbc:mysql://localhost:3306/" + dbName;
	// public static String dbAddress =
	// "jdbc:mysql://wyvern.cs.newpaltz.edu/se_18f_g01_db";
	// it doesn't like wyvern
	 public static String dbAddress =
	 "jdbc:mysql://cs.newpaltz.edu/se_18f_g01_db";
	public static String dbUser = "se_18f_g01";
	public static String dbPass = "wpdlm4";

	// Can be called by any other file to keep db variables in one location
	public static String getDbAddress() {
		return dbAddress;
	}

	public static String getDbUser() {
		return dbUser;
	}

	public static String getDbPass() {
		return dbPass;
	}

	// Used in GUI to validate user login
	public boolean login(String username, char[] password) throws SQLException {
		// Safeguard to prevent an empty password from allowing a login
		if (String.valueOf(password).equals("")) {
			return false;
		}

		String retrievedPass = "";

		String query = "SELECT * FROM login " + "WHERE username = \"" + username + "\" ";

		// Create database connection and save to ResultSet
		Connection myConn = DriverManager.getConnection(dbAddress, dbUser, dbPass);
		Statement myStmt = myConn.createStatement();
		ResultSet myRs = myStmt.executeQuery(query);

		// Set the retrieved password equal to a variable so we can close the
		// connection.
		if (myRs.next()) {
			retrievedPass = myRs.getString("password");

		}

		// close database connection
		myRs.close();
		myStmt.close();
		myConn.close();

		// If the password strings don't match then the user did not enter valid
		// credentials
		return String.valueOf(password).equals(retrievedPass);

	}

	// Used in RecordView to delete a single row ONLY
	//Overload allows for string or string[]
	public void delete(String tableName, String columnName , String columnValue) throws SQLException {
		String[] colArray = {columnName};
		String[] valArray = {columnValue};
		delete(tableName, colArray, valArray);
	}
	public void delete(String tableName, String[] columnName , String[] columnValue) throws SQLException {
		String query = "DELETE FROM " + tableName + " WHERE " + columnName[0] + " = \"" + columnValue[0] + "\" ";
		//If there is more than one primary key it is necessary to check for both values to delet only one row
		if(columnName.length > 1) {
			for(int i = 1; i < columnName.length; i++ ) {
				query += "AND " + columnName[i] + " = \"" + columnValue[1] + "\" "; 
			}
		}

		Connection myConn = DriverManager.getConnection(dbAddress, dbUser, dbPass);
		PreparedStatement preparedStmt = myConn.prepareStatement(query);
		preparedStmt.execute();

		myConn.close();

	}

	// Used in RecordView to insert a single row ONLY
	public void insert(String tableName, String[] columnNames, String[] columnValues) throws SQLException {
		String query = "INSERT INTO " + tableName + "(";
		for (int i = 0; i < columnNames.length; i++) {
			query += columnNames[i];
			if (i + 1 != columnNames.length) {
				query += ", ";
			}
		}
		query += ") VALUES (\"";

		for (int i = 0; i < columnValues.length; i++) {
			query += columnValues[i];
			if (i + 1 != columnValues.length) {
				query += "\", \"";
			}
		}

		query += "\") ";

		System.out.println(query);

		Connection myConn = DriverManager.getConnection(dbAddress, dbUser, dbPass);
		PreparedStatement preparedStmt = myConn.prepareStatement(query);
		preparedStmt.execute();

		myConn.close();

	}

	// Obtain a table's primary keys
	public String[] getPrimaryKeys(String table) throws SQLException {
		List<String> primaryKeys = new ArrayList();
		String query = "SHOW KEYS FROM " + table + " WHERE Key_name = 'PRIMARY'";
		// Create database connection and save to ResultSet
		Connection myConn = DriverManager.getConnection(dbAddress, dbUser, dbPass);
		Statement myStmt = myConn.createStatement();
		ResultSet myRs = myStmt.executeQuery(query);

		// Set the retrieved password equal to a variable so we can close the
		// connection.
		while (myRs.next()) {
			// System.out.println("primary key result: " + myRs.getString("column_name"));
			primaryKeys.add(myRs.getString("column_name").toLowerCase().trim());
		}

		// close database connection
		myRs.close();
		myStmt.close();
		myConn.close();

		return primaryKeys.toArray(new String[0]);
	}

	// Obtain a table's foreign keys and their parent tables
	public String[][] getForeignKeys(String table) throws SQLException {
		List<String> indexedKeys = new ArrayList();
		List<String> foreignColName = new ArrayList();
		List<String> foreignParents = new ArrayList();
		String[][] foreignKeys;

		String query = "SHOW KEYS FROM " + table + " WHERE Key_name != 'PRIMARY'";
		String queryTwo;

		// Create database connection and save to ResultSet
		Connection myConn = DriverManager.getConnection(dbAddress, dbUser, dbPass);
		Statement myStmt = myConn.createStatement();
		ResultSet myRs = myStmt.executeQuery(query);

		// First we need to retrieve the indexed keys.
		while (myRs.next()) {
			// System.out.println("indexed key result: " + myRs.getString("column_name"));
			indexedKeys.add(myRs.getString("column_name"));
		}

		// Then assuming there are indexed keys then those keys are foreign and we need
		// to use that to get their parent table
		if (indexedKeys.size() > 0) {
			queryTwo = "SELECT DISTINCT " + "  REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME " + "FROM "
					+ "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE " + "WHERE " + "REFERENCED_TABLE_SCHEMA = '" + dbName
					+ "' " + "AND REFERENCED_COLUMN_NAME = '" + indexedKeys.get(0) + "' ";
			for (int i = 1; i < indexedKeys.size(); i++) {
				queryTwo += "OR REFERENCED_COLUMN_NAME = '" + indexedKeys.get(i) + "' ";
			}

			myRs = myStmt.executeQuery(queryTwo);

			while (myRs.next()) {
				System.out.println("referenced column name result: " + myRs.getString("REFERENCED_COLUMN_NAME"));
				System.out.println("referenced column name result: " + myRs.getString("referenced_table_name"));
				foreignColName.add(myRs.getString("referenced_column_name"));
				foreignParents.add(myRs.getString("referenced_table_name"));
			}

			foreignKeys = new String[foreignColName.size()][2];

			for (int i = 0; i < foreignColName.size(); i++) {
				foreignKeys[i][0] = foreignColName.get(i).toLowerCase().trim();
				foreignKeys[i][1] = foreignParents.get(i).toLowerCase().trim();

			}
		}

		else {
			foreignKeys = new String[0][0];
		}

		System.out.println("FK Array size " + foreignKeys.length);

		// close database connection
		myRs.close();
		myStmt.close();
		myConn.close();

		return foreignKeys;
	}

	public void charterInput(String char_date, String ac_number, String char_destination, String char_distance)
			throws SQLException {

		String combining = "\"" + ac_number + "," + char_date + "," + char_destination + "," + char_distance + "\"";
		String query = "INSERT INTO charter (ac_number, char_date, char_destination, char_distance) VALUES(" + char_date
				+ ")";

		// Create database connection and save to ResultSet
		Connection myConn = DriverManager.getConnection(dbAddress, dbUser, dbPass);

		PreparedStatement preparedStmt = myConn.prepareStatement(query);
		preparedStmt.execute();
		// Statement myStmt = myConn.createStatement();
		// ResultSet myRs = myStmt.executeQuery(query);

		// close database connection
		// myRs.close();
		// myStmt.close();
		myConn.close();

	}

}

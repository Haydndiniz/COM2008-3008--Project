package Project_Code;

import javax.swing.*;
import java.sql.*;

/*
Database controller.
When main method is run, it displays all tables in the database
 */
public class DBController {
    private final String username;
    private final String password;
    private Statement stmt = null;
    private Connection connection = null;
    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk/team037";

    public DBController(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public void openConnection() {
        connection = null;
        try{
            connection = DriverManager.getConnection(DB_URL,username, password);
        }
        catch (SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

    }

    public void closeConnection() throws SQLException{
        if (connection!=null)
            connection.close();
    }

    public Statement getStatement() {
        return stmt;
    }

    public ResultSet performQuery(String query) {
        openConnection();
        ResultSet result = null;
        stmt = null;

        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery(query);
            return result;
        }
        catch (SQLException | NullPointerException ex){
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return result;

    }

    public static void main(String[] args) throws SQLException {
        DBController con = new DBController("team037","ee143bc0");
        //show the tables in the database
        ResultSet tables = con.performQuery("SHOW TABLES");

        while (tables.next()) {
            System.out.println(tables.getString(1));

        }

    }

}

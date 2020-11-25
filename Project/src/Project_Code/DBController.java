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
    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk/team037";

    public DBController(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ResultSet performQuery(String query) throws SQLException {

        ResultSet result = null;
        stmt = null;

        try (Connection con = DriverManager.getConnection(DB_URL, username, password)) {
            System.out.print("Connection was created\n");

            try {
                stmt = con.createStatement();
                result = stmt.executeQuery(query);

                while (result.next()) {
                    System.out.println(result.getString(1));
                }

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
        catch (Exception ex) {
            //display error message and leave the application
            System.out.print("Failed at creating connection.");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        finally {
            if (stmt != null) stmt.close();
        }

        return result;

    }

    public static void main(String[] args) throws SQLException {

        DBController con = new DBController("team037","ee143bc0");

        ResultSet tables = con.performQuery("SHOW TABLES");

    }

}

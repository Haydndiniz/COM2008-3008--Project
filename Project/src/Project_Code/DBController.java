package Project_Code;

import javax.swing.*;
import java.sql.*;

/*
Database controller.
When main method is run, it displays all tables in the database
 */
public class DBController {

    private String username;
    private String password;
    private Statement stmt = null;
    private Connection con = null;
    private ResultSet result;
    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk/team037";

    public DBController(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public Connection getConnection () {
        return con;
    }

    /**
     *
     * @return the statement
     */
    public Statement getStatement() {
        return stmt;
    }

    public void setStatement(Statement statement) {
        this.stmt = statement;
    }

    public void setConnection(Connection con) {
        this.con = con;
    }

    public void openConnection() throws SQLException{
        con = null;

        try{
            con = DriverManager.getConnection(DB_URL,username, password);
        }
        catch (SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

    }

    /**
     * close the connection
     * @throws SQLException
     */
    public void closeConnection() throws SQLException{
        if (con!=null)
            con.close();
    }

    /**
     * close the statement
     */
    public void closeStatement() throws SQLException{
        if (stmt!=null)
            stmt.close();
    }
    /**
     * execute an Update Query
     * @return number of rows updated
     */
    public int performUpdate(String update) throws SQLException {
        openConnection();
        int updates = 0;
        try {
            stmt=con.createStatement();
            updates = stmt.executeUpdate(update);
            return updates;
        }
        catch (SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return updates;
    }

    /**
     * execute a Query
     * @return ResultSet the results
     */
    public ResultSet performQuery(String query) throws SQLException{
        openConnection();
        result = null;
        stmt = null;
        try {
            stmt = con.createStatement();
            result = stmt.executeQuery(query);
            return result;
        }
        catch (SQLException ex){
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        catch (NullPointerException nex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        closeConnection();
        closeStatement();
        return result;

    }

    /**
     * execute a Prepared Statement
     * @return PreparedStatement
     */
    public PreparedStatement getPreparedStatement(String query) throws SQLException{
        openConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(query);
            return pstmt;
        }
        catch (SQLException ex){
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        catch (NullPointerException nex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        closeConnection();
        return pstmt;

    }
    public ResultSet performPreparedStatement(PreparedStatement pstmt) throws SQLException{
        openConnection();
        result = null;
        try {
            result = pstmt.executeQuery();
            return result;
        }
        catch (SQLException ex){
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        catch (NullPointerException nex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        closeConnection();
        pstmt.close();
        return result;
    }
    public int performPreparedUpdate(PreparedStatement pstmt) throws SQLException {
        openConnection();
        int updates = 0;
        try {
            updates = pstmt.executeUpdate();
            return updates;
        }
        catch (SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return updates;
    }

    public static void main(String[] args) throws SQLException {

        DBController con = new DBController("team037","ee143bc0");
        String xy = "admin";
        //Test Queries Here
        ResultSet tables = con.performQuery("SELECT password FROM UserAccounts WHERE username = '" + xy + "'");
        while (tables.next()) {
            System.out.println(tables.getString(1));
        }
        System.out.println(tables);
    }

}
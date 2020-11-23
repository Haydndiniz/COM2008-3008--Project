import java.sql.*;

import javax.swing.JOptionPane;

/**
 * class DataAccessController
 * class responsible for controlling access of data
 * contains get and set methods to access and modify data
 * contains functions for opening and closing connections
 * contains functions for performing queries
 * method for creating tables in the database - commented out
 */

public class Main {
    private String name;
    private String username;
    private String password;
    private Connection connection = null;
    private Statement stmt = null;
    private ResultSet result;
    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk/team042";

    /**
     * Constructor - create a DataAccessController object
     * @param name
     * @param username
     * @param password
     */
    public Main(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    //get Methods
    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return the connection object
     */
    public Connection getConnection () {
        return connection;
    }

    /**
     *
     * @return the statement
     */
    public Statement getStatement() {
        return stmt;
    }


    //set methods
    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatement(Statement statement) {
        this.stmt = statement;
    }

    public void setConnection(Connection con) {
        this.connection = con;
    }

    /**
     *
     * open a connection
     * @throws SQLException
     */
    public void openConnection() throws SQLException{
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

    /**
     * close the connection
     * @throws SQLException
     */
    public void closeConnection() throws SQLException{
        if (connection!=null)
            connection.close();
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
            stmt=connection.createStatement();
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
            stmt = connection.createStatement();
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
            pstmt = connection.prepareStatement(query);
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

    //method called for creating the tables in the database
    //commented out
    /*public int[] createTables() throws SQLException {
        openConnection();
        stmt = null;
        int [] updateCounts = null;
        try {
            stmt=connection.createStatement();
            stmt.addBatch("CREATE TABLE UserAccounts ("
                            +"username VARCHAR(20) NOT NULL PRIMARY KEY,"
                            +"password VARCHAR(50) NOT NULL);");

            stmt.addBatch("CREATE TABLE Roles ("
                            +"username VARCHAR(20) NOT NULL PRIMARY KEY,"
                            +"role VARCHAR(50) NOT NULL,"
                            +"FOREIGN KEY (username) REFERENCES UserAccounts(username));");
           stmt=connection.createStatement();
           stmt.addBatch("CREATE TABLE Departments ( "
                   + "deptCode VARCHAR(6) NOT NULL PRIMARY KEY, "
                   + "name VARCHAR(30))");


           stmt.addBatch("CREATE TABLE Degrees ( "
                   + "degreeCode VARCHAR(6) NOT NULL PRIMARY KEY,"
                   + "name VARCHAR(50));");

           stmt.addBatch("CREATE TABLE DegreeApproval ( "
                   + "deptCode VARCHAR(6) NOT NULL,"
                   + "degreeCode VARCHAR(6) NOT NULL,"
                   + "lead BOOL DEFAULT FALSE, "
                   + "PRIMARY KEY (deptCode, degreeCode),"
                   + "FOREIGN KEY (deptCode) REFERENCES Departments(deptCode),"
                   + "FOREIGN KEY (degreeCode) REFERENCES Degrees(degreeCode));");

           stmt.addBatch("CREATE TABLE Level ( "
                   + "levelCode VARCHAR(1) NOT NULL,"
                   + "name VARCHAR(30),"
                   + "PRIMARY KEY (levelCode))");
           stmt.addBatch("CREATE TABLE LevelApprove ( "
                   + "degreeCode VARCHAR(6) NOT NULL,"
                   + "levelCode VARCHAR(1) NOT NULL,"
                   + "PRIMARY KEY (degreeCode, levelCode),"
                   + "FOREIGN KEY (degreeCode) REFERENCES Degrees(degreeCode),"
                   + "FOREIGN KEY (levelCode) REFERENCES Level(levelCode));");

           stmt.addBatch("CREATE TABLE Modules ( "
                   + "moduleCode VARCHAR(7) NOT NULL PRIMARY KEY, "
                   + "deptCode VARCHAR(6),"
                   + "name VARCHAR(100),"
                   + "credits INT(3),"
                   + "periodCode VARCHAR(40),"
                   + "FOREIGN KEY (deptCode) REFERENCES Departments(deptcode));");


           stmt.addBatch("CREATE TABLE ModuleApprove ( "
                   +"moduleCode VARCHAR(7) NOT NULL,"
                   +"degreeCode VARCHAR(6) NOT NULL,"
                   +"levelCode VARCHAR(1) NOT NULL,"
                   +"core BOOL DEFAULT FALSE,"
                   +"PRIMARY KEY (moduleCode, degreeCode, levelCode),"
                   +"FOREIGN KEY (moduleCode) REFERENCES Modules(moduleCode),"
                   +"FOREIGN KEY (degreeCode) REFERENCES Degrees(degreeCode),"
                   +"FOREIGN KEY (levelCode) REFERENCES Level(levelCode));");


           stmt.addBatch("CREATE TABLE Students ( "
                   +"regNo INT(10) NOT NULL PRIMARY KEY,"
                   +"degreeCode VARCHAR(6) NOT NULL,"
                   +"email VARCHAR(20) NOT NULL,"
                   +"title VARCHAR(5),"
                   +"surname VARCHAR(20),"
                   +"forename VARCHAR(50),"
                   +"tutor VARCHAR(50),"
                   +"degreeClass VARCHAR(50),"
                   +"FOREIGN KEY (degreeCode) REFERENCES Degrees(degreeCode));");


           stmt.addBatch("CREATE TABLE Period ("
                   +"periodLabel VARCHAR(1) NOT NULL,"
                   +"regNo INT(10) NOT NULL,\r\n"
                   +"levelCode VARCHAR(1) NOT NULL,"
                   +"startDate DATE,"
                   +"endDate DATE,"
                   +"outcome VARCHAR(20),"
                   +"meanGrade FLOAT,"
                   +"PRIMARY KEY (periodLabel, regNo),"
                   +"FOREIGN KEY (regNo) REFERENCES Students(regNo),"
                   +"FOREIGN KEY (levelCode) REFERENCES Level(levelCode));");


           stmt.addBatch("CREATE TABLE StudyGrades ("
                   +"regNo INT(10) NOT NULL,"
                   +"moduleCode VARCHAR(7) NOT NULL,"
                   +"periodLabel VARCHAR(1) NOT NULL,"
                   +"initGrade FLOAT,"
                   +"resitGrade FLOAT,"
                   +"PRIMARY KEY (regNo, moduleCode, periodLabel),"
                   +"FOREIGN KEY (regNo) REFERENCES Students(regNo),"
                   +"FOREIGN KEY (moduleCode) REFERENCES Modules(moduleCode),"
                   +"FOREIGN KEY (periodLabel) REFERENCES Period(periodLabel));");
           updateCounts = stmt.executeBatch();
           return updateCounts;
        }
        catch (BatchUpdateException bx) {
            bx.printStackTrace();
        }
        catch (SQLException ex) {
               ex.printStackTrace();
        }
        closeConnection();
        closeStatement();
        return updateCounts;
    }*/


    /**
     * Main method used for testing and gathering Database information
     * @param args
     * @throws SQLException
     */
    public static void main (String [] args) throws SQLException {
        Main dac = new Main("team037","team037","ee143bc0");
        //show the tables in the database
        ResultSet tables = dac.performQuery("SHOW TABLES");

        while (tables.next()) {
            System.out.println(tables.getString(1));

        }

    }


}
package Project_Code.Admin;

import Project_Code.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminMain inherits user
 * Implements all actions carried out by admin role
 */
public class AdminMain extends User {


    private DBController dac = super.getDataAccessController();

    public AdminMain(String username) {
        super(username, "admin");
    }

    /**
     * viewAllUsers
     * @return a table with all usernames and roles
     */
    public DefaultTableModel viewAllUsers (){
        ResultSet result = null;
        DefaultTableModel t = new DefaultTableModel(new Object[][] {},new String[] {"Username", "Role"});
        //select all the users with permissions
        Object [] users = new Object[2];
        try {
            result = dac.performQuery("SELECT * FROM Users");
            while (result.next()) {
                users[0] = result.getString(1);
                users[1] = result.getString(2);
                t.addRow(users);
            }
            //close connection and statement
            dac.closeConnection();
            dac.closeStatement();
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return  t;
    }

    /**
     * viewAllDepartments
     * @return a table with all department code and names
     */
    public DefaultTableModel viewAllDepartments (){
        ResultSet result = null;
        DefaultTableModel t = new DefaultTableModel(new Object[][] {},new String[] {"Department Code", "Department Name"});

        Object [] departments = new Object[2];
        try {
            result = dac.performQuery("SELECT * FROM Department");
            while (result.next()) {
                departments[0] = result.getString(1);
                departments[1] = result.getString(2);
                t.addRow(departments);
            }
            //close connection and statement
            dac.closeConnection();
            dac.closeStatement();
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return  t;
    }

    /**
     * viewAllCourses
     * @return a table with all department code and names
     */
    public DefaultTableModel viewAllCourses (){
        ResultSet result = null;
        DefaultTableModel t = new DefaultTableModel(new Object[][] {},new String[] {"Course Code", "Course Name"});

        Object [] courses = new Object[2];
        try {
            result = dac.performQuery("SELECT * FROM Degree");
            while (result.next()) {
                courses[0] = result.getString(1);
                courses[1] = result.getString(2);
                t.addRow(courses);
            }
            //close connection and statement
            dac.closeConnection();
            dac.closeStatement();
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return  t;
    }

    /**
     * viewCoursesPerDepartment
     * @return a table with all department code and names
     */
    public DefaultTableModel viewCoursesPerDepartment (){
        ResultSet result = null;
        DefaultTableModel t = new DefaultTableModel(new Object[][] {},new String[] {"Department Code", "Course Code"});

        Object [] coursePerDepartment = new Object[2];
        try {
            result = dac.performQuery("SELECT * FROM DegreeApproval");
            while (result.next()) {
                coursePerDepartment[0] = result.getString(1);
                coursePerDepartment[1] = result.getString(2);
                t.addRow(coursePerDepartment);
            }
            //close connection and statement
            dac.closeConnection();
            dac.closeStatement();
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return  t;
    }

    /**
     * viewAllModules
     * @return a table with all department code and names
     */
    public DefaultTableModel viewAllModules (){
        ResultSet result = null;
        DefaultTableModel t = new DefaultTableModel(new Object[][] {},new String[] {"Module Code", "Module Name", "Credits", "Period"});

        Object [] modules = new Object[5];
        try {
            result = dac.performQuery("SELECT * FROM Module");
            while (result.next()) {
                modules[0] = result.getString(1);
                modules[1] = result.getString(2);
                modules[2] = result.getString(3);
                modules[3] = result.getString(4);
                t.addRow(modules);
            }
            //close connection and statement
            dac.closeConnection();
            dac.closeStatement();
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return  t;
    }

}

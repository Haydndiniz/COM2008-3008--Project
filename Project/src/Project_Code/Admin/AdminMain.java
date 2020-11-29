package Project_Code.Admin;

import Project_Code.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminMain inherits user
 * Implements all actions carried out by Admin role
 */
public class AdminMain extends User {


    private DBController dac = super.getDataAccessController();

    public AdminMain(String username) {
        super(username, "Admin");
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

}

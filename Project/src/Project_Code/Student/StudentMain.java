package Project_Code.Student;

import Project_Code.DBController;
import Project_Code.User;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentMain extends User {

    private DBController dac = super.getDataAccessController();

    public StudentMain(String username) {
        super(username, "Student");
    }
    public String[] getPeriodDetails(String regNo, String periodLabel){
        //Method assumes there is only one match
        ResultSet result = null;
        String[] results = new String[2];
        try {
            result = dac.performQuery("SELECT * FROM Period WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            while (result.next()) {
                results[0] =  result.getString("meanGrade");
                results[1] =  result.getString("outcome");
            }
            dac.closeConnection();
            dac.closeStatement();
            return results;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getDegreeClass(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        String degreeClass = null;
        try {
            result = dac.performQuery("SELECT * FROM Student WHERE registrationNo='"+regNo+"'");
            while (result.next()) {
                degreeClass = result.getString("degreeClass");
            }
            dac.closeConnection();
            dac.closeStatement();
            return degreeClass;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
}

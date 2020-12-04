package Project_Code.Teacher;

import Project_Code.*;

import javax.swing.*;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TeacherMain inherits user
 * Implements all actions carried out by Teacher role
 */
public class TeacherMain extends User {


    private DBController dac = super.getDataAccessController();

    public TeacherMain(String username) {
        super(username, "Teacher");
    }
    /**
     * searchByStudent
     * @return true if a single student is found, false otherwise
     */
    public boolean searchByStudent (String value, boolean username){
        ResultSet result = null;
        try {
            String query = "";
            if (username){
                query = "SELECT COUNT(*) FROM Student WHERE username=?";
            }
            else{
                query = "SELECT COUNT(*) FROM Student WHERE registrationNo=?";
            }
            PreparedStatement pstmt = dac.getPreparedStatement(query);
            pstmt.setString(1, value);
            result = dac.performPreparedStatement(pstmt);
            int n = 0;
            while (result.next()) {
                n = result.getInt(1);
            }
            if (n > 1){
                // Multiple results
                JOptionPane.showMessageDialog(null,"An error has occurred, please contact the admin",
                        "ERROR", JOptionPane.ERROR_MESSAGE, null);
                return false;
            }
            else if (n == 1){
                // 1 Result
                return true;
            }
            else {
                // No results
                JOptionPane.showMessageDialog(null,"No results found",
                        "ERROR", JOptionPane.ERROR_MESSAGE, null);
                return false;
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        return false;
    }
    /**
     * getRegNoFromUsername
     * @return registration number of a student, given their username
     */
    public String getRegNoFromUsername(String username){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Student WHERE username=?");
            pstmt.setString(1, username);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                return result.getString("registrationNo");
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String[] getStudentDetails(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        String[] results = new String[2];
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Student WHERE registrationNo=?");
            pstmt.setString(1, regNo);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                results[0] =  result.getString("username");
                results[1] =  result.getString("degreeCode");
            }
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
    public String[] getUserDetails(String username){
        //Method assumes there is only one match
        ResultSet result = null;
        String[] results = new String[2];
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Users WHERE username=?");
            pstmt.setString(1, username);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                results[0] =  result.getString("forename");
                results[1] =  result.getString("surname");
            }
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
    public String getUsername(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Student WHERE registrationNo=?");
            pstmt.setString(1, regNo);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                return result.getString("username");
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getDegreeCode(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Student WHERE registrationNo=?");
            pstmt.setString(1, regNo);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                return result.getString("degreeCode");
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getFirstName(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Users WHERE username=?");
            pstmt.setString(1, getUsername(regNo));
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                return result.getString("forename");
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getLastName(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Users WHERE username=?");
            pstmt.setString(1, getUsername(regNo));
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                return result.getString("surname");
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getLevel(String regNo, String periodLabel){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Period WHERE registrationNo=? AND periodLabel=?");
            pstmt.setString(1, regNo);
            pstmt.setString(2, periodLabel);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                return result.getString("levelCode");
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public List<String> getStudentPeriods(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        List<String> periodList = new ArrayList<>();
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Period WHERE registrationNo=?");
            pstmt.setString(1, regNo);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                periodList.add(result.getString("periodLabel"));
            }
            return periodList;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public List<String> getStudentModules(String regNo, String periodLabel){
        //Method assumes there is only one match
        ResultSet result = null;
        List<String> moduleList = new ArrayList<>();
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Study WHERE registrationNo=? AND periodLabel=?");
            pstmt.setString(1, regNo);
            pstmt.setString(2, periodLabel);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                moduleList.add(result.getString("moduleCode"));
            }
            return moduleList;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String[] getGrades(String regNo, String moduleCode, String periodLabel){
        //Method assumes there is only one match
        ResultSet result = null;
        String[] results = new String[2];
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Study WHERE registrationNo=? AND moduleCode=? AND periodLabel=?");
            pstmt.setString(1, regNo);
            pstmt.setString(2, moduleCode);
            pstmt.setString(3, periodLabel);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                results[0] =  result.getString("initialGrade");
                results[1] =  result.getString("resitGrade");
            }
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
    public void updateGrades(String regNo, String moduleCode, String periodLabel, String initGrade, String resitGrade){
        //Method assumes there is only one match
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("UPDATE Study SET initialGrade=?, resitGrade=? WHERE registrationNo=? AND moduleCode=? AND periodLabel=?");
            pstmt.setString(1, initGrade);
            pstmt.setString(2, resitGrade);
            pstmt.setString(3, regNo);
            pstmt.setString(4, moduleCode);
            pstmt.setString(5, periodLabel);
            dac.performPreparedUpdate(pstmt);
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
    }
    public Double getMeanGrade(String regNo, String periodLabel){
        //Method assumes there is only one match
        ResultSet result = null;
        List<String> initGrades = new ArrayList<>();
        List<String> resitGrades = new ArrayList<>();
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Study WHERE registrationNo=? AND periodLabel=?");
            pstmt.setString(1, regNo);
            pstmt.setString(2, periodLabel);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                initGrades.add(result.getString("initialGrade"));
                resitGrades.add(result.getString("resitGrade"));
            };
            Double initSum = 0.0;
            Double meanGrade;
            int count = 0;
            for (String i:initGrades){
                if (resitGrades.get(count) != null){
                    if(Double.parseDouble(resitGrades.get(count)) >= 39.5){
                        initSum+=40;
                    }
                }
                else{
                    if (i != null){
                        initSum += Double.parseDouble(i);
                    }
                    else{
                        return null;
                    }
                }
                count++;
            }
            meanGrade = initSum / initGrades.toArray().length;
            return round(meanGrade, 1);
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getOutcome(String regNo,String periodLabel,String levelCode,String degreeCode,double meanGrade) {
        //check if it is 1-year MSc
        if (degreeCode.substring(3,4).equals("P")) {
            //check if student failed dissertation but succeeded in all the other modules
            double disertationGrade=0;
            int studentTotalCredits=0;
            //for (Map.Entry<String, Double> entry : recModules.entrySet()) {
            //    int credit=moduleCredits.get(entry.getKey());
            //    if (credit==60) {
            //        //get the grade for the dissertation module
            //        disertationGrade=entry.getValue();
            //    }
            //   if (entry.getValue()>50) {
            //        studentTotalCredits+=credit;
            //    }
            //}
            //Special rule if student passes modules but fails dissertaion
            if ((studentTotalCredits==120) &&(disertationGrade<50)) { //pass mark is 50
                return "PGDip";
            }
            //Special rule if student passes 60 credits of modules
            if (studentTotalCredits==60) {
                return "PGCert";
            }

            if (meanGrade>=69.5) {
                return "distinction";
            }
            else if (meanGrade>=59.5) {
                return "merit";
            }
            else if (meanGrade>=49.5) {
                return "pass";
            }
            else {
                return "fail";
            }
        }
        //check if BSc,BEng degree
        else if (levelCode.equals("3") || levelCode.equals("2") || levelCode.equals("1")) {
            if (((int)periodLabel.charAt(0)-64)-Integer.parseInt(levelCode)>=2) {
                return "pass (non-honours)";
            }
            if (meanGrade>=69.5) {
                return "first class";
            }
            else if (meanGrade>=59.5) {
                return "upper second";
            }
            else if (meanGrade>=49.5) {
                return "lower second";
            }
            else if (meanGrade>=44.5){
                return "third class";
            }
            else if (meanGrade>=39.5){
                return "pass (non-honours)";
            }
            else{
                return "fail";
            }
        }
        //else MComp/MEng degree
        else {
            if (meanGrade>=69.5) {
                return "first class";
            }
            else if (meanGrade>=59.5) {
                return "upper second";
            }
            else if (meanGrade>=49.5) {
                return "lower second";
            }
            else {
                return "fail";
            }
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

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
    public String getUpperLevelCode(String degreeCode){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM ModuleApproval WHERE degreeCode=?");
            pstmt.setString(1, degreeCode);
            result = dac.performPreparedStatement(pstmt);
            int maxLevel = 1;
            int n = 0;
            while (result.next()) {
                if (result.getString("levelCode") == "P"){
                    continue;
                }
                n++;
                int num = Integer.parseInt(result.getString("levelCode"));
                if (num > maxLevel){
                    maxLevel = num;
                }
            }
            if (n > 0){
                //If a value exists
                return String.valueOf(maxLevel);
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
    public String[] getMeanGrade(String regNo, String periodLabel, String levelCode){
        //Method assumes there is only one match
        ResultSet result = null;
        String[] returnArr = new String[2];
        List<List<String>> grades = new ArrayList<>();
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT * FROM Study WHERE registrationNo=? AND periodLabel=?");
            pstmt.setString(1, regNo);
            pstmt.setString(2, periodLabel);
            result = dac.performPreparedStatement(pstmt);
            while (result.next()) {
                String initGrade = result.getString("initialGrade");
                String resitGrade = result.getString("resitGrade");
                List<String> combinedGrades = new ArrayList<>();
                combinedGrades.add(initGrade);
                combinedGrades.add(resitGrade);
                grades.add(combinedGrades);
            };
            Double initSum = 0.0;
            Double meanGrade;
            String checkPass = "pass";
            Integer concededModules = 0;
            for (List<String> i:grades){
                Double resitGrade;
                Double higherGrade;
                if (i.get(0) == null){
                    //If any initial grade is null, the outcome will be null
                    return null;
                }
                if (i.get(1) == null){
                    //If a resit grade is null, its value can be set to 0
                    resitGrade = 0.0;
                }
                else{
                    resitGrade = Double.parseDouble(i.get(1));
                }
                Double initGrade = Double.parseDouble(i.get(0));
                int passMark = 40;
                if (levelCode == "4"){
                    passMark = 50;
                }
                if (initGrade > resitGrade){
                    initSum += initGrade;
                    higherGrade = initGrade;
                }
                else{
                    initSum += Math.min(resitGrade, passMark);
                    higherGrade = resitGrade;
                }
                if (higherGrade < passMark - 10){
                    //Always fail
                    checkPass = "fail";
                }
                else if(higherGrade < passMark){
                    //Conceded module
                    concededModules++;
                }
                if (concededModules > 1){
                    //Always fail
                    checkPass = "fail";
                }
                else if (concededModules == 1 && checkPass != "fail"){
                    //Conceded pass
                    checkPass = "conceded";
                }
            }
            meanGrade = initSum / grades.toArray().length;
            PreparedStatement pstmt2 = dac.getPreparedStatement("UPDATE Period SET meanGrade=? WHERE registrationNo=? AND periodLabel=?");
            pstmt2.setString(1, String.valueOf(meanGrade));
            pstmt2.setString(2, regNo);
            pstmt2.setString(3, periodLabel);
            dac.performPreparedUpdate(pstmt2);
            returnArr[0] = String.valueOf(round(meanGrade, 1));
            returnArr[1] = checkPass;
            return returnArr;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String getStudentHighestLevel(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT MAX(levelCode) FROM Period WHERE registrationNo=? AND levelCode='1' OR levelCode='2' OR levelCode='3'");
            pstmt.setString(1, regNo);
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
    public String[] getOverallGrade(String regNo, String upperLevel){
        //Method assumes there is only one match
        ResultSet result = null;
        String[] returnArr = new String[2];
        List<List<String>> periods = new ArrayList<>();
        try {
            PreparedStatement pstmt = dac.getPreparedStatement("SELECT COUNT(*) FROM Period WHERE registrationNo=? AND levelCode='2' UNION " +
                    "SELECT COUNT(*) FROM Period WHERE registrationNo=? AND levelCode='3' UNION " +
                    "SELECT COUNT(*) FROM Period WHERE registrationNo=? AND levelCode='4'");
            pstmt.setString(1, regNo);
            pstmt.setString(2, regNo);
            pstmt.setString(3, regNo);;
            result = dac.performPreparedStatement(pstmt);
            int total = 0;
            while (result.next()) {
                List<String> combinedPeriodResit = new ArrayList<>();
                int current = Integer.parseInt(result.getString(1));
                if (current != 0){
                    total += current;
                    combinedPeriodResit.add(getCharForNumber(total));
                    if (current == 1){
                        //no resit
                        combinedPeriodResit.add("1");
                    }
                    else {
                        //current == 2, level was resat
                        combinedPeriodResit.add("2");
                    }
                    periods.add(combinedPeriodResit);
                }
            }
            if (periods.toArray().length != Integer.parseInt(upperLevel) - 1){
                //Final year of course not reached
                return new String[]{null, null};
            }
            double sumGrade = 0;
            double divisor = 0;
            int passMark = 40;
            returnArr[1] = "pass";
            for (List<String> i : periods){
                String level = getLevel(regNo, i.get(0));
                boolean resit = false;
                if (i.get(1) == "1"){
                    resit = false;
                }
                else if (i.get(1) == "2"){
                    resit = true;
                }
                String[] gradeArr = getMeanGrade(regNo, i.get(0), level);
                if (gradeArr[1] == "fail"){
                    returnArr[1] = "fail";
                }
                if (level == "2"){
                    //Year 2
                    if (resit){
                        sumGrade += Integer.parseInt(gradeArr[0]);
                    }
                    else{
                        sumGrade += Math.min(Integer.parseInt(gradeArr[0]), passMark);
                    }
                    divisor++;
                }
                else{
                    //Year 3/4
                    if (level == "4"){
                        passMark = 50;
                    }
                    if (true){
                        sumGrade += 2 * Integer.parseInt(gradeArr[0]);
                    }
                    else{
                        sumGrade += 2 * Math.min(Integer.parseInt(gradeArr[0]), passMark);
                    }
                    divisor+=2;
                }
            }
            returnArr[0] = String.valueOf(sumGrade/divisor);
            return returnArr;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return new String[]{null, null};
    }
    public String getOutcome(String regNo, String periodLabel,String levelCode,String degreeCode,Double meanGrade, String checkPass, boolean degreeClass) {
        //check if it is 1-year MSc
        String periodOutcome;
        if (meanGrade == null){
            return null;
        }
        else if (checkPass == "fail"){
            periodOutcome = "fail";
        }
        else if (checkPass == "conceded"){
            periodOutcome = "conceded pass";
        }
        else if (degreeCode.substring(3,4).equals("P")) {
            //check if student failed dissertation but succeeded in all the other modules
            double disertationGrade=0;
            int studentTotalCredits=0;
            for (Map.Entry<String, Double> entry : dac.recModules.entrySet()) {
                int credit=dac.moduleCredits.get(entry.getKey());
                if (credit==60) {
                    //get the grade for the dissertation module
                    disertationGrade=entry.getValue();
                }
               if (entry.getValue()>50) {
                    studentTotalCredits+=credit;
                }
            }
            //Special rule if student passes modules but fails dissertaion
            if ((studentTotalCredits==120) &&(disertationGrade<50)) { //pass mark is 50
                periodOutcome = "PGDip";
            }
            //Special rule if student passes 60 credits of modules
            if (studentTotalCredits==60) {
                periodOutcome = "PGCert";
            }

            if (meanGrade>=69.5) {
                periodOutcome = "distinction";
            }
            else if (meanGrade>=59.5) {
                periodOutcome = "merit";
            }
            else if (meanGrade>=49.5) {
                periodOutcome = "pass";
            }
            else {
                periodOutcome = "fail";
            }
        }
        //check if BSc,BEng degree
        else if (levelCode.equals("3") || levelCode.equals("2") || levelCode.equals("1")) {
            if (((int)periodLabel.charAt(0)-64)-Integer.parseInt(levelCode)>=2) {
                return "pass (non-honours)";
            }
            if (meanGrade>=69.5) {
                periodOutcome = "first class";
            }
            else if (meanGrade>=59.5) {
                periodOutcome = "upper second";
            }
            else if (meanGrade>=49.5) {
                periodOutcome = "lower second";
            }
            else if (meanGrade>=44.5){
                periodOutcome = "third class";
            }
            else if (meanGrade>=39.5){
                periodOutcome = "pass (non-honours)";
            }
            else{
                periodOutcome = "fail";
            }
        }
        //else MComp/MEng degree
        else {
            if (meanGrade>=69.5) {
                periodOutcome = "first class";
            }
            else if (meanGrade>=59.5) {
                periodOutcome = "upper second";
            }
            else if (meanGrade>=49.5) {
                periodOutcome = "lower second";
            }
            else {
                periodOutcome = "fail";
            }
        }
        try{
            if (!degreeClass){
                //For period outcomes,
                if (periodOutcome != "fail"){
                    if (periodOutcome != "conceded pass"){
                        periodOutcome = "pass";
                    }
                }
                PreparedStatement pstmt = dac.getPreparedStatement("UPDATE Period SET outcome=? WHERE registrationNo=? AND periodLabel=?");
                pstmt.setString(1, periodOutcome);
                pstmt.setString(2, regNo);
                pstmt.setString(3, periodLabel);
                dac.performPreparedUpdate(pstmt);
            }
            else{
                //For degree classes (meanGrade would be adjusted to the correct value for all levels/weightings)
                PreparedStatement pstmt = dac.getPreparedStatement("UPDATE Student SET degreeClass=? WHERE registrationNo=??");
                pstmt.setString(1, periodOutcome);
                pstmt.setString(2, regNo);
                dac.performPreparedUpdate(pstmt);
            }
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return periodOutcome;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }
}

package Project_Code.Teacher;

import Project_Code.*;
import Project_Code.DBController;
import Project_Code.Registrar.AddNewStudent;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.sun.scenario.effect.impl.prism.PrReflectionPeer;

import javax.swing.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TeacherMain inherits user
 * Implements all actions carried out by Teacher role
 */
public class TeacherMain extends User {

    private DBController con = new DBController("team037", "ee143bc0");

    public TeacherMain(String username, String role) {
        super(username, role);
    }
    /**
     * searchByStudent
     * @return true if a single student is found, false otherwise
     */
    public boolean searchByStudent (String value, boolean username){
        ResultSet result = null;
        value = value.replaceAll("\\p{Punct}", "");
        try {
            String query = "";
            if (username){
                query = "SELECT COUNT(*) FROM Student WHERE username='"+value+"'";
            }
            else{
                query = "SELECT COUNT(*) FROM Student WHERE registrationNo='"+value+"'";
            }
            result = con.performQuery(query);
            int n = 0;
            while (result.next()) {
                n = result.getInt(1);
            }
            con.closeConnection();
            con.closeStatement();
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
        String regNo = null;
        try {
            result = con.performQuery("SELECT * FROM Student WHERE username='"+username+"'");
            while (result.next()) {
                regNo = result.getString("registrationNo");
                break;
            }
            con.closeConnection();
            con.closeStatement();
            return regNo;
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
            result = con.performQuery("SELECT * FROM Student WHERE registrationNo='"+regNo+"'");
            while (result.next()) {
                results[0] =  result.getString("username");
                results[1] =  result.getString("degreeCode");
            }
            con.closeConnection();
            con.closeStatement();
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
            result = con.performQuery("SELECT * FROM Users WHERE username='"+username+"'");
            while (result.next()) {
                results[0] =  result.getString("forename");
                results[1] =  result.getString("surname");
            }
            con.closeConnection();
            con.closeStatement();
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
        String levelCode = null;
        try {
            result = con.performQuery("SELECT * FROM Period WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            while (result.next()) {
                levelCode = result.getString("levelCode");
                break;
            }
            con.closeConnection();
            con.closeStatement();
            return levelCode;
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
            result = con.performQuery("SELECT * FROM Period WHERE registrationNo='"+regNo+"'");
            while (result.next()) {
                periodList.add(result.getString("periodLabel"));
            }
            con.closeConnection();
            con.closeStatement();
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
            result = con.performQuery("SELECT * FROM Study WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            while (result.next()) {
                moduleList.add(result.getString("moduleCode"));
            }
            con.closeConnection();
            con.closeStatement();
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
            result = con.performQuery("SELECT * FROM Study WHERE registrationNo='"+regNo+"' AND moduleCode='"+moduleCode+"' AND periodLabel='"+periodLabel+"'");
            while (result.next()) {
                results[0] =  result.getString("initialGrade");
                results[1] =  result.getString("resitGrade");
            }
            con.closeConnection();
            con.closeStatement();
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
    public boolean updateGrades(String regNo, String moduleCode, String periodLabel, String initGrade, String resitGrade){
        if (super.getRole().equals("Teacher")){
            //Method assumes there is only one match
            try {
                //Init grades are checked to be numeric beforehand (and max characters 5)
                if (resitGrade != null){
                    resitGrade = "'" + resitGrade + "'";
                }
                if (initGrade != null){
                    initGrade = "'" + initGrade +  "'";
                }
                int changes = con.performUpdate("UPDATE Study SET initialGrade="+initGrade+", resitGrade="+resitGrade+" WHERE registrationNo='"+regNo+"' AND moduleCode='"+moduleCode+"' AND periodLabel='"+periodLabel+"'");
                if (changes>0) {
                    con.closeConnection();
                    con.closeStatement();
                    return true;
                }
                return false;
            }
            catch(SQLException ex) {
                //display error message and leave the application
                JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                        "ERROR", JOptionPane.ERROR_MESSAGE, null);
                System.exit(0);
            }
        }
        return false;
    }
    public String getUpperLevelCode(String degreeCode){
        //Method assumes there is only one match
        ResultSet result = null;
        try {
            result = con.performQuery("SELECT * FROM ModuleApproval WHERE degreeCode='"+degreeCode+"'");
            int maxLevel = 1;
            int n = 0;
            while (result.next()) {
                if (result.getString("levelCode").equals("P")){
                    continue;
                }
                n++;
                int num = Integer.parseInt(result.getString("levelCode"));
                if (num > maxLevel){
                    maxLevel = num;
                }
            }
            con.closeConnection();
            con.closeStatement();
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
            result = con.performQuery("SELECT * FROM Study WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
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
                    return new String[]{null, null};
                }
                if (i.get(1) == null){
                    //If a resit grade is null, its value can be set to 0
                    resitGrade = 0.0;
                }
                else{
                    resitGrade = Double.parseDouble(i.get(1));
                }
                Double initGrade = Double.parseDouble(i.get(0));
                Double passMark = 39.5;
                if (levelCode.equals("4")){
                    passMark = 49.5;
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
                else if (concededModules == 1 && !checkPass.equals("fail")){
                    //Conceded pass
                    checkPass = "conceded";
                }
            }
            meanGrade = initSum / grades.toArray().length;
            con.performUpdate("UPDATE Period SET meanGrade='"+round(meanGrade, 2)+"' WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            returnArr[0] = String.valueOf(round(meanGrade, 1));
            returnArr[1] = checkPass;
            con.closeConnection();
            con.closeStatement();
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
    public String getStudentHighestLevel(String regNo){
        //Method assumes there is only one match
        ResultSet result = null;
        String highestLevel = null;
        try {
            result = con.performQuery("SELECT MAX(levelCode) FROM Period WHERE registrationNo='"+regNo+"' AND (levelCode='1' OR levelCode='2' OR levelCode='3' OR levelCode='4')");
            while (result.next()) {
                highestLevel = result.getString(1);
            }
            con.closeConnection();
            con.closeStatement();
            return highestLevel;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return null;
    }
    public String[] getOverallGrade(String regNo, String upperLevel, boolean skipFour){
        //Method assumes there is only one match
        ResultSet result1 = null;
        ResultSet result2 = null;
        String[] returnArr = new String[2];
        List<List<String>> periods = new ArrayList<>();
        try {
            result1 = con.performQuery("SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='1'");
            if (!skipFour){
                result2 = con.performQuery("SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='2' UNION ALL " +
                        "SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='3' UNION ALL " +
                        "SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='4'");
            }
            else{
                result2 = con.performQuery("SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='2' UNION ALL " +
                        "SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='3' UNION ALL ");
            }
            String r1 = null;
            while (result1.next()){
                r1 = result1.getString(1);
            }
            int total = 0;
            if (r1 != null){
                //Year 1 is skipped for calculating degree classes
                total = Integer.parseInt(r1);
            }
            while (result2.next()) {
                List<String> combinedPeriodResit = new ArrayList<>();
                int current = Integer.parseInt(result2.getString(1));
                if (current != 0){
                    total += current;
                    //Converts number to letter (1 = A, 2 = B...)
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
            if (Integer.parseInt(getStudentHighestLevel(regNo)) != Integer.parseInt(upperLevel)){
                //Final year of course not reached
                return new String[2];
            }
            Double sumGrade = 0.0;
            Double divisor = 0.0;
            Double passMark = 39.5;
            returnArr[1] = "pass";
            for (List<String> i : periods){
                String level = getLevel(regNo, i.get(0));
                boolean resit = false;
                if (i.get(1).equals("1")){
                    resit = false;
                }
                else if (i.get(1).equals("2")){
                    resit = true;
                }
                String[] gradeArr = getMeanGrade(regNo, i.get(0), level);
                if (gradeArr[0] == null){
                    return new String[2];
                }
                if (gradeArr[1].equals("fail")){
                    returnArr[1] = "fail";
                }
                if (level.equals("2")){
                    //Year 2
                    if (!resit){
                        sumGrade += Double.parseDouble(gradeArr[0]);
                    }
                    else{
                        sumGrade += Math.min(Double.parseDouble(gradeArr[0]), passMark);
                    }
                    divisor++;
                }
                else{
                    //Year 3/4
                    if (level.equals("4")){
                        passMark = 49.5;
                    }
                    if (!resit){
                        sumGrade += 2 * Double.parseDouble(gradeArr[0]);
                    }
                    else{
                        sumGrade += 2 * Math.min(Double.parseDouble(gradeArr[0]), passMark);
                    }
                    divisor+=2;
                }
            }
            returnArr[0] = String.valueOf(round(sumGrade/divisor,2));
            con.closeConnection();
            con.closeStatement();
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
    public boolean checkRetake(String regNo, String levelCode, String periodLabel){
        //Method assumes there is only one match
        ResultSet result = null;
        boolean retake = false;
        try {
            result = con.performQuery("SELECT MAX(periodLabel) FROM Period WHERE registrationNo='"+regNo+"' AND levelCode='"+levelCode+"'");
            while (result.next()) {
                if (result.getString(1).equals(periodLabel)){
                    retake = true;
                }
            }
            con.closeConnection();
            con.closeStatement();
            return retake;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return false;
    }
    public List<String> getCoreModules(String degreeCode, String levelCode){
        ResultSet result = null;
        List<String> returnString = new ArrayList<>();
        try {
            result = con.performQuery("SELECT * FROM ModuleApproval WHERE degreeCode='"+degreeCode+"' AND levelCode='"+levelCode+"' AND core='1'");
            while (result.next()) {
                returnString.add(result.getString("moduleCode"));
            }
            return returnString;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return returnString;
    }
    public List<List<String>> getPreviousModulesGrades(String regNo, String periodLabel){
        ResultSet result = null;
        List<List<String>> returnString = new ArrayList<>();
        List<String> resultString = new ArrayList<>();
        try {
            result = con.performQuery("SELECT * FROM Study WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            System.out.println("SELECT * FROM Study WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            while (result.next()) {
                resultString = new ArrayList<>();
                resultString.add(result.getString("moduleCode"));
                resultString.add(result.getString("initialGrade"));
                resultString.add(result.getString("resitGrade"));
                returnString.add(resultString);
            }
            return returnString;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return returnString;
    }
    public boolean progressStudent(String regNo, String levelCode, String outcome, String periodLabel, String degreeCode){
        if (outcome == null){
            return false;
        }
        Integer updates = null;
        ResultSet result1 = null;
        ResultSet result2 = null;
        String startDate = null;
        String endDate = null;
        String newSDate = null;
        String newEDate = null;
        String startYear = null;
        String endYear = null;
        Character currentPeriod = periodLabel.charAt(0);
        Character nextPeriod = currentPeriod;
        nextPeriod++;
        try {
            result1 = con.performQuery("SELECT COUNT(*) FROM Period WHERE registrationNo='"+regNo+"' AND periodLabel='"+nextPeriod+"'");
            while (result1.next()){
                if (result1.getInt(1) > 0){
                    //If the next period already exists, a new one shouldn't be registered
                    return false;
                }
            }
            result2 = con.performQuery("SELECT * FROM Period WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
            while (result2.next()) {
                startDate = result2.getString("startDate");
                endDate = result2.getString("endDate");
                startYear = String.valueOf(Integer.parseInt(startDate.substring(0,4))+1);
                newSDate = startYear + startDate.substring(4);
                endYear = String.valueOf(Integer.parseInt(endDate.substring(0,4))+1);
                newEDate = endYear + startDate.substring(4);
            }
            if (outcome.equals("pass")){
                String newLevel = String.valueOf(Integer.parseInt(levelCode)+1);
                updates = con.performUpdate("INSERT INTO Period (periodLabel,registrationNo,levelCode,startDate,endDate)"
                        + " VALUES ('"+nextPeriod+"','"+regNo+"','"+newLevel+"','"+newSDate+"','"+newEDate+"')");
                //Add next level's core modules, no grades.
                List<String> coreModules = getCoreModules(degreeCode, newLevel);
                if (updates > 0){
                    for (String i : coreModules){
                        con.performUpdate("INSERT INTO Study (registrationNo,initialGrade,resitGrade,moduleCode,periodLabel)"
                                + " VALUES ('"+regNo+"',"+null+","+null+",'"+i+"','"+nextPeriod+"')");
                    }
                }
                return false;
            }
            else if (outcome.equals("repeat")){
                updates = con.performUpdate("INSERT INTO Period (periodLabel,registrationNo,levelCode,startDate,endDate)"
                        + " VALUES ('"+nextPeriod+"','"+regNo+"','"+levelCode+"','"+newSDate+"','"+newEDate+"')");
                //Add previously passed modules and their grades. Re-add failed modules with no grades.
                List<List<String>> previousModulesGrades = getPreviousModulesGrades(regNo, periodLabel);
                if (updates > 0){
                    for (List<String> i : previousModulesGrades){
                        String initGrade = i.get(1);
                        String resitGrade = i.get(2);
                        Double passMark = 39.5;
                        if (levelCode.equals("4")){
                            passMark = 49.5;
                        }
                        if (Double.parseDouble(initGrade) >= passMark){
                            //Passed module
                            if (resitGrade != null){
                                resitGrade = "'" + resitGrade + "'";
                            }
                            con.performUpdate("INSERT INTO Study (registrationNo,initialGrade,resitGrade,moduleCode,periodLabel)"
                                    + " VALUES ('"+regNo+"','"+initGrade+"',"+resitGrade+",'"+i.get(0)+"','"+nextPeriod+"')");
                        }
                        else if(resitGrade != null){
                            if (Double.parseDouble(resitGrade) >= passMark){
                                con.performUpdate("INSERT INTO Study (registrationNo,initialGrade,resitGrade,moduleCode,periodLabel)"
                                        + " VALUES ('"+regNo+"','"+initGrade+"','"+resitGrade+"','"+i.get(0)+"','"+nextPeriod+"')");
                            }
                            else{
                                con.performUpdate("INSERT INTO Study (registrationNo,initialGrade,resitGrade,moduleCode,periodLabel)"
                                        + " VALUES ('"+regNo+"',"+null+","+null+",'"+i.get(0)+"','"+nextPeriod+"')");
                            }
                        }
                        else{
                            con.performUpdate("INSERT INTO Study (registrationNo,initialGrade,resitGrade,moduleCode,periodLabel)"
                                    + " VALUES ('"+regNo+"',"+null+","+null+",'"+i.get(0)+"','"+nextPeriod+"')");
                        }
                    }
                }
            }
            else{
                updates = 0;
            }
            con.closeConnection();
            con.closeStatement();
            if (updates > 1){
                return true;
            }
            return false;
        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        return false;
    }
    public String[] getOutcome(String regNo, String periodLabel,String levelCode,String degreeCode,String grade, String checkPass, boolean degreeClass) {
        //check if it is 1-year MSc
        String[] periodOutcome = new String[2];
        boolean retake = checkRetake(regNo, levelCode, periodLabel);
        Double meanGrade;
        if (grade == null){
            return periodOutcome;
        }
        else{
            meanGrade = Double.valueOf(grade);
        }
        if (checkPass.equals("fail")){
            periodOutcome[0] = "fail";
        }
        else if (checkPass.equals("conceded")){
            periodOutcome[0] = "conceded pass";
        }
        else if (degreeCode.substring(3,4).equals("P")) {
            //check if student failed dissertation but succeeded in all the other modules
            double disertationGrade=0;
            int studentTotalCredits=0;
            for (Map.Entry<String, Double> entry : con.recModules.entrySet()) {
                int credit=con.moduleCredits.get(entry.getKey());
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
                periodOutcome[0] = "PGDip";
            }
            //Special rule if student passes 60 credits of modules
            if (studentTotalCredits==60) {
                periodOutcome[0] = "PGCert";
            }

            if (meanGrade>=69.5) {
                periodOutcome[0] = "distinction";
            }
            else if (meanGrade>=59.5) {
                periodOutcome[0] = "merit";
            }
            else if (meanGrade>=49.5) {
                periodOutcome[0] = "pass";
            }
            else {
                periodOutcome[0] = "fail";
            }
        }
        //check if BSc,BEng degree
        else if (levelCode.equals("3") || levelCode.equals("2") || levelCode.equals("1")) {
            if (meanGrade>=69.5) {
                periodOutcome[0] = "first class";
            }
            else if (meanGrade>=59.5) {
                periodOutcome[0] = "upper second";
            }
            else if (meanGrade>=49.5) {
                periodOutcome[0] = "lower second";
            }
            else if (meanGrade>=44.5){
                periodOutcome[0] = "third class";
            }
            else if (meanGrade>=39.5){
                periodOutcome[0] = "pass (non-honours)";
            }
            else{
                periodOutcome[0] = "fail";
            }
        }
        //else MComp/MEng degree
        else {
            if (meanGrade>=69.5) {
                periodOutcome[0] = "first class";
            }
            else if (meanGrade>=59.5) {
                periodOutcome[0] = "upper second";
            }
            else if (meanGrade>=49.5) {
                periodOutcome[0] = "lower second";
            }
            else {
                periodOutcome[0] = "fail";
            }
        }
        try{
            if (!degreeClass){
                //For period outcomes,
                if (!periodOutcome[0].equals("fail")){
                    periodOutcome[1] = "pass";
                    if (!periodOutcome[0].equals("conceded pass")){
                        periodOutcome[0] = "pass";
                    }
                }
                else{
                    if (retake){
                        periodOutcome[1] = "fail";
                    }
                    else{
                        periodOutcome[1] = "repeat";
                    }
                }
                if (super.getRole().equals("Teacher")){
                    con.performUpdate("UPDATE Period SET outcome='"+periodOutcome[0]+"' WHERE registrationNo='"+regNo+"' AND periodLabel='"+periodLabel+"'");
                }
            }
            else{
                //For degree classes (meanGrade would be adjusted to the correct value for all levels/weightings)
                if (!periodOutcome[0].equals("fail")){
                    periodOutcome[1] = "graduate";
                }
                else{
                    if (periodOutcome[0].equals("PGDip") || periodOutcome[0].equals("PGDip")){
                        periodOutcome[1] = periodOutcome[0];
                    }
                    else{
                        if (levelCode.equals("4")){
                            if (degreeCode.substring(3,4).equals("P")){
                                periodOutcome[1] = "fail";
                            }
                            else{
                                String bachelorsOutcome = getOutcome(regNo, periodLabel,levelCode,degreeCode,getOverallGrade(regNo, "3", true)[0],checkPass,true)[1];
                                periodOutcome[1] = "BSc " + bachelorsOutcome;
                            }
                        }
                        else{
                            if (retake){
                                periodOutcome[1] = "fail";
                            }
                            else{
                                periodOutcome[1] = "repeat";
                            }
                        }
                    }
                }
                if (super.getRole().equals("Teacher")) {
                    con.performUpdate("UPDATE Student SET degreeClass='" + periodOutcome[0] + "' WHERE registrationNo='" + regNo + "'");
                }
            }
            con.closeConnection();
            con.closeStatement();
            return periodOutcome;
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

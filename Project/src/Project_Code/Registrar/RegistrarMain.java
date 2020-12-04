package Project_Code.Registrar;

import Project_Code.DBController;
import Project_Code.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarMain extends User {
    private DBController con = super.getDataAccessController();
    private int currentCredits = 0;

    /**
     * Sets username as registrar for the Registrars
     *
     * @param username username of registrar
     */
    public RegistrarMain(String username) {

        super(username, "registrar");
    }

    public boolean addNewStudent(String title, String surname, String forename,
                                 String level, String degreeCode, String tutor, String startDate, String endDate, String password) throws SQLException {

        // create student username
        int counter=0;
        do {
            counter++;
            username= (forename.charAt(0)+surname+ counter).toLowerCase();
        } while (checkUsernameExists(username));

        // create an email for the student
        String email=username+"@sheffield.ac.uk";

        //period label
        String periodLabel="A";

        if (!(checkDates(startDate,endDate))){
            return false;
        }

        //convert the dates given in mysql format DATE (yyyymmdd)
        startDate=startDate.substring(6, 10)+startDate.substring(3,5)+startDate.substring(0,2);
        endDate=endDate.substring(6, 10)+endDate.substring(3,5)+endDate.substring(0,2);

        int changes;
        //insert to UserAccounts
        changes = con.performUpdate("INSERT INTO UserAccounts (username, password)"
                + " VALUES ('"+username+"','"+password+"')");

        //insert to Users Table
        changes+=con.performUpdate("INSERT INTO Users (username,role,forename,surname, title, email)"
                + " VALUES ('"+username+"','student','"+forename+"','"+surname+"','"+title+"','"+email+"')");

        //insert to Students Table
        changes+=con.performUpdate("INSERT INTO Student (username,degreeCode,tutor)"
                + " VALUES ('"+username+"','"+degreeCode+"','"+tutor+"')");

        int regNum = getRegNum(username);

        //insert student into period table
        changes+=con.performUpdate("INSERT INTO Period (periodLabel,registrationNo,levelCode,startDate,endDate)"
                + " VALUES ('"+periodLabel+"','"+regNum+"','"+level+"','"+startDate+"','"+endDate+"')");


        if (changes>0)
            JOptionPane.showMessageDialog(null,"New student has been registered with registration number:"+ regNum + " and email:"+ email);
        con.closeConnection();
        con.closeStatement();

        //add all the Required modules to the StudyGrade table
        addRequiredModules(regNum,degreeCode, level, periodLabel);

        return  changes>0;
    }


    public int getRegNum(String username) throws SQLException {
        ResultSet result;
        int res = 0;
        result = con.performQuery("SELECT registrationNo FROM Student WHERE username ='"+username+"'");
        while (result.next()) {
             res=Integer.parseInt(result.getString(1));
        }

        con.closeConnection();
        con.closeStatement();
        return (res);
    }


    public boolean checkUsernameExists(String user) throws SQLException {
        ResultSet result;
        int count=0;
        result = con.performQuery("SELECT EXISTS(SELECT 1 FROM UserAccounts WHERE username = '" +
                user + "' COLLATE latin1_bin)"); //collate is used for case sensitivity

        while (result.next()) {
            count=Integer.parseInt(result.getString(1));
        }
        con.closeConnection();
        con.closeStatement();
        return (count!=0);

    }


    public void addRequiredModules(int regNum,String degreeCode, String levelCode, String periodLabel) throws NumberFormatException, SQLException{
        ResultSet result;
        if (levelCode.equals("P")) {
            return; //doesn't add for a placement student so it escapes the method
        }
        //create an arraylist of all the required modules
        ArrayList<String> oblModules = new ArrayList<>();
        result = con.performQuery("SELECT moduleCode FROM ModuleApproval WHERE degreeCode='"+degreeCode+"' "
                + "AND levelCode='"+levelCode+"' AND core=1 ");
        while (result.next()) {
            oblModules.add(result.getString(1)); //add each required module to the arraylist
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
        //insert each required module to the database

        String query = ("INSERT INTO Study (registrationNo,moduleCode,periodLabel)");
        // iterate over required modules in memory and
        // insert these into the database
        ListIterator<String> modules = oblModules.listIterator();
        while(modules.hasNext()) {
            con.performUpdate(query+"VALUES ('"+regNum+"','"+modules.next()+"','"+periodLabel+"')");
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
    }

    /**
     * Removes student from the database
     *
     */
    public boolean removeStudent(String regNum) throws NumberFormatException, SQLException{
        ResultSet result;
        //checks whether the registration number is blank
        if (regNum.equals("")) {
            JOptionPane.showMessageDialog(null,"Select the registration number of the student you wish to remove.");
            return false;
        }

        //gets the username using the registration number
        String username="";
        System.out.println(regNum);

        int changes;
        result=con.performQuery("SELECT * FROM Student WHERE registrationNo= '"+Integer.valueOf(regNum)+"' ");
        while (result.next()) {
            username=result.getString(3);
        }
        //delete from database

        changes=con.performUpdate("DELETE FROM Study WHERE registrationNo= '"+Integer.valueOf(regNum)+"' ");
        con.closeConnection();
        con.closeStatement();

        //delete from Period
        changes+=con.performUpdate("DELETE FROM Period WHERE registrationNo= '"+Integer.valueOf(regNum)+"' ");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        //delete from Students
        changes+=con.performUpdate("DELETE FROM Student WHERE registrationNo= '"+Integer.valueOf(regNum)+"' ");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        //delete from StudyGrades
        changes+=con.performUpdate("DELETE FROM Study WHERE registrationNo= '"+Integer.valueOf(regNum)+"' ");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        changes+=con.performUpdate("DELETE FROM Users WHERE username= '"+username+"' ");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        changes+=con.performUpdate("DELETE FROM UserSalts WHERE username= '"+username+"' ");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        changes+=con.performUpdate("DELETE FROM UserAccounts WHERE username= '"+username+"' ");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
        return changes>0;
    }

    public DefaultTableModel findStudentRecord(String regNum, String periodLabel, String levelCode, String degreeCode) throws SQLException {
        ResultSet result;
        //create columns of the table
        DefaultTableModel table=new DefaultTableModel(new Object[][] {},new String[] {
                "Module Code", "Core", "Credits"});

        Object [] recordModules=new Object[3];

        //store the student's current credits
        currentCredits=0;
        //find the student's record
        result=con.performQuery("SELECT DISTINCT t1.moduleCode,t3.core,t2.credits FROM Study t1 JOIN Module t2 ON t1.moduleCode=t2.moduleCode"
                + " JOIN ModuleApproval t3 ON t2.moduleCode=t3.moduleCode WHERE t1.registrationNo='"+Integer.parseInt(regNum)+"' AND t1.periodLabel='"+periodLabel+"' "
                + "AND t3.levelCode='"+levelCode+"' AND t3.degreeCode ='"+degreeCode+"' ");
        while (result.next()) {
            recordModules[0] = result.getString(1);//modulecode
            recordModules[1] = result.getString(2);//core modules
            recordModules[2] = result.getString(3);//credits
            currentCredits+=Integer.parseInt(result.getString(3));
            table.addRow(recordModules);
        }

        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        return table;
    }

    public DefaultTableModel viewAllStudents () throws SQLException{
        ResultSet result;
        //initialise the columns of the table
        DefaultTableModel table=new DefaultTableModel(new Object[][] {},new String[] {
                "Registration Number", "Degree Code", "username", "E-mail", "Title", "Surname", "Forename", "Tutor"});

        //get the data from the database and add it to the table model
        Object [] regStudents = new Object[8];
        result = con.performQuery("SELECT Student.registrationNo, Student.degreeCode, Users.username, Users.email, Users.title, Users.surname, Users.forename, Student.tutor FROM Student INNER JOIN Users ON Student.username=Users.username;");
        while (result.next()) {
            regStudents[0] = result.getString(1);//registration number
            regStudents[1] = result.getString(2);//degreeCode
            regStudents[2] = result.getString(3);//username
            regStudents[3] = result.getString(4);//email
            regStudents[4] = result.getString(5);//title
            regStudents[5] = result.getString(6);//surname
            regStudents[6] = result.getString(7);//forename
            regStudents[7] = result.getString(8);//tutor
            table.addRow(regStudents); //add each row to the table model
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        return table;

    }

    public DefaultTableModel viewCurrentPeriodStatus () throws NumberFormatException, SQLException{
        ResultSet result;

        //create the table model columns
        DefaultTableModel defaultTable=new DefaultTableModel(new Object[][] {},new String[] {
                "Registration No.", "DegreeCode", "Modules","Period Label","Current Total Credits", "Required Credits"});

        Object [] currentStatus = new Object[6];

        //find the modules from the database
        result=con.performQuery("SELECT t1.registrationNo, t1.degreeCode, group_concat(t2.moduleCode), t2.periodLabel, SUM(t3.credits) FROM " +
                "Student t1 JOIN Study t2 ON t1.registrationNo = t2.registrationNo JOIN Module t3 ON t2.moduleCode = t3.moduleCode " +
                "WHERE t2.periodLabel= (SELECT MAX(periodLabel) FROM Study WHERE registrationNo = t1.registrationNo) GROUP BY registrationNo");
        while (result.next()) {
            currentStatus[0] = result.getString(1);//Registration No
            currentStatus[1] = result.getString(2);//degreecode
            currentStatus[2] = result.getString(3);//modulecode
            currentStatus[3] = result.getString(4);//periodlabel
            currentStatus[4] = result.getString(5);//credits
            if (result.getString(2).charAt(3) == 'P')
                currentStatus[5] = 180;
            else
                currentStatus[5] = 120;
            defaultTable.addRow(currentStatus);
        }
        con.closeConnection();
        con.closeStatement();


        return defaultTable;

    }

    public boolean addOptionalModule(String regNum,String moduleCode, String periodLabel, int totalCredits) throws SQLException {
        ResultSet result;
        //check if there are blanks
        if (regNum.equals("") || moduleCode.equals("")) {
            JOptionPane.showMessageDialog(null,"Please select a module to add");
            return false;
        }

        int credits=0;
        result=con.performQuery("SELECT credits FROM Module WHERE moduleCode= '"+moduleCode+"' ");
        while (result.next()) {
            credits=result.getInt(1);
        }

        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        //check whether the current credits are not more than the total credits
        if ((getCurrentCredits()+credits)>totalCredits) {
            JOptionPane.showMessageDialog(null,"Current credits are more than the total credits. Please choose a different module to add");
            return false;
        }

        //insert into the database the optional module
        int changes;
        changes=con.performUpdate("INSERT INTO Study (registrationNo,moduleCode,periodLabel)"
                + " VALUES ('"+regNum+"','"+moduleCode+"','"+periodLabel+"' )");
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
        return changes>0;
    }
    public boolean dropOptionalModule(String regNum,String moduleCode,String periodLabel) throws SQLException {
        //check if there are blanks
        if (regNum.equals("") || moduleCode.equals("")) {
            JOptionPane.showMessageDialog(null,"Please select a module to drop");
            return false;
        }

        //delete from the database the optional module
        int changes;
        changes=con.performUpdate("DELETE FROM Study WHERE registrationNo= '"+Integer.valueOf(regNum)+"' "
                + "AND moduleCode= '"+moduleCode+"' AND periodLabel='"+periodLabel+"' ");

        con.closeConnection();
        con.closeStatement();
        return changes>0;
    }

    public DefaultTableModel viewModules(String degreeCode, String levelCode, int core) throws NumberFormatException, SQLException{
        ResultSet result;

        //if core is 0 then the module is optional
        String moduleType;
        if (core==0) {
            moduleType="Optional Module";
        }
        //if core is 1 then the module is required
        else {
            moduleType="Required Module";
        }

        //create the table model columns
        DefaultTableModel defaultTable=new DefaultTableModel(new Object[][] {},new String[] {
                moduleType, "Credits"});

        Object [] modules = new Object[2];

        //find the modules from the database
        result=con.performQuery("SELECT t1.moduleCode,t2.credits FROM ModuleApproval t1 INNER JOIN Module t2 ON t1.moduleCode = t2.moduleCode"
                + " WHERE t1.degreeCode='"+degreeCode+"' AND t1.levelCode='"+levelCode+"' AND t1.core='"+core+"' ");
        while (result.next()) {
            modules[0] = result.getString(1);//moduleCode
            modules[1] = result.getString(2);//credits
            defaultTable.addRow(modules);
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();


        return defaultTable;

    }

    public int getCurrentCredits() {
        return currentCredits;
    }

    public int numberOfFailures(String regNum) throws SQLException {
        ResultSet result;
        int failures = 0;
        result = con.performQuery("SELECT COUNT(*) FROM Period WHERE registrationNO = '"+regNum+"' AND outcome = '"+"fail"+"' ");
        while (result.next()){
            failures = result.getInt(1);
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
        return failures;
    }

    public boolean checkLevelExists(String degreeCode,String levelCode) throws SQLException {
        ResultSet result;
        int count=0;
        result = con.performQuery("SELECT EXISTS(SELECT 1 FROM ModuleApproval WHERE degreeCode = '" +
                degreeCode + "' AND levelCode = '"+levelCode+"' COLLATE latin1_bin)");
        while (result.next()) {
            count=Integer.parseInt(result.getString(1));
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
        return (count!=0);
    }


    public int findDegreePeriod(String degreeCode) throws SQLException {
        if (checkLevelExists(degreeCode,"4")) {
            return 4;
        }
        else if(checkLevelExists(degreeCode,"3")) {
            return 3;
        }
        else if(checkLevelExists(degreeCode,"2")) {
            return 2;
        }
        else{
            return 1;
        }
    }
    public boolean checkDates(String start, String end) {
        //check for correct format using regular expression
        String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher startMatch=pattern.matcher(start);
        Matcher endMatch=pattern.matcher(end);

        if ( !startMatch.find() || !endMatch.find() ) {
            JOptionPane.showMessageDialog(null,"Please check the format of your dates");
            return false;
        }

        //check whether dates make sense
        int startY=Integer.parseInt(start.replaceAll("/", "").substring(4, 8));//start Year
        int startM=Integer.parseInt(start.replaceAll("/", "").substring(2, 4));//start Month
        int startD=Integer.parseInt(start.replaceAll("/", "").substring(0, 2));//start Day

        int endY=Integer.parseInt(end.replaceAll("/", "").substring(4, 8));//end Year
        int endM=Integer.parseInt(end.replaceAll("/", "").substring(2, 4));//end Month
        int endD=Integer.parseInt(end.replaceAll("/", "").substring(0, 2));//end Day

        //check the year difference only
        if (!(endY-startY==1)) {
            JOptionPane.showMessageDialog(null, "The registered period of study should have a year difference of 1");
            return false;
        }

        if (startY<2020) {
            JOptionPane.showMessageDialog(null,"The start date should be after the current date");
            return false;
        }

        //check the that the starting date is indeed earlier than the end date
        if (startY > endY ){
            JOptionPane.showMessageDialog(null,"The end date is earlier than the start date");
            return false;
        }
        //check the period difference including the months and days
        else if (12 - startM + endM < 6 || 12 - startM + endM == 6 && endD < startD){
            JOptionPane.showMessageDialog(null,"The dates should have at least six months difference");
            return false;
        }
        return true;
    }

    public static String generateRandomPassword() {
        // ASCII range - alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder pword = new StringBuilder();


        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(chars.length());
            pword.append(chars.charAt(randomIndex));
        }
        return pword.toString();
    }


}
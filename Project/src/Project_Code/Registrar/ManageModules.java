package Project_Code.Registrar;
import Project_Code.DBController;


import java.awt.HeadlessException;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;


public class ManageModules extends JPanel implements ActionListener {
    private JFrame frame;
    private RegistrarMain registrar;
    private DBController con = new DBController("team037", "ee143bc0");


    private JComboBox<String> comboBox, comboBox_1, comboBox_2;
    private JButton btnGoBack, button_6, button;

    private String degreeCode, levelCode, regNum, periodLabel;
    private int totalCredits;

    private JLabel label_1;
    private JLabel label_2;
    private JLabel label_4;
    private JLabel label_5;
    private JLabel label_7;
    private JLabel label_3;
    private JLabel label_6;
    private JLabel label_8;
    private JLabel label_9;
    private final JLabel lblModulesTakenFrom;
    private JLabel label_10;
    private JLabel label_11;
    private JLabel label_12;
    private JLabel lblNewLabel;
    private JLabel lblRequiredModules;
    private JLabel lblPeriodLabel;
    private JLabel label_13;

    private JScrollPane reqScrollP, optScrollP, recScrollP;
    private final JTable reqTable;
    private JTable optTable;
    private JTable recordTable;
    private String degreeClass;

    public ManageModules(JFrame f, RegistrarMain reg) {
        //bind the registrar object
        registrar = reg;

        setLayout(null);

        label_3 = new JLabel("Select Optional Module to Add:");
        label_3.setBounds(12, 452, 168, 25);
        add(label_3);
        label_3.setVisible(false);

        label_9 = new JLabel("Select Optional Module to Drop:");
        label_9.setBounds(411, 452, 168, 25);
        add(label_9);
        label_9.setVisible(false);

        //Add optional module button
        button_6 = new JButton("Add Optional Module");
        button_6.setBounds(79, 504, 158, 41);
        add(button_6);
        button_6.setVisible(false);
        button_6.addActionListener(this);

        //drop optional module button
        button = new JButton("Drop Optional Module");
        button.setBounds(497, 504, 180, 41);
        add(button);
        button.setVisible(false);
        button.addActionListener(this);

        //Select registration number label
        JLabel label = new JLabel("Select Registration Number:");
        label.setBounds(12, 20, 183, 25);
        add(label);

        //The registration number combo box
        //adds the numbers available from the database
        comboBox = new JComboBox<>();
        comboBox.setBounds(207, 22, 179, 22);
        comboBox.addItem("");
        ResultSet result;
        try {
            result = con.performQuery("SELECT registrationNo FROM Student");
            while (result.next())
                comboBox.addItem(result.getString(1));
            //close connection and statement
            con.closeConnection();
            con.closeStatement();

        } catch (SQLException e) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        //show options on registrar panel
        comboBox.addActionListener(event -> {
            regNum = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
            if (!regNum.equals("")) { //if the registration number is not blank
                try {
                    findStudentDetails(regNum);
                } catch (SQLException e1) {
                    //display error message and leave the application
                    JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    System.exit(0);
                }
                if (!(degreeClass == null || degreeClass.equals(""))) {
                    JOptionPane.showMessageDialog(null, "Student already graduated");
                    viewComponents(false);
                }//check if placement year
                else if (levelCode.equals("P")) {
                    JOptionPane.showMessageDialog(null, "Student is currently on Placement");
                    viewComponents(false);
                }//if student has failed more than once
                else {
                    try {
                        if (registrar.numberOfFailures(regNum) > 1) {
                            JOptionPane.showMessageDialog(null, "Student has failed and is not progressed to any level");
                            viewComponents(false);
                        } else {
                            comboBox_1.removeAllItems();
                            comboBox_1.addItem("");
                            comboBox_2.removeAllItems();
                            comboBox_2.addItem("");
                            setupComponents();
                            viewComponents(true);
                        }
                    } catch (HeadlessException | SQLException e) {
                        //display error message and leave the application
                        JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                                "ERROR", JOptionPane.ERROR_MESSAGE, null);
                        System.exit(0);
                    }

                }
            } else {
                viewComponents(false); //else hide the components
            }
        });
        add(comboBox);

        //combo box of optional modules to be added
        comboBox_1 = new JComboBox<>();
        comboBox_1.setBounds(230, 453, 116, 22);
        add(comboBox_1);
        comboBox_1.setVisible(false);
        comboBox_1.addItem("");

        //go back button
        btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 155, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        //degree Code label
        label_1 = new JLabel("Degree Code:");
        label_1.setBounds(12, 58, 88, 25);
        add(label_1);
        label_1.setVisible(false);

        //level code label
        label_2 = new JLabel("Level Code:");
        label_2.setBounds(12, 79, 88, 25);
        add(label_2);
        label_2.setVisible(false);

        //total credits label
        label_6 = new JLabel("Total Credits for degree:");
        label_6.setBounds(12, 134, 168, 25);
        add(label_6);
        label_6.setVisible(false);

        //where the degree code is displayed after selecting a reg number
        label_4 = new JLabel("");
        label_4.setBounds(100, 58, 88, 25);
        add(label_4);
        label_4.setVisible(false);

        //where the level code is displayed after selecting a reg number
        label_5 = new JLabel("");
        label_5.setBounds(92, 79, 54, 25);
        add(label_5);
        label_5.setVisible(false);

        //where the total credits  is displayed after selecting a reg number
        label_7 = new JLabel("");
        label_7.setBounds(169, 134, 168, 25);
        add(label_7);
        label_7.setVisible(false);

        lblPeriodLabel = new JLabel("Period Label:");
        lblPeriodLabel.setBounds(12, 105, 88, 25);
        add(lblPeriodLabel);
        lblPeriodLabel.setVisible(false);

        //displays the latest period label after the registration number is selected
        label_13 = new JLabel("");
        label_13.setBounds(95, 105, 44, 25);
        add(label_13);
        label_13.setVisible(false);

        //scroll panel for the table for all the available obligatory modules
        reqScrollP = new JScrollPane();
        reqScrollP.setBounds(411, 42, 424, 177);
        add(reqScrollP);
        reqScrollP.setVisible(false);

        //the table for all the available obligatory modules
        reqTable = new JTable();
        reqScrollP.setViewportView(reqTable);
        reqTable.setEnabled(false);
        reqTable.setVisible(false);

        //scroll panel for the table for all the available optional modules
        optScrollP = new JScrollPane();
        optScrollP.setBounds(411, 255, 424, 177);
        add(optScrollP);
        optScrollP.setVisible(false);

        //the table for all the available optional modules
        optTable = new JTable();
        optScrollP.setViewportView(optTable);
        optTable.setEnabled(false);
        optTable.setVisible(false);

        //scroll panel for the table for the modules of the student
        recScrollP = new JScrollPane();
        recScrollP.setBounds(12, 214, 347, 186);
        add(recScrollP);
        recScrollP.setVisible(false);

        //table for the modules of the student
        recordTable = new JTable();
        recScrollP.setViewportView(recordTable);
        recordTable.setEnabled(false);
        recordTable.setVisible(false);

        //required modules label
        lblRequiredModules = new JLabel("Required Modules:");
        lblRequiredModules.setBounds(411, 13, 158, 16);
        add(lblRequiredModules);
        lblRequiredModules.setVisible(false);

        //optional modules label
        label_8 = new JLabel("Optional Modules:");
        label_8.setBounds(411, 226, 125, 16);
        add(label_8);
        label_8.setVisible(false);

        //drop optional module combo box
        comboBox_2 = new JComboBox<>();
        comboBox_2.setBounds(600, 453, 116, 22);
        add(comboBox_2);
        comboBox_2.setVisible(false);
        comboBox_2.addItem("");

        lblModulesTakenFrom = new JLabel("Modules taken from this student:");
        lblModulesTakenFrom.setBounds(12, 185, 235, 16);
        add(lblModulesTakenFrom);
        lblModulesTakenFrom.setVisible(false);

        lblNewLabel = new JLabel("Current Credits:");
        lblNewLabel.setBounds(12, 413, 120, 25);
        add(lblNewLabel);
        lblNewLabel.setVisible(false);

        label_10 = new JLabel("Remaining Credits:");
        label_10.setBounds(158, 413, 143, 25);
        add(label_10);
        label_10.setVisible(false);

        //current credits displayed after a reg number is selected
        label_11 = new JLabel("");
        label_11.setBounds(120, 413, 32, 25);
        add(label_11);
        label_11.setVisible(false);

        //credits remaining displayed after a reg number is selected
        label_12 = new JLabel("");
        label_12.setBounds(275, 413, 32, 25);
        add(label_12);
        label_12.setVisible(false);

        //frame configurations
        frame = f;
        frame.setBounds(100, 100, 450, 280);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back": //goes back to the registrar menu
                setVisible(false);
                frame.setContentPane(new RegistrarFrame(frame, registrar.getUsername()));
                break;
            case "Add Optional Module":
                String addModuleCode = Objects.requireNonNull(comboBox_1.getSelectedItem()).toString();
                try { //add the optional module
                    if (registrar.addOptionalModule(regNum, addModuleCode, periodLabel, totalCredits)) {
                        JOptionPane.showMessageDialog(null, "Optional Module Added");
                        setupComponents();//re-set up the components
                    }
                    //close connection and statement
                    con.closeConnection();
                    con.closeStatement();
                } catch (SQLException e1) {
                    //display error message and leave the application
                    JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    System.exit(0);
                }

                break;
            case "Drop Optional Module":
                String dropModuleCode = Objects.requireNonNull(comboBox_2.getSelectedItem()).toString();
                try {//drop the optional module
                    if (registrar.dropOptionalModule(regNum, dropModuleCode, periodLabel)) {
                        JOptionPane.showMessageDialog(null, "Optional Module Dropped");
                        setupComponents();//re-set up the components
                    }
                    //close connection and statement
                    con.closeConnection();
                    con.closeStatement();
                } catch (SQLException e1) {
                    //display error message and leave the application
                    JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    System.exit(0);
                }
                break;
        }
    }


    /**
     * Finds the students details using the registration number selected from the database
     *
     */
    public void findStudentDetails(String regNum) throws SQLException {
        ResultSet result;
        try {
            result = con.performQuery("SELECT * FROM Student WHERE registrationNo='" + Integer.parseInt(regNum) + "'");
            while (result.next()) {
                degreeCode = result.getString(2);
                degreeClass = result.getString(5);
            }
            ResultSet result2;
            result2 = con.performQuery("SELECT periodLabel,levelCode FROM Period WHERE registrationNo='" + regNum + "' ORDER BY periodLabel DESC LIMIT 1");
            while (result2.next()) {
                periodLabel = result2.getString(1);//gets the latest period label
                levelCode = result2.getString(2);//gets the latest level code
            }


            if (degreeCode.charAt(3) == 'U') {
                totalCredits = 120;
            } else {
                totalCredits = 180;
            }

        } catch (SQLException e1){
            JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

    }


    /**
     * shows the components after a registration number is selected
     * hides them if the selection is blank
     *
     * @param view true if the components are to be displayed false if otherwise
     */
    public void viewComponents(boolean view) {
        label_4.setVisible(view);
        label_5.setVisible(view);
        label_12.setVisible(view);
        button_6.setVisible(view);
        button.setVisible(view);
        label_3.setVisible(view);
        label_9.setVisible(view);
        comboBox_2.setVisible(view);
        comboBox_1.setVisible(view);
        reqScrollP.setVisible(view);
        optScrollP.setVisible(view);
        recScrollP.setVisible(view);
        reqTable.setVisible(view);
        optTable.setVisible(view);
        recordTable.setVisible(view);
        lblRequiredModules.setVisible(view);
        label_8.setVisible(view);
        frame.setBounds(100, 100, 990, 650);
        frame.setLocationRelativeTo(null);
        label_1.setVisible(view);
        label_2.setVisible(view);
        label_6.setVisible(view);
        label_10.setVisible(view);
        label_11.setVisible(view);
        label_12.setVisible(view);
        label_13.setVisible(view);
        lblNewLabel.setVisible(view);
        lblPeriodLabel.setVisible(view);
        lblModulesTakenFrom.setVisible(view);
        btnGoBack.setBounds(828, 13, 120, 25);
        label_7.setVisible(view);
    }

    /**
     * sets up the components
     */
    public void setupComponents() {
        try {
            findStudentDetails(regNum);
            label_4.setText(degreeCode);
            label_5.setText(levelCode);
            label_13.setText(periodLabel);

            ArrayList<String> availableOptModules = new ArrayList<String>();
            ResultSet result;
            result = con.performQuery("SELECT * FROM ModuleApproval WHERE degreeCode='" + degreeCode + "' AND levelCode='" + levelCode + "' AND core=0");
            while (result.next()) {
                availableOptModules.add(result.getString(1));
            }
            con.closeConnection();
            con.closeStatement();

            //get all the modules
            ArrayList<String> studentModules = new ArrayList<String>();
            result = con.performQuery("SELECT * FROM Study WHERE registrationNo='" + Integer.parseInt(regNum) + "' AND periodLabel='" + periodLabel + "' ");
            while (result.next()) {
                studentModules.add(result.getString(4));
            }
            //close connection and statement
            con.closeConnection();
            con.closeStatement();

            //filter out the core modules for the students degree and level
            result = con.performQuery("SELECT moduleCode FROM ModuleApproval WHERE levelCode= '" + levelCode + "' AND degreeCode='" + degreeCode + "' AND core=1");
            while (result.next()) {
                studentModules.remove(result.getString(1));       //remove obligatory modules from student optional modules
            }
            //close connection and statement
            con.closeConnection();
            con.closeStatement();

            comboBox_1.removeAllItems();
            comboBox_2.removeAllItems();
            comboBox_1.addItem("");
            comboBox_2.addItem("");
            for (String str : availableOptModules) {
                if (studentModules.contains(str)) {
                    comboBox_1.removeItem(str);
                    comboBox_2.addItem(str);
                }

                else {
                    comboBox_1.addItem(str);
                    comboBox_2.removeItem(str);
                }
            }

            recordTable.setModel(registrar.findStudentRecord(regNum, periodLabel, levelCode, degreeCode));

            //check if the student has failed by
            //by transforming the period label to an ascii character so that we get its integer
            if (((int) periodLabel.charAt(0) - 64) <= Integer.parseInt(levelCode) ||
                    ((registrar.checkLevelExists(degreeCode, "P")) && levelCode.equals(String.valueOf(registrar.findDegreePeriod(degreeCode))))) {
                reqTable.setModel(registrar.viewModules(degreeCode, levelCode, 1));
                optTable.setModel(registrar.viewModules(degreeCode, levelCode, 0));
            }
            //if a student has failed a module
            else {
                totalCredits = registrar.getCurrentCredits();
                comboBox_1.removeAllItems();
                comboBox_2.removeAllItems();
            }
            label_7.setText(String.valueOf(totalCredits));
            label_11.setText(String.valueOf(registrar.getCurrentCredits()));
            label_12.setText(String.valueOf(totalCredits - registrar.getCurrentCredits()));
        } catch (SQLException e1) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
    }

}


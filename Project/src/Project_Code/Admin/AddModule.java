package Project_Code.Admin;


import Project_Code.DBController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddModule extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTextField moduleCodeField;
    private JTextField moduleNameField;
    private JTextField creditsField;
    private JTextField degreeCodeField;
    private JComboBox<String> periodDropdown;
    private JComboBox<String> yearDropdown;
    private JComboBox<String> coreDropdown;
    private static DBController con = new DBController("team037", "ee143bc0");

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public AddModule(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Add Module:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // Module code label
        JLabel moduleCodeLabel = new JLabel("Module Code:");
        moduleCodeLabel.setBounds(57, 112, 134, 25);
        add(moduleCodeLabel);

        moduleCodeField = new JTextField(20);
        moduleCodeField.setBounds(211, 112, 132, 25);
        add(moduleCodeField);

        // Module name label
        JLabel moduleNameLabel = new JLabel("Module Name:");
        moduleNameLabel.setBounds(57, 152, 134, 25);
        add(moduleNameLabel);

        moduleNameField = new JTextField(20);
        moduleNameField.setBounds(211, 152, 132, 25);
        add(moduleNameField);

        // Credits label
        JLabel creditsLabel = new JLabel("Credits:");
        creditsLabel.setBounds(57, 192, 134, 25);
        add(creditsLabel);

        creditsField = new JTextField(20);
        creditsField.setBounds(211, 192, 132, 25);
        add(creditsField);

        // Department code label
        JLabel degreeCodeLabel = new JLabel("Degree Code:");
        degreeCodeLabel.setBounds(57, 232, 134, 25);
        add(degreeCodeLabel);

        degreeCodeField = new JTextField(20);
        degreeCodeField.setBounds(211, 232, 132, 25);
        add(degreeCodeField);

        // Period code label
        JLabel periodCodeLabel = new JLabel("Period Code:");
        periodCodeLabel.setBounds(57, 272, 134, 25);
        add(periodCodeLabel);

        periodDropdown = new JComboBox<String>(new String[]{"Year", "Autumn", "Spring"});
        periodDropdown.setBounds(211, 272, 132, 25);
        add(periodDropdown);

        // Year label
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setBounds(57, 312, 134, 25);
        add(yearLabel);

        yearDropdown = new JComboBox<String>(new String[]{"1", "2", "3", "4"});
        yearDropdown.setBounds(211, 312, 132, 25);
        add(yearDropdown);

        // Core label
        JLabel coreLabel = new JLabel("Core:");
        coreLabel.setBounds(57, 352, 134, 25);
        add(coreLabel);

        coreDropdown = new JComboBox<String>(new String[]{"YES", "NO"});
        coreDropdown.setBounds(211, 352, 132, 25);
        add(coreDropdown);

        //Add button
        JButton btnAddUsr = new JButton("Add");
        btnAddUsr.setBounds(291, 432, 97, 25);
        add(btnAddUsr);
        btnAddUsr.addActionListener(this);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 432, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 400, 537);
        frame.setLocationRelativeTo(null);
    }

    private boolean validateFields(String moduleCode, String moduleName, String credits, String degreeCode) {
        //check if values have been given
        if (moduleCode.equals("")) {
            JOptionPane.showMessageDialog(null,"Module code is a required field.");
            return false;
        } else if (moduleName.equals("")) {
            JOptionPane.showMessageDialog(null,"Module name is a required field.");
            return false;
        } else if (credits.equals("")) {
            JOptionPane.showMessageDialog(null,"Credits field is required.");
            return false;
        } else if (degreeCode.equals("")) {
            JOptionPane.showMessageDialog(null,"Degree code is a required field.");
            return false;
        } else if (moduleCode.length() >= 8) {
            JOptionPane.showMessageDialog(null,"Module code is too long.");
            return false;
        } else if (moduleName.length() >= 256) {
            JOptionPane.showMessageDialog(null,"Module name is too long.");
            return false;
        } else if (degreeCode.length() >= 7) {
            JOptionPane.showMessageDialog(null,"Module name is too long.");
            return false;
        } else if (this.admin.getRole() != "admin") {
            System.out.println(this.admin.getRole());
            JOptionPane.showMessageDialog(null,"Access denied.");
            return false;
        } else {

            try {
                int creditsInt = Integer.parseInt(credits);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,"Credits not a number.");
                ex.printStackTrace();
                return false;
            } finally {
                return true;
            }

        }

    }

    private boolean checkModuleCode(String moduleCode, String degreeCode) throws SQLException {

        ResultSet result;
        ResultSet sResult;

        try {

            String query = "SELECT COUNT(*) FROM Module WHERE moduleCode ='"+moduleCode+"'";
            System.out.println(query);
            result = con.performQuery(query);

            int count = 0;
            while(result.next()) {
                System.out.println(result.getInt(1));
                count = result.getInt(1);
            }

            if (count == 0) {
                System.out.println("New module");

                String sQuery = "SELECT COUNT(*) FROM Degree WHERE degreeCode ='"+degreeCode+"'";
                System.out.println(sQuery);
                sResult = con.performQuery(sQuery);

                int sCount = 0;
                while(sResult.next()) {
                    sCount = sResult.getInt(1);
                }

                if (sCount == 0) {
                    System.out.println("New degree - bad");
                    JOptionPane.showMessageDialog(null,"This degree code does not exist.");
                    return false;
                }

            } else {
                System.out.println("Module found");
                JOptionPane.showMessageDialog(null,"This module code already exists.");
                return false;
            }

            System.out.println(result);

            con.closeConnection();
            con.closeStatement();

            return true;

        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error, SQL.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            ex.printStackTrace();
            System.exit(0);
        }

        return false;
    }

    private boolean addModule(String moduleCode, String moduleName, String credits, String degreeCode) throws SQLException {

        try {

            int core = 0;
            if (coreDropdown.getSelectedItem() == "YES") {
                core = 1;
            }

            int changes;
            //insert to Modules
            changes = con.performUpdate("INSERT INTO Module (moduleCode, moduleName, credits, periodCode)"
                    + " VALUES ('"+moduleCode+"','"+moduleName+"','"+Integer.parseInt(credits)+"','"+periodDropdown.getSelectedItem()+"')");

            changes += con.performUpdate("INSERT INTO ModuleApproval (moduleCode, degreeCode, levelCode, core)"
                    + " VALUES ('"+moduleCode+"','"+degreeCode+"','"+yearDropdown.getSelectedItem()+"','"+core+"')");

            if (changes>0) {
                con.closeConnection();
                con.closeStatement();

                return true;
            }

        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error, SQL.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            ex.printStackTrace();
            System.exit(0);
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));
            case "Add":
                String moduleCode = moduleCodeField.getText().replaceAll("\\p{Punct}", "");
                String moduleName = moduleNameField.getText().replaceAll("\\p{Punct}", "");
                String moduleCredits = creditsField.getText().replaceAll("\\p{Punct}", "");
                String degreeCode = degreeCodeField.getText().replaceAll("\\p{Punct}", "");

                if (validateFields(moduleCode, moduleName, moduleCredits, degreeCode)) {

                    try {
                        if (checkModuleCode(moduleCode, degreeCode)) {
                            // Department code does not exist at this point
                            if(addModule(moduleCode, moduleName, moduleCredits, degreeCode)) {
                                JOptionPane.showMessageDialog(null,"Module Registered Successfully");
                                setVisible(false);
                                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));
                            }
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (NullPointerException n) {
                        n.printStackTrace();
                    }

                }
        }
    }
}
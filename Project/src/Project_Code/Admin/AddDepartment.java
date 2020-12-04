package Project_Code.Admin;


import Project_Code.DBController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddDepartment extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTextField departmentCodeField;
    private JTextField departmentNameField;
    private static DBController con = new DBController("team037", "ee143bc0");

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public AddDepartment(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Add Department:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // departmentCode
        JLabel departmentCode = new JLabel("Department Code:");
        departmentCode.setBounds(57, 87, 134, 25);
        add(departmentCode);

        // departmentCodeField
        departmentCodeField = new JTextField(20);
        departmentCodeField.setBounds(211, 87, 132, 25);
        add(departmentCodeField);

        // departmentName label
        JLabel departmentName = new JLabel("Department Name:");
        departmentName.setBounds(57, 127, 134, 25);
        add(departmentName);

        // departmentNameField
        departmentNameField = new JTextField(20);
        departmentNameField.setBounds(211, 127, 132, 25);
        add(departmentNameField);

        //Add button
        JButton btnRemoveUsr = new JButton("Add");
        btnRemoveUsr.setBounds(291, 192, 97, 25);
        add(btnRemoveUsr);
        btnRemoveUsr.addActionListener(this);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 192, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 400, 297);
        frame.setLocationRelativeTo(null);

    }

    private boolean validateFields(String deptCode, String deptName) {
        //check if values have been given
        if (deptCode.equals("")) {
            JOptionPane.showMessageDialog(null,"Department code is a required field.");
            return false;
        } else if (deptName.equals("")) {
            JOptionPane.showMessageDialog(null,"Department name is a required field.");
            return false;
        } else if (deptCode.length() >= 4) {
            JOptionPane.showMessageDialog(null,"Department code is too long.");
            return false;
        } else if (deptName.length() >= 90) {
            JOptionPane.showMessageDialog(null,"Department name is too long.");
            return false;
        } else if (this.admin.getRole() != "Admin") {
            JOptionPane.showMessageDialog(null,"Access denied.");
            return false;
        } else {
            return true;
        }

    }

    private boolean checkDeptCode(String deptCode) throws SQLException {

        ResultSet result;

        try {

            String query = "SELECT COUNT(*) FROM Department WHERE deptCode ='"+deptCode+"'";
            System.out.println(query);
            result = con.performQuery(query);

            int count = 0;
            while(result.next()) {
                System.out.println(result.getInt(1));
                count = result.getInt(1);
            }

            if (count == 0) {
                System.out.println("New department");
            } else {
                System.out.println("Repeat found");
                JOptionPane.showMessageDialog(null,"This department code already exists.");
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

    private boolean addDepartment(String deptCode, String deptName) throws SQLException {

        try {

            int changes;
            //insert to Department
            changes = con.performUpdate("INSERT INTO Department (deptCode, deptName)"
                    + " VALUES ('"+deptCode+"','"+deptName+"')");

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
                String deptCode = departmentCodeField.getText().replaceAll("\\p{Punct}", "");
                String deptName = departmentNameField.getText().replaceAll("\\p{Punct}", "");

                if (validateFields(deptCode, deptName)) {
                    try {
                        if (checkDeptCode(deptCode)) {
                            // Department code does not exist at this point
                            if(addDepartment(deptCode, deptName)) {
                                JOptionPane.showMessageDialog(null,"Department Registered Successfully");
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
                break;
        }
    }
}
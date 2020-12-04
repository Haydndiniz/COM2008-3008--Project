package Project_Code.Admin;


import Project_Code.DBController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AddCourse extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTextField courseCodeField;
    private JTextField courseNameField;
    private final JComboBox<String> comboBox;
    private JList<String> comboList;
    private static DBController con = new DBController("team037", "ee143bc0");

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public AddCourse(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Add Course:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // courseCode
        JLabel courseCode = new JLabel("Course Code:");
        courseCode.setBounds(57, 87, 134, 25);
        add(courseCode);

        // courseCodeField
        courseCodeField = new JTextField(20);
        courseCodeField.setBounds(211, 87, 132, 25);
        add(courseCodeField);

        // courseName
        JLabel courseName = new JLabel("Course Name:");
        courseName.setBounds(57, 127, 134, 25);
        add(courseName);

        // courseNameField
        courseNameField = new JTextField(20);
        courseNameField.setBounds(211, 127, 132, 25);
        add(courseNameField);

        // courseName
        JLabel leadDept = new JLabel("Lead Department:");
        leadDept.setBounds(57, 167, 134, 25);
        add(leadDept);

        //degree combo Box
        comboBox = new JComboBox<>();
        comboBox.setBounds(211, 167, 132, 22);
        comboBox.addItem("");
        ResultSet result;
        try {
            result=con.performQuery("SELECT * FROM Department"); //get the degree codes from the database
            while (result.next())
                comboBox.addItem(result.getString(1));
            //close connection and statement
            con.closeConnection();
            con.closeStatement();
        } catch (SQLException e) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        add(comboBox);

        // optDepts Label
        JLabel optDepts = new JLabel("Other Departments:");
        optDepts.setBounds(57, 207, 134, 25);
        add(optDepts);

        // optDepts List
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("");
        ResultSet resultOptDepts;
        try {
            result=con.performQuery("SELECT * FROM Department"); //get the degree codes from the database
            while (result.next())
                listModel.addElement(result.getString(1));
            //close connection and statement
            con.closeConnection();
            con.closeStatement();

            // continue creating list

            comboList = new JList<>(listModel);
            comboList.setBounds(211, 207, 132, 100);
            comboList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            add(comboList);

        } catch (SQLException e) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        //Add button
        JButton btnRemoveUsr = new JButton("Add");
        btnRemoveUsr.setBounds(291, 332, 97, 25);
        add(btnRemoveUsr);
        btnRemoveUsr.addActionListener(this);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 332, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 400, 417);
        frame.setLocationRelativeTo(null);

    }

    private boolean validateFields(String courseCode, String courseName) {
        //check if values have been given
        if (courseCode.equals("")) {
            JOptionPane.showMessageDialog(null,"Course code is a required field.");
            return false;
        } else if (courseName.equals("")) {
            JOptionPane.showMessageDialog(null,"Course name is a required field.");
            return false;
        } else if (courseCode.length() >= 7) {
            JOptionPane.showMessageDialog(null,"Course code is too long.");
            return false;
        } else if (courseName.length() >= 256) {
            JOptionPane.showMessageDialog(null,"Course name is too long.");
            return false;
        } else if (this.admin.getRole() != "admin") {
            JOptionPane.showMessageDialog(null,"Access denied.");
            return false;
        } else if (comboBox.getSelectedItem() == "") {
            JOptionPane.showMessageDialog(null,"You must choose a Lead department.");
            return false;
        } else {
            return true;
        }

    }

    private boolean checkCourseCode(String courseCode) throws SQLException {

        ResultSet result;

        try {

            String query = "SELECT COUNT(*) FROM Degree WHERE degreeCode ='"+courseCode+"'";
            System.out.println(query);
            result = con.performQuery(query);

            int count = 0;
            while(result.next()) {
                System.out.println(result.getInt(1));
                count = result.getInt(1);
            }

            if (count == 0) {
                System.out.println("New course");
            } else {
                System.out.println("Repeat found");
                JOptionPane.showMessageDialog(null,"This degree code already exists.");
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

    private boolean addCourse(String courseCode, String courseName) throws SQLException {

        try {

            List<String> selectedItems = comboList.getSelectedValuesList();
            if (selectedItems.contains(comboBox.getSelectedItem())) {
                selectedItems.remove(comboBox.getSelectedItem());
                System.out.println(selectedItems);
        }

            int changes;
            int lead = 1;
            int nLead = 0;
            //insert to Department
            changes = con.performUpdate("INSERT INTO Degree (degreeCode, degreeName)"
                    + " VALUES ('"+courseCode+"','"+courseName+"')");

            changes += con.performUpdate("INSERT INTO DegreeApproval (deptCode, degreeCode, leadDept)"
                    + " VALUES ('"+comboBox.getSelectedItem()+"','"+courseCode+"','"+lead+"')");

            for (String temp : selectedItems) {
                changes += con.performUpdate("INSERT INTO DegreeApproval (deptCode, degreeCode, leadDept)"
                        + " VALUES ('"+temp+"','"+courseCode+"','"+nLead+"')");
            }

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
                String courseCode = courseCodeField.getText().replaceAll("\\p{Punct}", "");
                String courseName = courseNameField.getText().replaceAll("\\p{Punct}", "");

                if (validateFields(courseCode, courseName)) {
                    try {
                        if (checkCourseCode(courseCode)) {
                            // Department code does not exist at this point
                            if(addCourse(courseCode, courseName)) {
                                JOptionPane.showMessageDialog(null,"Course Registered Successfully");
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
package Project_Code.Admin;


import Project_Code.DBController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveCourse extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTextField courseField;
    private static DBController con = new DBController("team037", "ee143bc0");

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public RemoveCourse(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Remove Course:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // Course label
        JLabel roleLabel = new JLabel("Course Code:");
        roleLabel.setBounds(57, 87, 134, 25);
        add(roleLabel);

        // courseField
        courseField = new JTextField(20);
        courseField.setBounds(211, 87, 132, 25);
        add(courseField);

        //Add button
        JButton btnRemoveUsr = new JButton("Remove");
        btnRemoveUsr.setBounds(291, 152, 97, 25);
        add(btnRemoveUsr);
        btnRemoveUsr.addActionListener(this);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 152, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 400, 257);
        frame.setLocationRelativeTo(null);
    }

    private boolean deleteCourse(String degreeCode) throws SQLException {

        ResultSet result;

        try {

            String query = "SELECT COUNT(*) FROM Degree WHERE degreeCode ='"+degreeCode+"'";
            System.out.println(query);
            result = con.performQuery(query);

            int count = 0;
            while(result.next()) {
                System.out.println(result.getInt(1));
                count = result.getInt(1);
            }

            if (count != 0) {
                System.out.println("Degree found.");
                // DELETE
                int changes;

                changes=con.performUpdate("DELETE FROM DegreeApproval WHERE degreeCode = '"+degreeCode+"' ");
                changes+=con.performUpdate("DELETE FROM Degree WHERE degreeCode = '"+degreeCode+"' ");

                con.closeConnection();
                con.closeStatement();

                JOptionPane.showMessageDialog(null,"Degree deleted successfully");
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));

            } else {
                System.out.println("Degree does not exist.");
                JOptionPane.showMessageDialog(null,"Degree not found.",
                        "ERROR", JOptionPane.ERROR_MESSAGE, null);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));
                break;
            case "Remove":

                if (courseField.equals("")) {
                    JOptionPane.showMessageDialog(null,"Degree code is a required field.");
                } else {
                    try {
                        deleteCourse(courseField.getText());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

        }
    }
}
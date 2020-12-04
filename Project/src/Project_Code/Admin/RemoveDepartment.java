package Project_Code.Admin;


import Project_Code.DBController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveDepartment extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTextField departmentField;
    private static DBController con = new DBController("team037", "ee143bc0");

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public RemoveDepartment(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Remove Department:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // Department label
        JLabel roleLabel = new JLabel("Department Code:");
        roleLabel.setBounds(57, 87, 134, 25);
        add(roleLabel);

        // UsernameField
        departmentField = new JTextField(20);
        departmentField.setBounds(211, 87, 132, 25);
        add(departmentField);

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

            if (count != 0) {
                System.out.println("Department found.");
                // DELETE
                int changes;
                changes=con.performUpdate("DELETE FROM Department WHERE deptCode = '"+deptCode+"' ");
                con.closeConnection();
                con.closeStatement();

                JOptionPane.showMessageDialog(null,"Department deleted successfully");
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));

            } else {
                System.out.println("Department does not exist.");
                JOptionPane.showMessageDialog(null,"Department does not exist.",
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
            case "Remove":

                if (departmentField.equals("")) {
                    JOptionPane.showMessageDialog(null,"Department code is a required field.");
                } else {
                    try {
                        checkDeptCode(departmentField.getText());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

        }
    }
}
package Project_Code.Admin;

import Project_Code.DBController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveUser extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTextField usernameField;
    private static DBController con = new DBController("team037", "ee143bc0");

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public RemoveUser(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Remove User:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // Username label
        JLabel roleLabel = new JLabel("Username:");
        roleLabel.setBounds(57, 87, 134, 25);
        add(roleLabel);

        // UsernameField
        usernameField = new JTextField(20);
        usernameField.setBounds(211, 87, 132, 25);
        add(usernameField);

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

    private boolean deleteUsername(String username) throws SQLException {

        ResultSet result;

        try {

            String query = "SELECT COUNT(*) FROM Users WHERE username ='"+username+"'";
            System.out.println(query);
            result = con.performQuery(query);

            int count = 0;
            while(result.next()) {
                System.out.println(result.getInt(1));
                count = result.getInt(1);
            }

            if (count != 0) {
                System.out.println("User found.");
                // DELETE
                int changes;
                changes=con.performUpdate("DELETE FROM Users WHERE username = '"+username+"' ");
                changes+=con.performUpdate("DELETE FROM UserSalts WHERE username = '"+username+"' ");
                changes+=con.performUpdate("DELETE FROM UserAccounts WHERE username = '"+username+"' ");

                System.out.println(changes);
                con.closeConnection();
                con.closeStatement();

                JOptionPane.showMessageDialog(null,"User deleted successfully");
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));

            } else {
                System.out.println("User does not exist.");
                JOptionPane.showMessageDialog(null,"User not found.",
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

                if (usernameField.equals("")) {
                    JOptionPane.showMessageDialog(null,"Username is a required field.");
                } else {
                    try {
                        deleteUsername(usernameField.getText());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

        }
    }
}
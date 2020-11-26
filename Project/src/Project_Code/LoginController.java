package Project_Code;

import Project_Code.*;
import Project_Code.Admin.*;

import java.sql.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * LoginController
 * Manages user login and access to the system
 */
public class LoginController extends JPanel implements ActionListener {

        private JFrame frame;
        private JPasswordField passwordField;
        private JTextField usernameTextField;
        private static JMenuItem logoutItem;
        private static DBController con = new DBController("team037", "ee143bc0");

    public LoginController(Main f,JMenuItem logout) {
        //attributes
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(120, 79, 86, 13);
        add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(120, 111, 66, 13);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setText((String) null);
        passwordField.setBounds(222, 107, 86, 19);
        add(passwordField);

        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(172, 159, 76, 19);
        add(loginButton);

        usernameTextField = new JTextField();
        usernameTextField.setText((String) null);
        usernameTextField.setColumns(10);
        usernameTextField.setBounds(222, 75, 86, 19);
        add(usernameTextField);

        //jframe settings
        frame=f;
        f.setContentPane(this);
        frame.setBounds(100, 100, 450, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        loginButton.addActionListener(this);
        logoutItem=logout;
        logoutItem.setVisible(false);
        //press enter to login
        frame.getRootPane().setDefaultButton(loginButton);
        loginButton.requestFocus();
    }

    public static boolean validateUsername(String username)  {
        ResultSet result = null;
        int exists = 0;
        try {
            //check firstly if the username exists in the database before extracting the password
            System.out.println(username);
            result = con.performQuery("SELECT 1 FROM Users WHERE username = '" + username + "'");
            while(result.next()) {
                exists = Integer.valueOf(result.getString(1));
            }
            return (exists!=0);

        }
        catch (SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(1);

        }
        catch (NullPointerException nex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(2);
        }

        return (exists!=0);

    }
    public static boolean validatePassword(String username, String password) {
        String pass= password;

        ResultSet result = null;
        String storedPass = null;
        try{
            //collate is used for case sensitivity
            result = con.performQuery("SELECT password FROM Users WHERE username = '" + username + "'");
            System.out.println(result);
            while (result.next())
                storedPass = result.getString(1);

            return (pass.equals(storedPass));
        }
        catch (SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        catch (NullPointerException nex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.2",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(1);
        }

        return (pass.equals(storedPass));

    }

    public static boolean validateCredentials(String username, String password) {
        //check if values have been given
        if (username.equals("")) {
            logoutItem.setVisible(false);
            JOptionPane.showMessageDialog(null,"Username is a required field.");
            return false;
        }
        if (password.equals("")) {
            logoutItem.setVisible(false);
            JOptionPane.showMessageDialog(null,"Password is a required field.");
            return false;
        }
        //check if the username is valid
        if (!validateUsername(username)) {
            logoutItem.setVisible(false);
            JOptionPane.showMessageDialog(null,"The username and password combination you have entered is incorrect.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            return false;

        }
        //check if the password is valid
        else if (!validatePassword(username, password)) {
            logoutItem.setVisible(false);
            JOptionPane.showMessageDialog(null,"The username and password combination you have entered is incorrect.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        else
            return true;
    }

        @Override
    public void actionPerformed(ActionEvent e) {
            logoutItem.setVisible(true);
            String buttonPressed = e.getActionCommand();
            if (buttonPressed.equals("Log In")) {
                //get the username
                //removes punctuation from the input, for security
                String username = usernameTextField.getText().replaceAll("\\p{Punct}", "");
                //get the password for that username
                String password = new String(passwordField.getPassword());
                if (validateCredentials(username, password)) { //validates the credentials
//                    //find the role
//                    ResultSet result = null;
                    String role = "Administrator";
//                    try {
//                        result = con.performQuery("SELECT role FROM Users WHERE username =  '" + username + "'");
//                        while (result.next()) {
//                            role = result.getString(1);
//                        }
//
//                    } catch (SQLException ex) {
//                        //display error message and leave the application
//                        JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
//                                "ERROR", JOptionPane.ERROR_MESSAGE, null);
//                        System.exit(0);
//                    }
                switch (role) {
                    case "Administrator":
                        setVisible(false);
                        frame.setContentPane(new AdminFrame(frame, username));
                        break;

//                        case "Registrar":
//                            setVisible(false);
//                            frame.setContentPane(new RegistrarMenu(frame, username));
//                            break;
//
//                        case "Teacher":
//                            setVisible(false);
//                            frame.setContentPane(new TeacherMenu(frame, username));
//                            break;
//
//                        case "Student":
//                            setVisible(false);
//                            frame.setContentPane(new StudentMenu(frame, username));
//                            break;
                }
                }
            }

    }
}
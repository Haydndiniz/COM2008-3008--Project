package Project_Code;

import Project_Code.Admin.*;
import Project_Code.Registrar.*;
import Project_Code.Teacher.*;
import Project_Code.Student.*;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

    private static boolean validateUsername(String username)  {
        int exists = 0;
        try {
            //check firstly if the username exists in the database before extracting the password
            ResultSet result = con.performQuery("SELECT 1 FROM UserAccounts WHERE username = '" + username + "'");
            while(result.next()) {
                exists = Integer.parseInt(result.getString(1));
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
    private static boolean validatePassword(String username, String password) {
        ResultSet result = null;
        String storedPass = null;
        String saltRes = null;
        try{
            //get stored password in hash format
            result = con.performQuery("SELECT password FROM UserAccounts WHERE username = '" + username + "'");
            while (result.next()) {
                storedPass = result.getString(1);
            }
//
            ResultSet salt = con.performQuery("SELECT salt FROM UserSalts WHERE username = '" + username + "'");
            while (salt.next()) {
                saltRes = salt.getString(1);
            }

            String hashed = AddUser.createHash(password, AddUser.saltDecode(saltRes));
            System.out.println(hashed);
            return (hashed.equals(storedPass));


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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return (password.equals(storedPass));

    }

    private static boolean validateCredentials(String username, String password) {
        //check if values have been given
        if (username.equals("")) {
            JOptionPane.showMessageDialog(null,"Username is a required field.");
            return false;
        }
        if (password.equals("")) {
            JOptionPane.showMessageDialog(null,"Password is a required field.");
            return false;
        }
        //check if the username is valid
        if (!validateUsername(username)) {
            JOptionPane.showMessageDialog(null,"The username and password combination you have entered is incorrect.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            return false;

        }
        //check if the password is valid
        else if (!validatePassword(username, password)) {
            JOptionPane.showMessageDialog(null,"The username and password combination you have entered is incorrect.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        else
            return true;
    }

        @Override
    public void actionPerformed(ActionEvent e) {
            String buttonPressed = e.getActionCommand();
            if (buttonPressed.equals("Log In")) {
                //get the username
                //removes punctuation from the input, for security
                String username = usernameTextField.getText().replaceAll("\\p{Punct}", "");
                //get the password for that username
                String password = new String(passwordField.getPassword());
                if (validateCredentials(username, password)) { //validates the credentials
                    logoutItem.setVisible(true);
//                    //find the role
                    ResultSet result = null;
                    String role = "";
                    try {
                        result = con.performQuery("SELECT role FROM Users WHERE username =  '" + username + "'");
                        while (result.next()) {
                            role = result.getString(1);
                        }

                    } catch (SQLException ex) {
                        //display error message and leave the application
                        JOptionPane.showMessageDialog(null, "There was an error when processing the data.",
                                "ERROR", JOptionPane.ERROR_MESSAGE, null);
                        System.exit(0);
                    }
                switch (role) {

                    case "admin":
                        setVisible(false);
                        frame.setContentPane(new AdminFrame(frame, username));
                        break;
                    case "registrar":
                        setVisible(false);
                        frame.setContentPane(new RegistrarFrame(frame, username));
                        break;
                    case "teacher":
                        setVisible(false);
                        frame.setContentPane(new SearchByStudent(frame, username));
                        break;
                     case "student":
                         setVisible(false);
                         frame.setContentPane(new StudentFrame(frame, username));
                         break;
                }
                }
            }

    }
}
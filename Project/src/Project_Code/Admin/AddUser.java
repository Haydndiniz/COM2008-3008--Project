package Project_Code.Admin;

import Project_Code.DBController;
//import com.sun.xml.internal.rngom.parse.host.Base;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;
import java.util.Random;

public class AddUser extends JPanel implements ActionListener {
    private JFrame frame;
    private JTextField forenameField;
    private JTextField surnameField;
    private AdminMain admin;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;
    private JComboBox<String> titleDropdown;
    private static final Random RANDOM = new SecureRandom();
    private static DBController con = new DBController("team037", "ee143bc0");


    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public AddUser(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Add User:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // role label
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(57, 112, 134, 25);
        add(roleLabel);

        roleDropdown = new JComboBox<String>(new String[]{"admin", "registrar", "teacher"});
        roleDropdown.setBounds(211, 112, 132, 25);
        add(roleDropdown);

        // title label
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(57, 152, 134, 25);
        add(titleLabel);

        titleDropdown = new JComboBox<String>(new String[]{"Mr", "Ms"});
        titleDropdown.setBounds(211, 152, 132, 25);
        add(titleDropdown);

        // forename label
        JLabel forenameLabel = new JLabel("Forename:");
        forenameLabel.setBounds(57, 192, 134, 25);
        add(forenameLabel);

        // forenameField
        forenameField = new JTextField(20);
        forenameField.setBounds(211, 192, 132, 25);
        add(forenameField);

        // surname label
        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setBounds(57, 232, 134, 25);
        add(surnameLabel);

        // surnameField
        surnameField = new JTextField(20);
        surnameField.setBounds(211, 232, 132, 25);
        add(surnameField);

        //password label
        JLabel label_5 = new JLabel("Password:");
        label_5.setBounds(57, 272, 134, 25);
        add(label_5);

        //password field
        passwordField = new JPasswordField();
        passwordField.setText(generateRandomPassword());
        passwordField.setBounds(211, 272, 132, 25);
        add(passwordField);

        //show password checkbox
        JCheckBox passwordCheckbox = new JCheckBox("Show Password");
        passwordCheckbox.setBounds(209,300,140,22);
        passwordCheckbox.addActionListener(e -> {
            if(passwordCheckbox.isSelected()){
                passwordField.setEchoChar((char)0);
            } else{
                passwordField.setEchoChar('*');

            }
        });
        add(passwordCheckbox);

        //Add button
        JButton btnAddUsr = new JButton("Add User");
        btnAddUsr.setBounds(291, 392, 97, 25);
        add(btnAddUsr);
        btnAddUsr.addActionListener(this);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 392, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 400, 497);
        frame.setLocationRelativeTo(null);
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



    private boolean validateFields(String forename, String surname) {
        //check if values have been given
        if (forename.equals("")) {
            JOptionPane.showMessageDialog(null,"Forename is a required field.");
            return false;
        } else if (surname.equals("")) {
            JOptionPane.showMessageDialog(null,"Surname is a required field.");
            return false;
        } else if (forename.length() >= 45) {
            JOptionPane.showMessageDialog(null,"Forename is too long.");
            return false;
        } else if (surname.length() >= 45) {
            JOptionPane.showMessageDialog(null,"Surname is too long.");
            return false;
        } else if (this.admin.getRole() != "admin") {
            JOptionPane.showMessageDialog(null,"Access denied.");
            return false;
        } else {
            return true;
        }

    }

    private String createUsername(String forename, String surname) throws SQLException {

        ResultSet result;

        try {
            String shortenedForename = forename.charAt(0) + "%";
            String query = "SELECT COUNT(*) FROM Users WHERE forename LIKE '"+shortenedForename+"' AND surname = '"+surname+"'";
            System.out.println(query);
            result = con.performQuery(query);

            int count = 0;
            while(result.next()) {
                System.out.println(result.getInt(1));
                count = result.getInt(1);
            }

            if (count == 0) {
                System.out.println("New User");
            } else {
                System.out.println("Repeat found");
                return forename.charAt(0) + surname + String.valueOf(count+1);
            }

            System.out.println(result);

            con.closeConnection();
            con.closeStatement();

            return forename.charAt(0) + surname + "1";

        }
        catch(SQLException ex) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error, SQL.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            ex.printStackTrace();
            System.exit(0);
        }

        return "ERROR";
    }

    public static byte[] createSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        return salt;
    }
    public static String saltEncode(byte[] res){

        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(res);
    }

    public static byte[] saltDecode(String encodedSalt){

        Base64.Decoder enc = Base64.getDecoder();
        return enc.decode(encodedSalt);
    }




    public static String createHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(hash);
    }


    private boolean addUser(String username, String role, String forename, String surname, String title, String email, String hash, String salt) throws SQLException {

        try {

            System.out.println(username);
            System.out.println(role);
            System.out.println(forename);
            System.out.println(surname);
            System.out.println(title);
            System.out.println(email);
            System.out.println(hash);
            System.out.println(salt);

            int changes;
            //insert to UserAccounts
            changes = con.performUpdate("INSERT INTO UserAccounts (username, password) VALUES ('"+username+"','"+hash+"')");
            //insert to UserSalts
            changes += con.performUpdate("INSERT INTO UserSalts (username, salt)"
                    + " VALUES ('"+username+"','"+salt+"')");
            //insert to Users
            changes += con.performUpdate("INSERT INTO Users (username, role, forename, surname, title, email)"
                    + " VALUES ('"+username+"','"+role+"','"+forename+"','"+surname+"','"+title+"','"+email+"')");

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
                break;
            case "Add User":
                //setVisible(false);
                String forename = forenameField.getText().replaceAll("\\p{Punct}", "");
                String surname = surnameField.getText().replaceAll("\\p{Punct}", "");

                if (validateFields(forename, surname)) {
                    // generate username
                    String username = null;
                    try {
                        username = createUsername(forename, surname);

                        byte[] salt = createSalt();
                        String encodedSalt = saltEncode(salt);

                        String password = new String(passwordField.getPassword());
                        String hash = createHash(password, salt);
                        String email = username+"@sheffield.ac.uk";

                        if (addUser(username, roleDropdown.getSelectedItem().toString(), forename, surname, titleDropdown.getSelectedItem().toString(), email, hash, encodedSalt)) {
                            JOptionPane.showMessageDialog(null,"User Registered Successfully");
                            setVisible(false);
                            frame.setContentPane(new AdminFrame(frame, admin.getUsername()));
                        }


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (NullPointerException n) {
                        n.printStackTrace();
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        noSuchAlgorithmException.printStackTrace();
                    } catch (InvalidKeySpecException invalidKeySpecException) {
                        invalidKeySpecException.printStackTrace();
                    }
                    System.out.println(username);

                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + buttonPressed);
        }
    }

}

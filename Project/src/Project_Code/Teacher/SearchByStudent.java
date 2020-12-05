package Project_Code.Teacher;

import Project_Code.LoginController;

import javax.swing.*;
import java.awt.event.*;

public class SearchByStudent extends JPanel implements ActionListener {
    private JFrame frame;
    private TeacherMain teacher;
    private JTextField usernameTextField;
    private JTextField regNoTextField;

    /**
     * Parameterized constructor
     * Creates the frame containing the search student grades by username/registration no fields
     * @param f - the frame created
     * @param username - object of type Teacher
     */
    public SearchByStudent(JFrame f, String username) {
        teacher = new TeacherMain(username, "Teacher");

        setBounds(87, 13, 450, 300);
        setLayout(null);

        JLabel label_0 = new JLabel("Search for a student by registration number OR username");
        label_0.setBounds(0, 0, 450, 25);
        label_0.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_0);

        //search button
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(337, 213, 97, 25);
        add(searchButton);
        searchButton.addActionListener(this);

        JLabel label_1 = new JLabel("Reg number:");
        label_1.setBounds(12, 63, 97, 25);
        add(label_1);

        JLabel label_2 = new JLabel("OR");
        label_2.setBounds(12, 88, 97, 25);
        add(label_2);

        JLabel label_3 = new JLabel("Username:");
        label_3.setBounds(12, 113, 97, 25);
        add(label_3);

        JTextField textField1 = new JTextField();
        regNoTextField = textField1;
        JTextField textField2 = new JTextField();
        usernameTextField = textField2;
        textField1.setBounds(86, 63, 350, 25);
        textField2.setBounds(86, 113, 350, 25);
        textField1.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (textField1.getText().length() >= 9 ) // limit textfield to 9 characters
                    e.consume();
                textField2.setText("");
            }
        });
        textField2.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (textField2.getText().length() >= 45 )
                    e.consume();
                textField1.setText("");
            }
        });
        add(textField1);
        add(textField2);

        //back button
        //JButton btnGoBack = new JButton("Go Back");
        //btnGoBack.setBounds(12, 213, 97, 25);
        //add(btnGoBack);
        //btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 450, 300);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new TeacherFrame(frame, teacher.getUsername()));
                break;
            case "Search":
                if (regNoTextField.getText().length() != 0){
                    if (teacher.searchByStudent(regNoTextField.getText(), false)){
                        setVisible(false);
                        frame.setContentPane(new DisplayStudent(frame, teacher, regNoTextField.getText(), false));
                    }
                }
                else if (usernameTextField.getText().length() != 0){
                    if (teacher.searchByStudent(usernameTextField.getText(), true)){
                        setVisible(false);
                        frame.setContentPane(new DisplayStudent(frame, teacher, usernameTextField.getText(), true));
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Please enter a username or registration number",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                }
                break;
            default:
                break;
        }
    }
}
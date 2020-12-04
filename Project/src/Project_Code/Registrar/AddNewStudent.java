package Project_Code.Registrar;

import Project_Code.DBController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AddNewStudent extends JPanel implements ActionListener {
    private RegistrarMain registrar;
    private final JFrame frame;
    private final DBController con = new DBController("team037","ee143bc0");

    private final JComboBox<String> comboBox_3;
    private final JComboBox<String> comboBox_4;
    private JComboBox<String> comboBox;
    private String degreeCode,title,surname,forename,tutor,level,startDate,endDate,password;
    private final JTextField textField;
    private final JTextField textField_1;
    private final JTextField textField_2;
    private final JTextField textField_3;
    private final JTextField textField_4;
    private final JPasswordField passwordField;


    public AddNewStudent(JFrame f, RegistrarMain reg) {
        registrar = reg;
        setLayout(null);

        //surname label
        JLabel label_3 = new JLabel("Surname:");
        label_3.setBounds(12, 120, 168, 25);
        add(label_3);

        //register student button
        JButton button_6 = new JButton("Register Student");
        button_6.setBounds(88, 427, 199, 55);
        add(button_6);
        button_6.addActionListener(this);

        //forename label
        JLabel label = new JLabel("Forename:");
        label.setBounds(12, 82, 86, 25);
        add(label);

        //degree label
        JLabel label_2 = new JLabel("Degree");
        label_2.setBounds(12, 158, 168, 25);
        add(label_2);

        //degree combo Box
        comboBox_3 = new JComboBox<>();
        comboBox_3.setBounds(211, 156, 116, 22);
        comboBox_3.addItem("");
        ResultSet result;
        try {
            result=con.performQuery("SELECT * FROM Degree"); //get the degree codes from the database
            while (result.next())
                comboBox_3.addItem(result.getString(1));
            //close connection and statement
            con.closeConnection();
            con.closeStatement();
        } catch (SQLException e) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }
        //each time the degree is selected it adds the levels appropriate for that degree

        comboBox_3.addActionListener(event -> {
            ResultSet result2;
            String degreeCode = Objects.requireNonNull(comboBox_3.getSelectedItem()).toString();
            if (!degreeCode.equals("")) {
                comboBox.removeAllItems();
                comboBox.addItem("");
                try {
                    result2=con.performQuery("SELECT levelCode FROM LevelApproval WHERE degreeCode ='"+degreeCode+"' ");
                    while (result2.next()) {
                        comboBox.addItem(result2.getString(1));
                    }
                    //close connection and statement
                    con.closeConnection();
                    con.closeStatement();
                } catch (SQLException e) {
                    //display error message and leave the application
                    JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    System.exit(0);
                }
            }
        });
        add(comboBox_3);

        //go back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(470, 13, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        //title label
        JLabel label_4 = new JLabel("Title:");
        label_4.setBounds(12, 37, 168, 25);
        add(label_4);

        //title combo box
        comboBox_4 = new JComboBox<>();
        comboBox_4.setBounds(211, 34, 116, 22);
        comboBox_4.addItem("");
        comboBox_4.addItem("Mr");
        comboBox_4.addItem("Mrs");
        comboBox_4.addItem("Miss");
        add(comboBox_4);

        //forename text field
        textField = new JTextField();
        textField.setBounds(211, 83, 116, 22);
        add(textField);
        textField.setColumns(10);

        //surname text field
        textField_1 = new JTextField();
        textField_1.setBounds(211, 121, 116, 22);
        add(textField_1);
        textField_1.setColumns(10);

        //tutor label
        JLabel label_6 = new JLabel("Tutor:");
        label_6.setBounds(12, 196, 168, 25);
        add(label_6);

        //tutor text field
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(211, 197, 116, 22);
        add(textField_2);

        //level label
        JLabel label_1 = new JLabel("Level:");
        label_1.setBounds(12, 236, 168, 25);
        add(label_1);

        //level combo box
        comboBox = new JComboBox<>();
        comboBox.setBounds(211, 234, 116, 22);
        add(comboBox);
        comboBox.addItem("");
        comboBox.addItem("1");
        comboBox.addItem("2");
        comboBox.addItem("3");
        comboBox.addItem("4");
        comboBox.addItem("P");

        //start date label
        JLabel label_7 = new JLabel("Start Date of Period:");
        label_7.setBounds(12, 271, 168, 25);
        add(label_7);

        //end date label
        JLabel label_8 = new JLabel("End Date of Period:");
        label_8.setBounds(12, 304, 168, 25);
        add(label_8);

        //labels that state the format of the data
        JLabel lblNewLabel = new JLabel("format: dd/mm/yyyy");
        lblNewLabel.setBounds(410, 273, 138, 20);
        add(lblNewLabel);

        JLabel label_9 = new JLabel("format: dd/mm/yyyy");
        label_9.setBounds(410, 306, 138, 20);
        add(label_9);

        //start date text-field
        textField_3 = new JTextField();
        textField_3.setBounds(211, 272, 116, 22);
        add(textField_3);
        textField_3.setColumns(10);

        //end date text field
        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(211, 305, 116, 22);
        add(textField_4);

        //password label
        JLabel label_5 = new JLabel("Password:");
        label_5.setBounds(12, 342, 117, 19);
        add(label_5);

        //password field
        passwordField = new JPasswordField();
        passwordField.setBounds(211, 340, 116, 22);
        add(passwordField);

        //frame configurations
        frame=f;
        frame.setBounds(100, 100, 650, 550);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new RegistrarFrame(frame, registrar.getUsername()));
                break;
            case "Register Student":
                processInfo(); //get the information from the form
                try {
                    //call the boolean method of adding a new student and if it is true display the message
                    if (registrar.addNewStudent( title, surname, forename,
                             level, degreeCode, tutor, startDate, endDate, password)) {
                        JOptionPane.showMessageDialog(null,"Student Registered Successfully");
                        setVisible(false);
                        frame.setContentPane(new RegistrarFrame(frame, registrar.getUsername()));
                    }
                    //close connection and statement
                    con.closeConnection();
                    con.closeStatement();
                } catch (HeadlessException | SQLException e1) {
                    //display error message and leave the application
                    JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    System.exit(0);
                }
                break;
        }
    }


    public void processInfo() {
        level = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
        startDate= textField_3.getText();
        endDate= textField_4.getText();
        degreeCode= Objects.requireNonNull(comboBox_3.getSelectedItem()).toString();
        title= Objects.requireNonNull(comboBox_4.getSelectedItem()).toString();
        surname=textField_1.getText().replaceAll("\\p{Punct}", "").replaceAll("\\s","");//replace punctuation and spaces
        forename=textField.getText().replaceAll("\\p{Punct}", "").replaceAll("\\s","");
        tutor=textField_2.getText().replaceAll("\\p{Punct}", "");//the tutor has spaces
        password = new String (passwordField.getPassword());
    }
}

package Project_Code.Registrar;

import Project_Code.DBController;
import java.awt.HeadlessException;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.swing.*;

/**
 * RemoveRegStudent
 * removes a student from the databse
 */

public class RemoveRegStudent extends JPanel implements ActionListener{
    private JFrame frame;
    private RegistrarMain registrar;
    private JComboBox<String> comboBox;

    private DBController con = new DBController( "team037", "ee143bc0");
    private String degreeCode,periodLabel,levelCode,regNo,forename,surname, username;
    private JLabel lblForename,lblSurname,lblDegreeCode,lblPeriodLabel,label,label_1,label_2,label_4;
    private JLabel lblLevelCode;
    private JLabel label_5;

    public RemoveRegStudent(JFrame f, RegistrarMain reg) {
        //bind the registrar object
        registrar = reg;

        setLayout(null);
        JLabel label_3 = new JLabel("Select Registered Student:");
        label_3.setBounds(12, 32, 179, 25);
        add(label_3);

        JButton button_6 = new JButton("Remove Registered Student");
        button_6.setBounds(136, 242, 199, 55);
        add(button_6);
        button_6.addActionListener(this);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 313, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        comboBox = new JComboBox<String>();
        comboBox.setBounds(213, 34, 179, 22);
        comboBox.addItem("");
        ResultSet result;
        try {
            result= con.performQuery("SELECT registrationNo FROM Student");
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
        //when a registration number is selected
        //the panel sets up the components and shows them
        comboBox.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent event) {
                regNo=comboBox.getSelectedItem().toString();
                if (!regNo.equals("")) { //if the registration number is not blank
                    setUpComponents();
                    viewComponents(true);
                }
                else {
                    viewComponents(false); //else hide the components
                }
            }
        });
        add(comboBox);

        lblForename = new JLabel("Forename:");
        lblForename.setBounds(42, 88, 84, 16);
        add(lblForename);
        lblForename.setVisible(false);

        lblSurname = new JLabel("Surname:");
        lblSurname.setBounds(42, 117, 84, 16);
        add(lblSurname);
        lblSurname.setVisible(false);

        lblDegreeCode = new JLabel("Degree Code:");
        lblDegreeCode.setBounds(42, 146, 84, 16);
        add(lblDegreeCode);
        lblDegreeCode.setVisible(false);

        lblPeriodLabel = new JLabel("Period Label:");
        lblPeriodLabel.setBounds(42, 175, 84, 16);
        add(lblPeriodLabel);
        lblPeriodLabel.setVisible(false);

        //label for forename displayed after a registration number is selected
        label = new JLabel("");
        label.setBounds(169, 88, 121, 16);
        add(label);
        label.setVisible(false);

        //label for surname displayed after a registration number is selected
        label_1 = new JLabel("");
        label_1.setBounds(169, 117, 121, 16);
        add(label_1);
        label_1.setVisible(false);

        //label for degree code displayed after a registration number is selected
        label_2 = new JLabel("");
        label_2.setBounds(169, 146, 121, 16);
        add(label_2);
        label_2.setVisible(false);

        //label for period label displayed after a registration number is selected
        label_4 = new JLabel("");
        label_4.setBounds(169, 175, 121, 16);
        add(label_4);
        label_4.setVisible(false);

        lblLevelCode = new JLabel("Level Code:");
        lblLevelCode.setBounds(42, 204, 84, 16);
        add(lblLevelCode);
        lblLevelCode.setVisible(false);

        //label for levelCode displayed after a registration number is selected
        label_5 = new JLabel("");
        label_5.setBounds(169, 204, 121, 16);
        add(label_5);
        label_5.setVisible(false);


        frame=f;
        frame.setBounds(100, 100, 450, 410);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new RegistrarFrame(frame, registrar.getUsername()));
                break;
            case "Remove Registered Student":
                String regNo= Objects.requireNonNull(comboBox.getSelectedItem()).toString();
                try {
                    if (registrar.removeStudent(regNo)) {
                        JOptionPane.showMessageDialog(null,"Successfully deleted registered student "+regNo);
                        setVisible(false);
                        frame.setContentPane(new RegistrarFrame(frame, registrar.getUsername()));
                    }
                    //close connection and statement
                    con.closeConnection();
                    con.closeStatement();
                } catch (NumberFormatException | HeadlessException | SQLException e1) {
                    //display error message and leave the application
                    JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    System.exit(0);
                }
                break;
        }

    }

    /**
     * Finds the students details using the registration number selected from the database
     * @param regNo registration number selected
     * @throws SQLException sql exception
     */
    public void findStudentDetails(String regNo) throws SQLException {
        ResultSet result2;
        String username = null;
        ResultSet res = con.performQuery("SELECT * FROM Student WHERE registrationNo='"+Integer.parseInt(regNo)+"'");

        while (res.next()) {
            username=res.getString(3);
            degreeCode=res.getString(2);
        }
        result2=con.performQuery("SELECT * FROM Users WHERE username='"+username+"'");
        while (result2.next()) {
            forename=result2.getString(3);
            surname=result2.getString(4);
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();

        ResultSet result3;
        result3 = con.performQuery("SELECT periodLabel,levelCode FROM Period WHERE registrationNo='"+regNo+"' ORDER BY periodLabel DESC LIMIT 1");
        while (result3.next()) {
            periodLabel=result3.getString(1);//gets the latest period label
            levelCode=result3.getString(2);//gets the latest level code
        }
        //close connection and statement
        con.closeConnection();
        con.closeStatement();
    }

    /**
     * shows the components after a registration number is selected
     * hides them if the selection is blank
     * @param view true if the components are to be displayed false if otherwise
     */
    public void viewComponents(boolean view) {
        label.setVisible(view);
        label_4.setVisible(view);
        label_1.setVisible(view);
        label_2.setVisible(view);
        lblPeriodLabel.setVisible(view);
        lblForename.setVisible(view);
        lblSurname.setVisible(view);
        lblDegreeCode.setVisible(view);
        lblLevelCode.setVisible(view);
        label_5.setVisible(view);
    }

    /**
     * sets up the components
     */
    public void setUpComponents() {
        try {
            findStudentDetails(regNo);
            label.setText(forename);
            label_1.setText(surname);
            label_2.setText(degreeCode);
            label_4.setText(periodLabel);
            label_5.setText(levelCode);
            //close connection and statement
            con.closeConnection();
            con.closeStatement();
        }
        catch (SQLException e1) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

    }
}

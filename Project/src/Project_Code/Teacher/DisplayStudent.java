package Project_Code.Teacher;

import javax.naming.directory.SearchControls;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class DisplayStudent extends JPanel implements ActionListener {
    private JFrame frame;
    private TeacherMain teacher;
    private JComboBox periodsList;
    private JComboBox modulesList;
    private String regNo;
    private JTextField initGrade;;
    private JTextField resitGrade;

    /**
     * Parameterized constructor
     * Creates the frame containing the search result for the student
     * @param f - the frame created
     * @param teacher - object of type Teacher
     */
    public DisplayStudent(JFrame f, TeacherMain teacher, String value, boolean username) {
        //bind the teacher object
        this.teacher = teacher;

        setBounds(87, 13, 300, 600);
        setLayout(null);

        JLabel label_1 = new JLabel("Student information");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setBounds(0, 0, 300, 25);
        add(label_1);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 613, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        //update grades button
        JButton btnApply = new JButton("Apply");
        btnApply.setBounds(182, 613, 97, 25);
        add(btnApply);
        btnApply.addActionListener(this);

        String regNo;
        if (username){
            regNo = teacher.getRegNoFromUsername(value);
        }
        else{
            regNo = value;
        }
        this.regNo = regNo;

        List<String> studentPeriods = teacher.getStudentPeriods(regNo);

        JLabel label_2 = new JLabel("Period:");
        label_2.setBounds(12, 50, 100, 40);
        add(label_2);

        JLabel label_3 = new JLabel("First Name:");
        label_3.setBounds(12, 90, 100, 40);
        add(label_3);

        JLabel label_4 = new JLabel("Last Name:");
        label_4.setBounds(12, 130, 100, 40);
        add(label_4);

        JLabel label_5 = new JLabel("Reg number:");
        label_5.setBounds(12, 170, 100, 40);
        add(label_5);

        JLabel label_6 = new JLabel("Username:");
        label_6.setBounds(12, 210, 100, 40);
        add(label_6);

        JLabel label_7 = new JLabel("Degree:");
        label_7.setBounds(12, 250, 100, 40);
        add(label_7);

        JLabel label_8 = new JLabel("Level:");
        label_8.setBounds(12, 290, 100, 40);
        add(label_8);

        JLabel label_9 = new JLabel("Module:");
        label_9.setBounds(12, 370, 100, 40);
        add(label_9);

        JLabel label_10 = new JLabel("Initial Grade:");
        label_10.setBounds(12, 410, 100, 40);
        add(label_10);

        JLabel label_11 = new JLabel("Resit Grade:");
        label_11.setBounds(12, 450, 100, 40);
        add(label_11);

        JLabel label_12 = new JLabel("Mean Grade:");
        label_12.setBounds(12, 490, 100, 40);
        add(label_12);

        JLabel label_13 = new JLabel("Outcome:");
        label_13.setBounds(12, 530, 100, 40);
        add(label_13);

        JLabel label_14 = new JLabel("Degree Class:");
        label_14.setBounds(12, 570, 100, 40);
        add(label_14);

        periodsList = new JComboBox(studentPeriods.toArray());
        periodsList.setBounds(112, 60, 100, 26);
        periodsList.setSelectedIndex(0);
        periodsList.addItemListener(new ItemChangeListener());
        add(periodsList);

        List<String> studentModules = teacher.getStudentModules(regNo, String.valueOf(periodsList.getSelectedItem()));

        modulesList = new JComboBox(studentModules.toArray());
        modulesList.setBounds(112, 380, 100, 26);
        modulesList.setSelectedIndex(0);
        modulesList.addItemListener(new ItemChangeListener());
        add(modulesList);

        String[] studentDetails = teacher.getStudentDetails(regNo);
        String[] userDetails = teacher.getUserDetails(studentDetails[0]);

        JLabel label_firstName = new JLabel(userDetails[0]);
        label_firstName.setBounds(112, 90, 200, 40);
        add(label_firstName);

        JLabel label_lastName = new JLabel(userDetails[1]);
        label_lastName.setBounds(112, 130, 200, 40);
        add(label_lastName);

        JLabel label_regNumber = new JLabel(regNo);
        label_regNumber.setBounds(112,170, 200, 40);
        add(label_regNumber);

        JLabel label_username = new JLabel(studentDetails[0]);
        label_username.setBounds(112, 210, 200, 40);
        add(label_username);

        JLabel label_degree = new JLabel(studentDetails[1]);
        label_degree.setBounds(112, 250, 200, 40);
        add(label_degree);

        JLabel label_level = new JLabel(teacher.getLevel(regNo, String.valueOf(periodsList.getSelectedItem())));
        label_level.setBounds(112, 290, 200, 40);
        add(label_level);

        JLabel label_meanGrade = new JLabel(String.valueOf(teacher.getMeanGrade(regNo, String.valueOf(periodsList.getSelectedItem()))));
        label_meanGrade.setBounds(112, 490, 100, 40);
        add(label_meanGrade);

        JLabel label_outcome = new JLabel();
        label_outcome.setBounds(112, 490, 100, 40);
        add(label_outcome);

        JLabel label_degreeGrade = new JLabel();
        label_degreeGrade.setBounds(112, 490, 100, 40);
        add(label_degreeGrade);

        String[] studentGrades = teacher.getGrades(regNo, String.valueOf(modulesList.getSelectedItem()), String.valueOf(periodsList.getSelectedItem()));
        JTextField label_initialGrade = new JTextField(studentGrades[0]);
        label_initialGrade.setBounds(112, 410, 50, 40);
        add(label_initialGrade);
        initGrade = label_initialGrade;

        JTextField label_resitGrade = new JTextField(studentGrades[1]);
        label_resitGrade.setBounds(112, 450, 50, 40);
        add(label_resitGrade);
        resitGrade = label_resitGrade;

        label_initialGrade.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (label_initialGrade.getText().length() >= 5 ) // limit textfield to 9 characters
                    e.consume();
            }
        });
        label_resitGrade.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (label_resitGrade.getText().length() >= 5 ) // limit textfield to 9 characters
                    e.consume();
            }
        });

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 300, 700);
        frame.setLocationRelativeTo(null);
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    class ItemChangeListener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Object item = event.getItem();
                if (String.valueOf(item).length() == 1){
                    //Period changed
                    List<String> studentModules = teacher.getStudentModules(regNo, String.valueOf(item));
                    modulesList.removeAllItems();
                    for(String s:studentModules){
                        modulesList.addItem(s);
                    }
                }
                else{
                    //Module changed
                    String[] studentGrades = teacher.getGrades(regNo, String.valueOf(modulesList.getSelectedItem()), String.valueOf(periodsList.getSelectedItem()));
                    initGrade.setText(studentGrades[0]);
                    resitGrade.setText(studentGrades[1]);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new SearchByStudent(frame, teacher));
                break;
            case "Apply":
                if (isNumeric(initGrade.getText()) && isNumeric(resitGrade.getText())){
                    if (Double.parseDouble(initGrade.getText()) <= 100 && Double.parseDouble(resitGrade.getText()) <= 100) {
                        teacher.updateGrades(regNo, String.valueOf(modulesList.getSelectedItem()), String.valueOf(periodsList.getSelectedItem()), initGrade.getText(), resitGrade.getText());
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Please enter valid grades",
                                "ERROR", JOptionPane.ERROR_MESSAGE, null);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Please enter valid grades",
                            "ERROR", JOptionPane.ERROR_MESSAGE, null);
                }
        }
    }
}
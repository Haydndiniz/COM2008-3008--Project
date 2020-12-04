package Project_Code.Teacher;

import Project_Code.Admin.ViewUsers;
import Project_Code.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherFrame extends JPanel implements ActionListener {
    private JFrame frame;
    private TeacherMain teacher;

    /**
     * Constructor
     * Creates the frame containing all menu items for teacher
     * @param f        - the frame created
     * @param username - username of the Teacher user
     */
    public TeacherFrame(JFrame f, String username) {

        teacher = new TeacherMain(username);

        //create a 4 row 1 column grid
        setLayout(new GridLayout(4, 1, 0, 0));

        JLabel label = new JLabel("Manage & View Student Grades");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        JButton searchByDegreeButton = new JButton("Search by Degree");
        add(searchByDegreeButton);
        searchByDegreeButton.addActionListener(this);

        JButton searchByModuleButton = new JButton("Search by Module");
        add(searchByModuleButton);
        searchByModuleButton.addActionListener(this);

        JButton searchByStudentButton = new JButton("Search by Student");
        add(searchByStudentButton);
        searchByStudentButton.addActionListener(this);

        frame = f;
        frame.setTitle("University Information System: Teacher");
        frame.setContentPane(this);
        frame.setBounds(87, 13, 450, 300);
        frame.setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();

        //switch frame on button press
        switch (buttonPressed) {
            case "Search by Degree":
                setVisible(false);
                break;
            case "Search by Module":
                setVisible(false);
                break;
            case "Search by Student":
                setVisible(false);
                frame.setContentPane(new SearchByStudent(frame, teacher));
                break;
        }
    }
}

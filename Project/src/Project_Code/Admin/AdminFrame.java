package Project_Code.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;

    /**
     * Constructor
     * Creates the frame containing all menu items for admin
     * @param f        - the frame created
     * @param username - username of the Admin user
     */
    public AdminFrame(JFrame f, String username) {

        admin = new AdminMain(username);

        //create a 6 row 4 column grid
        setLayout(new GridLayout(6, 4, 0, 0));

        //add labels in first row
        JLabel label_3 = new JLabel("Manage Users:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_3);

        JLabel label = new JLabel("Manage Departments:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        JLabel label_1 = new JLabel("Manage Degree Courses:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_1);

        JLabel label_2 = new JLabel("Manage Modules:");
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        add(label_2);

        //Buttons in row 2
        JButton addAccountButton = new JButton("Add User");
        add(addAccountButton);
        addAccountButton.addActionListener(this);

        JButton addDepartmentButton = new JButton("Add Department");
        add(addDepartmentButton);
        addDepartmentButton.addActionListener(this);

        JButton addCourseButton = new JButton("Add Course");
        add(addCourseButton);
        addCourseButton.addActionListener(this);

        JButton addModuleButton = new JButton("Add Module");
        add(addModuleButton);
        addModuleButton.addActionListener(this);

        //Buttons in row 3
        JButton removeAccountButton = new JButton("Remove User");
        add(removeAccountButton);
        removeAccountButton.addActionListener(this);

        JButton removeDepartmentButton = new JButton("Remove Department");
        add(removeDepartmentButton);
        removeDepartmentButton.addActionListener(this);

        JButton removeCourseButton = new JButton("Remove Course");
        add(removeCourseButton);
        removeCourseButton.addActionListener(this);

        JButton btnAddModuleFor = new JButton("Remove Module");
        add(btnAddModuleFor);
        btnAddModuleFor.addActionListener(this);

        //Buttons in row 4
        JButton viewAccountsButton = new JButton("View Users");
        add(viewAccountsButton);
        viewAccountsButton.addActionListener(this);

        JButton viewDepartmentsButton = new JButton("View Departments");
        add(viewDepartmentsButton);
        viewDepartmentsButton.addActionListener(this);

        JButton viewCoursesButton = new JButton("View Courses");
        add(viewCoursesButton);
        viewCoursesButton.addActionListener(this);

        JButton removeModuleButton = new JButton("View Modules");
        add(removeModuleButton);
        removeModuleButton.addActionListener(this);

        //lines between sections
        JSeparator column0 = new JSeparator();
        column0.setOrientation(SwingConstants.VERTICAL);
        add(column0);

        JSeparator column1 = new JSeparator();
        column1.setOrientation(SwingConstants.VERTICAL);
        add(column1);

        //view degrees per department
        JButton btnViewCoursesPer = new JButton("View Courses per Department");
        add(btnViewCoursesPer);
        btnViewCoursesPer.addActionListener(this);

        //separators for columns
        JSeparator column2 = new JSeparator();
        column2.setOrientation(SwingConstants.VERTICAL);
        add(column2);

        JSeparator column3 = new JSeparator();
        column3.setOrientation(SwingConstants.VERTICAL);
        add(column3);

        JSeparator column4 = new JSeparator();
        column4.setOrientation(SwingConstants.VERTICAL);
        add(column4);

        setBounds(87, 13, 1000, 521);

        frame = f;
        frame.setTitle("University Information System: Admin");
        frame.setContentPane(this);
        frame.setBounds(87, 13, 800, 480);
        frame.setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();

        //switch frame on button press
        switch (buttonPressed) {
            case "View Users":
                setVisible(false);
                frame.setContentPane(new ViewUsers(frame, admin));
                break;
            case "Add User":
                setVisible(false);
                frame.setContentPane(new AddUser(frame, admin));
                break;
            case "Remove User":
                setVisible(false);
                frame.setContentPane(new RemoveUser(frame, admin));
                break;
            case "Add Department":
                setVisible(false);
                frame.setContentPane(new AddDepartment(frame, admin));
                break;
            case "Remove Department":
                setVisible(false);
                frame.setContentPane(new RemoveDepartment(frame, admin));
                break;
            case "View Departments":
                setVisible(false);
                frame.setContentPane(new ViewDepartments(frame, admin));
                break;
            case "Add Course":
                setVisible(false);
                frame.setContentPane(new AddCourse(frame, admin));
                break;
            case "Remove Course":
                setVisible(false);
                frame.setContentPane(new RemoveCourse(frame, admin));
                break;
            case "View Courses":
                setVisible(false);
                frame.setContentPane(new ViewCourses(frame, admin));
                break;
            case "View Courses per Department":
                setVisible(false);
                frame.setContentPane(new ViewCoursesPerDepartment(frame, admin));
                break;
            case "Add Module":
                setVisible(false);
                frame.setContentPane(new AddModule(frame, admin));
                break;
            case "Remove Module":
                setVisible(false);
                frame.setContentPane(new RemoveModule(frame, admin));
                break;
            case "View Modules":
                setVisible(false);
                frame.setContentPane(new ViewModules(frame, admin));
                break;
        }
    }
}

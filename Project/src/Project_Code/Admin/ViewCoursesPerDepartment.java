package Project_Code.Admin;


import javax.swing.*;
import java.awt.event.*;

public class ViewCoursesPerDepartment extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;
    private JTable table;

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */
    public ViewCoursesPerDepartment(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Courses per Department Table:");
        label.setBounds(27, 22, 250, 25);
        add(label);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 415, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        //table with all Departments
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(15, 75, 461, 273);
        add(scrollPane);

        table = new JTable();
        table.setEnabled(false);
        table.setModel(this.admin.viewCoursesPerDepartment());
        scrollPane.setViewportView(table);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 491, 520);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));
                break;
        }
    }
}
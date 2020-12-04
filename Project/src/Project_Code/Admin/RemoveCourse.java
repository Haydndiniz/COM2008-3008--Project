package Project_Code.Admin;


import javax.swing.*;
import java.awt.event.*;

public class RemoveCourse extends JPanel implements ActionListener {
    private JFrame frame;
    private AdminMain admin;

    /**
     * Parameterized constructor
     * Creates the frame containing table of users
     * @param f - the frame created
     * @param admin - object of type Administrator
     */

    public RemoveCourse(JFrame f, AdminMain admin) {
        //bind the admin object
        this.admin = admin;

        setBounds(87, 13, 491, 453);
        setLayout(null);

        JLabel label = new JLabel("Remove Course:");
        label.setBounds(27, 22, 134, 25);
        add(label);

        // Course label
        JLabel roleLabel = new JLabel("Course Code:");
        roleLabel.setBounds(57, 87, 134, 25);
        add(roleLabel);

        // courseField
        JTextField forenameField = new JTextField(20);
        forenameField.setBounds(211, 87, 132, 25);
        add(forenameField);

        //Add button
        JButton btnRemoveUsr = new JButton("Remove");
        btnRemoveUsr.setBounds(291, 152, 97, 25);
        add(btnRemoveUsr);
        btnRemoveUsr.addActionListener(this);

        //back button
        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(12, 152, 97, 25);
        add(btnGoBack);
        btnGoBack.addActionListener(this);

        frame=f;
        frame.setContentPane(this);
        frame.setBounds(87, 13, 400, 257);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Go Back":
                setVisible(false);
                frame.setContentPane(new AdminFrame(frame, admin.getUsername()));
            case "Remove":
                //setVisible(false);
        }
    }
}
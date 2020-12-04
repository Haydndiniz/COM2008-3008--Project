package Project_Code.Registrar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrarFrame extends JPanel implements ActionListener {
    private JFrame frame;
    private RegistrarMain registrar;

    /**
     * Constructor
     * @param f the frame that the panel is added
     * @param username the username
     */
    public RegistrarFrame(JFrame f, String username) {

        registrar = new RegistrarMain(username);

        setLayout(null);

        JLabel label_3 = new JLabel("Registration:");
        label_3.setBounds(35, 13, 134, 25);
        add(label_3);

        JButton btnNewButton = new JButton("Register New Student");
        btnNewButton.setBounds(35, 51, 178, 40);
        add(btnNewButton);
        btnNewButton.addActionListener(this);

        JButton button = new JButton("Remove Student");
        button.setBounds(35, 121, 178, 40);
        add(button);
        button.addActionListener(this);

        JButton button_6 = new JButton("View Registered Students");
        button_6.setBounds(324, 121, 191, 40);
        add(button_6);
        button_6.addActionListener(this);

        JButton button_1 = new JButton("Add/Drop Optional Module");
        button_1.setBounds(324, 51, 191, 40);
        add(button_1);
        button_1.addActionListener(this);

        JLabel label = new JLabel("Manage Registered Students:");
        label.setBounds(324, 13, 204, 25);
        add(label);

        frame=f;
        frame.setBounds(100, 100, 560, 360);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        switch (buttonPressed) {
            case "Register New Student":
                setVisible(false);
                frame.setContentPane(new AddNewStudent(frame, registrar));
                break;
            case "Remove Student":
                setVisible(false);
                frame.setContentPane(new RemoveRegStudent(frame, registrar));
                break;
            case "Add/Drop Optional Module":
                setVisible(false);
                frame.setContentPane(new ManageModules(frame, registrar));
                break;
            case "View Registered Students":
                setVisible(false);
                frame.setContentPane(new ViewRegStudents(frame, registrar));
                break;
        }
    }
}

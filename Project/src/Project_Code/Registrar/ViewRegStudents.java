package Project_Code.Registrar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ViewRegStudents extends JPanel implements ActionListener {
    private JFrame frame;
    private RegistrarMain registrarMain;
    private JTable table,table1;
    private RegistrarMain registrar;

    public ViewRegStudents(JFrame f, RegistrarMain reg) {
        registrar = reg;

        setLayout(null);
        JLabel label_3 = new JLabel("Registered Students:");
        label_3.setBounds(12, 13, 134, 25);
        add(label_3);

        JButton button = new JButton("Go Back");
        button.setBounds(22, 614, 97, 25);
        add(button);
        button.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(111, 40, 950, 289);
        add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        table.setEnabled(false);
        try {
            table.setModel(reg.viewAllStudents());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(111, 370, 950, 231);
        add(scrollPane_1);

        table1 = new JTable();
        scrollPane_1.setViewportView(table1);
        table1.setEnabled(false);
        try {
            table1.setModel(reg.viewCurrentPeriodStatus());
        } catch (SQLException e) {
            //display error message and leave the application
            JOptionPane.showMessageDialog(null,"There was an error when processing the data.",
                    "ERROR", JOptionPane.ERROR_MESSAGE, null);
            System.exit(0);
        }

        table1.getColumnModel().getColumn(2).setPreferredWidth(517);

        JLabel label = new JLabel("Current period status:");
        label.setBounds(12, 342, 186, 25);
        add(label);


        frame=f;
        frame.setBounds(100, 100, 1170, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed=e.getActionCommand();
        if(buttonPressed=="Go Back"){
            setVisible(false);
            frame.setContentPane(new RegistrarFrame(frame, registrar.getUsername()));
        }

    }


}

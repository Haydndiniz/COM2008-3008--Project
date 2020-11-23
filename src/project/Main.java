package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Main extends JFrame implements ActionListener {


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmLogout = new JMenuItem("Log out");
        mnFile.add(mntmLogout);
        mntmLogout.addActionListener(this);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mnFile.add(mntmExit);
        mntmExit.addActionListener(this);

//        JPanel loginPane = new Login(this, mntmLogout);

        //jframe settings
        setTitle("University Information System");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
//        setContentPane(loginPane);
        setLocationRelativeTo(null);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();
        switch (buttonPressed) {
            case "Log out":
                dispose();
                new Main().setVisible(true);
                break;
            case "Exit":
                System.exit(0);
                break;

        }
    }
}

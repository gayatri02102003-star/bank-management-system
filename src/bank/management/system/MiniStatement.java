package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MiniStatement extends JFrame implements ActionListener{

    String pinnumber;
    JButton back;

    MiniStatement(String pinnumber){
        this.pinnumber = pinnumber;
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 900, 900);
        add(image);
        image.setLayout(null);

        JLabel bank = new JLabel("Indian Bank");
        bank.setBounds(355, 275, 100, 20);
        bank.setForeground(Color.WHITE);
        bank.setFont(new Font("System", Font.BOLD, 12));
        image.add(bank);

        JLabel card = new JLabel();
        card.setBounds(250, 295, 400, 20);
        card.setForeground(Color.WHITE);
        card.setFont(new Font("System", Font.BOLD, 10));
        image.add(card);

        JLabel mini = new JLabel();
        mini.setBounds(250, 320, 400, 140);
        mini.setForeground(Color.WHITE);
        mini.setFont(new Font("System", Font.PLAIN, 9));
        image.add(mini);

        JLabel balLabel = new JLabel();
        balLabel.setBounds(250, 470, 400, 20);
        balLabel.setForeground(Color.WHITE);
        balLabel.setFont(new Font("System", Font.BOLD, 11));
        image.add(balLabel);

        back = new JButton("Back");
        back.setBounds(610, 490, 110, 26);
        back.addActionListener(this);
        image.add(back);

        try{
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("select * from login where pin = '"+pinnumber+"'");
            while(rs.next()){
                card.setText("Card: " + rs.getString("cardnumber").substring(0,4) + "XXXXXXXX" + rs.getString("cardnumber").substring(12));
            }
        } catch(Exception e){}

        try{
            Conn conn = new Conn();
            int bal = 0;
            ResultSet rs = conn.s.executeQuery("select * from bank where pin = '"+pinnumber+"'");
            String trans = "<html>";
            int count = 0;
            while(rs.next()){
                if(count < 5){
                    trans += rs.getString("date").substring(0,16) + " " + rs.getString("type") + " " + rs.getString("amount") + "<br>";
                }
                if(rs.getString("type").equals("Deposit")){
                    bal += Integer.parseInt(rs.getString("amount"));
                } else {
                    bal -= Integer.parseInt(rs.getString("amount"));
                }
                count++;
            }
            trans += "</html>";
            if(count==0){
                mini.setText("<html>No Transactions Yet<br>Please Deposit First</html>");
            } else {
                mini.setText(trans);
            }
            balLabel.setText("Total Balance is Rs " + bal);

        } catch(Exception e){
            System.out.println(e);
        }

        setSize(900, 900);
        setLocation(300, 0);
        setUndecorated(true);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae){
        setVisible(false);
        new Transactions(pinnumber).setVisible(true);
    }

    public static void main(String args[]){
        new MiniStatement("");
    }
}
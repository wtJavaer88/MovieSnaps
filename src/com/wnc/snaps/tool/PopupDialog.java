package com.wnc.snaps.tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopupDialog extends JDialog implements ActionListener
{

    public PopupDialog(String title, String text)
    {// Constructo
        super(new Frame(""), title, true);// Create a framer
        setSize(300, 140);
        setResizable(false);
        setLocation(240, 240);

        JLabel message = new JLabel(text, JLabel.CENTER);// Messenger
        message.setFont(new Font("微软雅黑", Font.BOLD, 15));
        message.setForeground(new Color(0x206620));
        getContentPane().add(message, BorderLayout.CENTER);

        JButton close = new JButton("关闭");// close button
        close.setFont(new Font("微软雅黑", Font.BOLD, 15));
        close.setForeground(new Color(0xff0000));
        JPanel p = new JPanel();
        close.addActionListener(this);
        p.add(close);
        getContentPane().add(p, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        // TODO Auto-generated method stub
        this.dispose();
    }

}

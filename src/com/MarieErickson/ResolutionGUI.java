package com.MarieErickson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import java.util.LinkedList;

/**
 * Created by Marie on 11/8/2016.
 */
public class ResolutionGUI extends JFrame{
    private JLabel lblDateName;
    private JLabel lblReporter;
    private JLabel lblPriority;
    private JLabel lblIssue;
    private JTextField txtResolution;
    private JButton resolveButton;
    private JPanel rootPanel1;

    protected ResolutionGUI(Ticket ticket, LinkedList<Ticket> resolved) {
        setContentPane(rootPanel1);
        addListeners(ticket, resolved);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(resolveButton);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //get data from ticket to display on the new GUI
        Date GUIDate = ticket.getDateReported();
        DateFormat df = new SimpleDateFormat("EEE MMM dd");
        String date = df.format(GUIDate);
        lblDateName.setText(date);
        lblIssue.setText(ticket.getDescription());
        lblPriority.setText(Integer.toString(ticket.getPriority()));
        lblReporter.setText(ticket.getReporter());



    }
    private void addListeners(Ticket ticket, LinkedList<Ticket> resolved){
        //reslove button click event handler
        resolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //adds resolution and resolution date to ticket
                ticket.setResolutionDate(new Date());
                ticket.setResolution(txtResolution.getText());
                //writes resolved ticket to file
                writeResolved(resolved);

                dispose();
            }
        });
    }
    //code to write resolved tickets to a text file
    private static void writeResolved(LinkedList<Ticket> resolvedTickets){
        //get variables to generate a file name with current date parts
        Date date = new Date();
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        String day = sdfDay.format(date);
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM");
        String month = sdfMonth.format(date);
        SimpleDateFormat sdfYear = new SimpleDateFormat("YYYY");
        String year= sdfYear.format(date);
        //generated file name
        String fileName = "Resolved_tickets_as_of_"+month+"_"+day+"_"+year+".txt";
        //appends
        try (BufferedWriter bufWrite = new BufferedWriter(new FileWriter(fileName, true))) {
            Ticket t = resolvedTickets.getLast();
            String writeFileLine = t.toString("resolved");
            bufWrite.write(writeFileLine);
        }
        catch(IOException ex){
            System.out.println("An IO Exception occured");
        }

    }

}
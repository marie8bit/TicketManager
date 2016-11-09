package com.MarieErickson;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

/**
 * Created by yd7581ku on 11/1/2016.
 */
public class NewTicketGUI extends JFrame
{
    private JLabel lblDate;
    private JTextField txtReporter;
    private JTextField txtProblem;
    private JComboBox comboPriority;
    private JList stJList;
    private JButton btnAddNewT;
    private JPanel rootPanel;
    private JLabel lblEdit;
    private JButton resolveTicketButton;
    private DefaultListModel<Ticket>listModel;

    protected NewTicketGUI(LinkedList<Ticket> resolved, LinkedList<Ticket> queue){
        setContentPane(rootPanel);
        listModel = new DefaultListModel<Ticket>();
        stJList.setModel(listModel);
        LinkedList<Ticket> resolvedTickets = new LinkedList<>();
        addListeners(resolved,queue);
        addListeners(queue);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(btnAddNewT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Date GUIDate = new Date();
        DateFormat df = new SimpleDateFormat("EEE MMM dd");
        String date = df.format(GUIDate);
        lblDate.setText(date);

        if (!queue.isEmpty())
        {
            for (Ticket t : queue)
            {
                listModel.addElement(t);
                lblEdit.setText("Select a ticket to resolve, then click the resolve button");
            }
        }
        else{
            lblEdit.setText("No tickets in the queue");
        }
    }
    private void addListeners(LinkedList<Ticket> queue){
        btnAddNewT.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                Date tDate = new Date();
                Ticket tNew = new Ticket(txtProblem.getText(), comboPriority.getSelectedIndex()+1,
                        txtReporter.getText(), tDate);
                addTicketInPriorityOrder(queue, tNew);
                writeOpen(queue);
                listModel.clear();
                for (Ticket t : queue)
                {
                    listModel.addElement(t);

                }
                lblEdit.setText("Select a ticket to resolve, then click the resolve button");
            }
        });

    }
    private void addListeners(LinkedList<Ticket> resolved, LinkedList<Ticket> queue){
        stJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = stJList.getSelectedIndex();
            }
        });


        resolveTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTicketbyID(resolved, queue, stJList.getSelectedIndex());
                listModel.removeElementAt(stJList.getSelectedIndex());
                writeOpen(queue);
            }
        });
    }
    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }
    protected static void deleteTicketbyID(LinkedList<Ticket> resolvedTickets, LinkedList<Ticket> ticketQueue, int index) {
            Ticket ticket = ticketQueue.get(index);
                setResolutionAndDate(ticket, resolvedTickets);
                ticketQueue.remove(ticket);
                //add resolved tickets for file storage
                resolvedTickets.add(ticket);
    }
    private static void setResolutionAndDate(Ticket ticket, LinkedList<Ticket> resolved){
        //Scanner scanner= new Scanner(System.in);
        //System.out.println("Enter the reason why the ticket is being deleted");
        //String res = scanner.nextLine();
        //time stamp resolution
        ResolutionGUI resGUI = new ResolutionGUI(ticket, resolved);
        ticket.setResolutionDate(new Date());
     }
    private static void writeOpen(LinkedList<Ticket> ticketQueue){
        if (!ticketQueue.isEmpty()) {
            try {
                //set up writing to facilitate reading of the file to generate objects
                try (BufferedWriter bufWrite = new BufferedWriter(new FileWriter("openTickets.txt"))) {
                    for (Ticket t : ticketQueue) {
                        int id = t.getTicketID();
                        bufWrite.write(Integer.toString(id));
                        bufWrite.newLine();
                        bufWrite.write(t.getDescription());
                        bufWrite.newLine();
                        int p=t.getPriority();
                        bufWrite.write(Integer.toString(p));
                        bufWrite.newLine();
                        bufWrite.write(t.getReporter());
                        bufWrite.newLine();
                        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
                        String date = df.format(t.getDateReported());
                        bufWrite.write(date);
                        bufWrite.newLine();
                    }
                }
            } catch (IOException ex) {
                System.out.println("An IO Exception occured");
            }
        }
    }
}

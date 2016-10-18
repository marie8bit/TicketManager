package com.MarieErickson;
import java.util.*;

public class TicketManager {

    public static void main(String[] args) {

        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();


        Scanner scan = new Scanner(System.in);

        while(true){

            System.out.println("1. Enter Ticket\n2. Delete By ID \n3. Search by Name"+
                    "\n4. Delete by Issue\n5. Display All Tickets\n6. Quit");
            int task = getPositiveIntInput();

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                //delete a ticket
                deleteTicketbyID(ticketQueue);

            }  else if (task == 3) {
                //search description for keyword and display list
                searchTicketbyName(ticketQueue);

            }  else if (task == 4) {
                //delete a ticket
                LinkedList<Ticket> n3 =searchTicketbyName(ticketQueue);
                deleteTicketbyID(ticketQueue, n3);
            }  else if (task == 5){
                printAllTickets(ticketQueue);
            }  else if (task == 6) {
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                break;
            } else {
                //requests valid data from the user
                System.out.println("Please choose an option from the list");
            }

        }

        scan.close();

    }


    private static LinkedList<Ticket> searchTicketbyName(LinkedList<Ticket> ticketQueue) {
        LinkedList<Ticket> searchResults = new LinkedList<>();
        if (ticketQueue.isEmpty()){
            System.out.println("No open tickets to search");
            return searchResults;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the keyword(s) you would like to search for");
        String search = scanner.nextLine();
        for (Ticket t :ticketQueue) {
            if (t.getDescription().contains(search)) {
                searchResults.add(t);
            }
        }
        printAllTickets(searchResults);
        return searchResults;
    }

    protected static void deleteTicketbyID(LinkedList<Ticket> ticketQueue) {
        printAllTickets(ticketQueue);   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }


        System.out.println("Enter ID of ticket to delete");
        int deleteID = getPositiveIntInput();

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }

        if (found == false) {
            System.out.println("Ticket ID not found, no ticket deleted");
            System.out.println("Would you like to try again? Y for yes");
            Scanner sc = new Scanner(System.in);
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("y")) {
                deleteTicketbyID(ticketQueue);
            }
        }
        printAllTickets(ticketQueue);  //print updated list

    }

    protected static void deleteTicketbyID(LinkedList<Ticket> ticketQueue,
                                           LinkedList<Ticket> searchResults) {

        if (searchResults.size() == 0) {    //no tickets!
            System.out.println("No tickets found!\n");
            return;
        }


        System.out.println("Enter ID of ticket to delete");
        int deleteID = getPositiveIntInput();

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : searchResults) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }

        if (found == false) {
            System.out.println("Ticket ID not found, no ticket deleted");
            System.out.println("Would you like to try again? Y for yes");
            Scanner sc = new Scanner(System.in);
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("y")) {
                deleteTicketbyID(ticketQueue, searchResults);
            }
        }
        printAllTickets(ticketQueue);  //print updated list

    }


    //Move the adding ticket code to a method
    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
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
    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- List of Open Tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }
    //Validation methods

    private static int getPositiveIntInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String stringInput = scanner.nextLine();
                int intInput = Integer.parseInt(stringInput);
                if (intInput >= 0) {
                    return intInput;
                } else {
                    System.out.println("Please enter a vaild Ticket ID#");

                    continue;
                }
            } catch (NumberFormatException ime) {
                System.out.println("Please enter a valid Ticket ID#");
            }

        }

    }
}


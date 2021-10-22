import java.util.*;

/**
 * Creates a calendar with events annotated on it.
 * @author Zachary Mallol
 * @author Tanvir Arora
 * @author Vennela Madduri
 * @author Alex Ongstad
 */
public class CalendarMaker {
    
    /** The number of months in a year, represented as an integer */
    public static final int MONTHS_IN_YEAR = 12;
    
    /** Maximum year the calendar can support */
    public static final int MAX_YEAR = 4000;
    
    /** Minimum year the calendar can support */
    public static final int MIN_YEAR = 1800;
    
    /** An array of Month objects that can hold up to 12 months */
    public static Month[] months = new Month[MONTHS_IN_YEAR];
    
    /**
    * Executes the Calendar Maker code. Prompts user for variables
    * and then proceeds to print the calendar and its events
    * @param args command line arguments (not used).
    */
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        
        int year = -1;      //Used for the year while loop
        
        System.out.println();
        System.out.print("Welcome to the Calendar Maker program. You ");
        System.out.println("will choose a year between 1800 and 4000 to ");
        System.out.print("display an entire calendar of that year. ");
        System.out.println("You will also get an option to add events on ");
        System.out.print("specific days to be displayed under each month.");
        System.out.println(" Please input a year below.");
        
        //Gets user's year. Repeatedly asks for input
        //if the input is invalid.
        while(year == -1) {
            System.out.print("Input year (1800-4000): ");
            if(!scnr.hasNextInt()) {
                scnr.next();
                System.out.println("Invalid year.");
            }
            else {
                year = scnr.nextInt();
                if(year > MAX_YEAR || year < MIN_YEAR) {
                    System.out.println("Invalid year.");
                    year = -1;
                }
            }
        }
        
        for(int i = 0; i < MONTHS_IN_YEAR; i++) {
            months[i] = new Month(year, i + 1);
        }
        
        String option = "";     //if the user wants to add another event
        do {
            System.out.print("Would you like to add an event? (y/n) ");
            option = scnr.next();
            /*If the user no longer wants to add events,
            print the calendar.*/
            if(option.equalsIgnoreCase("n")){
                for(int i = 0; i < MONTHS_IN_YEAR; i++) {
                    System.out.println(months[i].toString());
                }
            }
            //The user wants to add an event
            else if(option.equalsIgnoreCase("y")){
                int month = -1;
                //Continuously prompts user for a valid month
                while (month == -1) {
                    System.out.print("Month (one or two digit format): ");
                    if (!scnr.hasNextInt()) {
                        scnr.next();
                        System.out.println("Invalid month.");
                    }
                    else {
                        month = scnr.nextInt();
                        if(month < 1 || month > MONTHS_IN_YEAR) {
                            System.out.println("Invalid month.");
                            month = -1;
                        }   
                    }
                }
                //Continuously prompts user for a valid day
                int day = -1;
                while (day == -1) {
                    System.out.print("Day (one or two digit format): ");
                    if (!scnr.hasNextInt()) {
                        scnr.next();
                        System.out.println("Invalid day.");
                    }
                    else {
                        day = scnr.nextInt();
                        scnr.nextLine();    //Needed to make description: work
                        if(!months[month - 1].isValidDay(day)) {
                            System.out.println("Invalid day.");
                            day = -1;
                        }
                    }
                }
                //Prompts user for a description
                System.out.print("Description: ");
                String description = scnr.nextLine();
                boolean a = months[month - 1].createEvent(day, description);
                if(a == false) {    
                    //If createEvent returns false, events for that day are maxed out
                    System.out.print("Failed to add event. ");
                    System.out.println("You can only add up to ten events per day!");
                }
            }
            else {
                System.out.println("Invalid option.");
            }
        } while (!option.equalsIgnoreCase("n")); 
    }
}
import java.util.Scanner;

/*
 * Ivan Thich
 * 11/30/2023
 * CSE 190 Z
 * Final Project - GPA Calculator
 */

public class main {

    /*
     * Class constants
     */

    // To keep track of class names each quarter
    private static String[] classes;
    // If user wants to calculate quarterly
    private static boolean wantQuarterly;
    // To keep track of credits of each class, each quarter
    private static int[] credits;
    // Is concurrent with numQuarts, stores number of classes in each index/quarter
    private static int[] numClassStore;
    // Number of quarters user inputted if chosen cumulative route
    private static int numQuarts;
    // Used in calculateCumGPA and calculateQuarterlyGPA
    private static int totalCreds;
    // To keep track of credits of each grade point of each class, each quarter
    private static double[] classGrade;
    // Used in calculateCumGPA and calculateQuarterlyGPA
    private static double totalGradePts;

    /*
     * LIMITS
     */
    private final static int MIN_QUARTER = 1;
    private final static int MAX_QUARTER = 4;
    private final static int MIN_CREDIT = 1;
    private final static int MAX_CREDIT = 5;
    private final static double MIN_GRADE = 0.0;
    private final static double MAX_GRADE = 4.0;

    /*
     * RUN PROGRAM HERE
     */
    public static void main(String[] args){
        Scanner user = new Scanner(System.in);
        
        startProgram(user);
    }

    /*
     * Starts and runs GPA Calculator
     * param@ user
     */
    public static void startProgram(Scanner user){
        double finalGPA = 0.0;
        boolean answeredProgram = askPrompt(user); 

        if(answeredProgram){
            quarterlyOrCumulative(user);
            // Quarterly Route
            if(wantQuarterly){ 
                totalGradePts = 0;
                totalCreds = 0;
                inputC(user, numClassStore[0], 1);
                finalGPA = Math.round(calculateQuarterlyGPA(numClassStore[0]) * 100.0) / 100.0;
                System.out.println("Quarterly GPA: " + finalGPA);
            }
            // Cumulative Route
            else{
                totalGradePts = 0;
                totalCreds = 0;
                for(int i = 0; i < numQuarts; i++){
                    inputC(user, numClassStore[i], i+1);
                }
                finalGPA = Math.round(calculateCumGPA(totalCreds, totalGradePts) * 100.0) / 100.0;
                System.out.println("Cumulative GPA: " + finalGPA);
            }
        }
    }
    
    /*
     * Gives user a prompt if it wants to start the program or not
     * param@ user
     * return@ answered
     */
    public static boolean askPrompt(Scanner user){
        String answer = "";
        boolean answeredPrompt = false;

        System.out.println("Welcome to the UW GPA Calculator!");
        System.out.println("This program will help you calculate");
        System.out.println("your GPA whether cumulative or quarterly.");

        while(!answeredPrompt){
            System.out.println("Would you like to try? (Y/N)");
            answer = user.next();
            user.nextLine();

            if(answer.equalsIgnoreCase("N")){
                System.out.println("See you later!");
                System.exit(0);
            }
            if(answer.equalsIgnoreCase("Y")){
                System.out.println("Great! Some things to go over:");
                System.out.println("Credit limit for a quarter is 18 credits max!");
                System.out.println("Each class has 1-5 credits, no more, no less");
                System.out.println("Grade standing for each class cant be negative or greater than 4.0!");
                System.out.println("-----------------------------------------------");
                answeredPrompt = true;
            }
        }

        return answeredPrompt;
    }

    /*
     * Asks user if they want to calculate quarterly/cumulative GPA
     * and asks for number of classes and if request cumulative, asks for number of quarters
     * param@ user
     */
    public static void quarterlyOrCumulative(Scanner user){
        String answer = "";
        boolean answeredQoC = false;
        wantQuarterly = false;
        
        while(!answeredQoC){
            System.out.println("Do you want to calculate Quarterly or Cumulative GPA?");
            answer = user.next();
            user.nextLine();

            // User chooses cumulative
            if(answer.equalsIgnoreCase("Cumulative")){
                askNumClassandQuart(user);
                answeredQoC = true;
            }
            // User chooses quarterly
            else if(answer.equalsIgnoreCase("Quarterly")){
                wantQuarterly = true;
                askNumClassandQuart(user);
                answeredQoC = true;
            }
            // User chooses something else
            else{
                System.out.println("Try again!");
            }
        }
    }

    /*
     * Asks user for number of classes and if requested cumulative, quarters
     * and stores them for the whole class to use
     * param@ user
     */
    public static void askNumClassandQuart(Scanner user){
        boolean quarterCheck = false;

        // If user just wants to calculate one quarter
        if(wantQuarterly){
            numQuarts = 1;
            numClassStore = new int[numQuarts];
    
            System.out.println("How many classes?: ");
            numClassStore[0] = user.nextInt();
            user.nextLine();
        }
        // If user just wants to calculate multiple quarters
        else if(!wantQuarterly){
            //For asking suer how many quarters they want to calculate
            while(!quarterCheck){
                System.out.println("How many quarters? (Enter between 2-4 Quarters, in int): ");
                numQuarts = user.nextInt();
                user.nextLine();
        
                if(numQuarts > MIN_QUARTER && numQuarts <= MAX_QUARTER){
                    quarterCheck = true;
                }
                else{
                    System.out.println("Try again!");
                }
            }
            
            // Initializes numClassStore from the numQuarts collected
            numClassStore = new int[numQuarts];
    
            // Starts asking how many classes for each quarter
            for(int i = 0; i < numQuarts; i++){
                System.out.println("Quarter " + (i+1) + ":");
                System.out.println("How many classes?: ");
                numClassStore[i] = user.nextInt();       
                user.nextLine();
            }
        }
        
    }

    /*
     * Utilizes numClass value and current quarter from user to 
     * start collecting classes, credits, and classGrades
     * param@ user, numClass, int i (current quarter)
     */
    public static void inputC(Scanner user, int numClassStore, int i){
        classes = new String[numClassStore];
        credits = new int[numClassStore];
        classGrade = new double[numClassStore];
        
        for(int j = 0; j < numClassStore; j++){
            boolean creditAnswer = false;
            boolean gradeAnswer = false;

            System.out.println("Quarter: " + i + " | " + "Class " + (j+1) + " (name of class):");
            classes[j] = user.next();
            user.nextLine();

            // If user tries to put more than 5 and less than 1 for this prompt, they will have to try again
            while(!creditAnswer){
                System.out.println("How many credits? (enter in int): ");

                // If user types an int value
                if(user.hasNextInt()){
                    credits[j] = user.nextInt();
                    if(credits[j] < MIN_CREDIT || credits[j] > MAX_CREDIT){
                        System.out.println("Try again!");
                    }else{
                        creditAnswer = true;
                        user.nextLine();
                    }
                }
                // If user types a double value
                else{
                    System.out.println("Use an int value. Try again!");
                    user.next();
                }
            }

            // If user tries to put more than a 4.0 and less than a 0.0 for this prompt, they will have to try again
            while(!gradeAnswer){
                System.out.println("What is your grade standing? (enter in double) (type 0.0 if CR, S/NS, or anything not a number): ");
                classGrade[j] = user.nextDouble();
                if(classGrade[j] < MIN_GRADE || classGrade[j] > MAX_GRADE){
                    System.out.println("Try again!");
                }else{
                    gradeAnswer = true;
                    user.nextLine();
                }
            }

        }
    }

    /*
     * Calculates and returns quarterly GPA
     * Also stores values in totalCreds and totalGradePts for calculateCumGPA
     * param@ numClassStore[int value]
     * returns@ quartGPA - quarterly GPA
     */
    public static double calculateQuarterlyGPA(int numClassStore){
        double quartGPA = 0.0;
        double totalCurrentQuart = 0.0;

        for(int i = 0; i < numClassStore; i++){
            totalCurrentQuart += credits[i] * classGrade[i];
            if(classGrade[i] != 0){
                // For calculateCumGPA later on if user chooses cumulative
                totalCreds += credits[i];
            }
        }
        
        // For calculateCumGPA later on if user chooses cumulative
        totalGradePts += totalCurrentQuart; 

        quartGPA = totalCurrentQuart/totalCreds;

        return quartGPA;
    }

    /*
     * Calculates and returns cumulative GPA 
     * returns@ cumGPA - cumulative GPA
     * param@ totalCreds, totalGradePts
     * return@ cumGPA
     */
    public static double calculateCumGPA(double totalCreds, double totalGradePts){
        double cumGPA = totalGradePts/totalCreds;

        return cumGPA;
    }

}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        double loanSize;
        double amountOfPayments;
        double interestRate;
        String answer;
        LocalDate startDate;
        StringBuilder sb = new StringBuilder();
        PrintWriter pw;
        LocalDate interestChangeDate;

        Scanner reader = new Scanner(System.in);
        System.out.println("Ar patys norite įvesti parametrus? Jeigu taip, parašykite Y");
        answer = reader.next();
//        if (answer.equals("Y")) {
            System.out.println("Įveskite pradžios datą: ");
            System.out.println("Formatas turi būti MM/dd/yyyy. MM - mėnesis, dd - diena, yyyy - metai");
            startDate = dateInput(reader.next());
            System.out.println("Įveskite pradinę sumą: ");
            loanSize = reader.nextDouble();
            System.out.println("Įveskite palūkanų normą: ");
            interestRate = reader.nextDouble();
            System.out.println("Įveskite įmokų skaičių: ");
            amountOfPayments = reader.nextDouble();

            pw = new PrintWriter(new File("UserInputGraph.csv")); // Third challenge part
            generateGraph(pw, sb, interestRate, amountOfPayments, loanSize, startDate, null);
//        }
            pw = new PrintWriter(new File("FirstPartGraph.csv")); // First challenge part
            startDate = LocalDate.of(2017, Month.APRIL, 15);
            generateGraph(pw, sb, 7, 26, 10000, startDate, null);

            pw = new PrintWriter(new File("SecondPartGraph.csv")); // Second Challenge part
            interestChangeDate = LocalDate.of(2017, Month.SEPTEMBER, 1);
            generateGraph(pw, sb, 7, 26, 10000, startDate, interestChangeDate);
    }

    public static void generateGraph(PrintWriter pw, StringBuilder sb, double interestRate, double amountOfPayments,
                                     double loanSize, LocalDate startDate, LocalDate middleDate) throws FileNotFoundException {
        double annuity;
        double interest = interestRate * 0.01 / 12;
        double interestPayment;
        double principalPayment;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        createCSVTemplate(pw, sb);

        annuity = (interest /(1-(Math.pow((1+interest),-(amountOfPayments)))))*loanSize;
        BigDecimal annuityTemp = new BigDecimal(annuity);

        annuityTemp = annuityTemp.setScale(2, BigDecimal.ROUND_DOWN);
        annuity = annuityTemp.doubleValue(); // Convert to double for further calculations

        for (int i = 1; i <= amountOfPayments; i++) {

            // Check if date when the interest rate changes is provided as a parameter
            if (middleDate != null) {
                if (startDate.compareTo(middleDate) > 0) {
                    interestRate = 9;
                    interest = interestRate * 0.01 / 12;
                    annuity = (interest /(1-(Math.pow((1+interest),-(amountOfPayments)))))*loanSize;
                    annuityTemp = annuityTemp.setScale(2, BigDecimal.ROUND_DOWN);
                    annuity = annuityTemp.doubleValue();
                }
            }

            interestPayment = roundUp(loanSize) * interest;
            interestPayment = roundUp(interestPayment);

            if (i == amountOfPayments) {
                annuity = loanSize + interestPayment; // For the last annuity payment because it has different size
            }

            principalPayment = annuity - interestPayment;
            principalPayment = roundUp(principalPayment);

            sb.append(i);
            sb.append(',');
            sb.append(startDate.format(formatter));
            sb.append(',');
            sb.append(roundUp(loanSize));
            sb.append(',');
            sb.append(roundUp(principalPayment));
            sb.append(',');
            sb.append(roundUp(interestPayment));
            sb.append(',');
            sb.append(roundUp(annuity));
            sb.append(',');
            sb.append((int)interestRate);
            sb.append(',');
            sb.append("\n");
            startDate = startDate.plusMonths(1);
            loanSize -= roundUp(annuity - ((roundUp(loanSize)) * interest));
        }
        pw.write(sb.toString());
        pw.close();
    }

    private static void createCSVTemplate(PrintWriter pw, StringBuilder sb) throws FileNotFoundException {
        sb.append("Payment #");
        sb.append(',');
        sb.append("Payment date");
        sb.append(',');
        sb.append("Remaining amount");
        sb.append(',');
        sb.append("Principal payment");
        sb.append(',');
        sb.append("interestRate payment");
        sb.append(',');
        sb.append("Total payment");
        sb.append(',');
        sb.append("interestRate rate");
        sb.append(',');
        sb.append("\n");
    }

    public static LocalDate dateInput(String userInput) {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);

        System.out.println(date);
        return date ;
    }

    private static double roundUp(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}

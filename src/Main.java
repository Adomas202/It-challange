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
        LocalDate startDate;
        StringBuilder sb = new StringBuilder();
        PrintWriter pw;

        Scanner reader = new Scanner(System.in);
//        System.out.println("Įveskite pradžios datą: ");
//        loanSize = reader.nextDouble();
//        System.out.println("Įveskite pradinę sumą: ");
//        interestRate = reader.nextDouble();
//        System.out.println("Įveskite palūkanų normą: ");
//        amountOfPayments = reader.nextDouble();
//        System.out.println("Įveskite, nuo kada pradėsite mokėti: ");
//        String date = reader.next();

        startDate = LocalDate.of(2017, Month.APRIL, 15);

        pw = new PrintWriter(new File("FirstGraph.csv"));
        generateGraph(pw, sb, 7, 26, 10000, startDate);

        pw = new PrintWriter(new File("SecondGraph.csv"));
        generateGraph(pw, sb, 7, 26, 10000, startDate);

    }

    public static void generateGraph(PrintWriter pw, StringBuilder sb, double interestRate, double amountOfPayments,
                                     double loanSize, LocalDate startDate) throws FileNotFoundException {
        double annuity;
        double interest = interestRate * 0.01 / 12;
        double interestPayment;
        double principalPayment;
        int i;
        LocalDate midleDate = LocalDate.of(2017, Month.NOVEMBER, 1);

        createCSVTemplate(pw, sb);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        annuity = (interest /(1-(Math.pow((1+interest),-(amountOfPayments)))))*loanSize;
        BigDecimal annuityTemp = new BigDecimal(annuity);
        annuityTemp = annuityTemp.setScale(2, BigDecimal.ROUND_DOWN);
        annuity = annuityTemp.doubleValue();
        for (i = 1; i <= amountOfPayments; i++) {

            if (startDate.compareTo(midleDate) > 0) {
                interestRate = 9;
                interest = interestRate * 0.01 / 12;
                annuity = (interest /(1-(Math.pow((1+interest),-(amountOfPayments)))))*loanSize;
                annuity = annuityTemp.doubleValue();
            }

            interestPayment = Math.round(loanSize * 100.0) / 100.0 * interest;
            interestPayment = Math.round(interestPayment * 100.0) / 100.0;

            if (i == amountOfPayments) {
                annuity = loanSize + interestPayment; // For the last annuity payment because it has different size
            }

            principalPayment = annuity - interestPayment;
            principalPayment = Math.round(principalPayment * 100.0) / 100.0;

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

    private static double roundUp(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}

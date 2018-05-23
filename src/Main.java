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

        double loanSize = 5000;
        double amountOfPayments = 24;
        double interestRate = 12;
        int i;
        double interestPayment;
        double principalPayment;
        LocalDate startDate = LocalDate.of(2017, Month.APRIL, 15);

        Scanner reader = new Scanner(System.in);
        System.out.println("Įveskite paskolos dydį: ");
        loanSize = reader.nextDouble();
        System.out.println("Įveskite metinių palūkanų dydį: ");
        interestRate = reader.nextDouble();
        System.out.println("Įveskite, per kiek mėnesių įmoka turi būti grąžinta: ");
        amountOfPayments = reader.nextDouble();
//        System.out.println("Įveskite, nuo kada pradėsite mokėti: ");
//        startDate = LocalDate.ofEpochDay(reader.nextInt());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        double annuity;
        double interest = interestRate * 0.01 / 12;

        PrintWriter pw = new PrintWriter(new File("graph.csv"));
        StringBuilder sb = new StringBuilder();
        createCSVTemplate(pw, sb);

        annuity = (interest /(1-(Math.pow((1+interest),-(amountOfPayments)))))*loanSize;
        BigDecimal annuityTemp = new BigDecimal(annuity);
        annuityTemp = annuityTemp.setScale(2, BigDecimal.ROUND_DOWN);
        annuity = annuityTemp.doubleValue();
        for (i = 1; i <= amountOfPayments; i++) {
            interestPayment = Math.round(loanSize * 100.0) / 100.0 * interest;
            interestPayment = Math.round(interestPayment * 100.0) / 100.0;

            if (i == amountOfPayments) {
                annuity = loanSize + interestPayment;
            }

            principalPayment = annuity - interestPayment;
            principalPayment = Math.round(principalPayment * 100.0) / 100.0;

            sb.append(i);
            sb.append(',');
            sb.append(startDate.format(formatter));
            sb.append(',');
            sb.append(Math.round(loanSize * 100.0) / 100.0);
            sb.append(',');
            sb.append(principalPayment);
            sb.append(',');
            sb.append(Math.round(interestPayment * 100.0) / 100.0);
            sb.append(',');
            sb.append(Math.round(annuity * 100.0) / 100.0);
            sb.append(',');
            sb.append((int)interestRate);
            sb.append(',');
            sb.append("\n");
            startDate = startDate.plusMonths(1);
            loanSize -= Math.round((annuity - ((Math.round(loanSize * 100.0) / 100.0) * interest)) * 100.0) / 100.0;
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

        System.out.println("done!");
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        double loanSize = 10000;
        double amountOfPayments = 26;
        double interestRate = 7;
        double interest = interestRate * 0.01 / 12;
        int i;
        double interestPayment;
        double principalPayment;
        DecimalFormat format2Places = new DecimalFormat("0.00");
        BigDecimal Big;

        LocalDate startDate = LocalDate.of(2016, Month.NOVEMBER, 15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        double annuity;

        PrintWriter pw = new PrintWriter(new File("graph.csv"));
        StringBuilder sb = new StringBuilder();
        createCSVTemplate(pw, sb);

//        annuity = (interest /(1-(Math.pow((1+interest),-(amountOfPayments)))))*loanSize;
        annuity = 415.63;
        for (i = 1; i <= amountOfPayments; i++) {
            if (i == --amountOfPayments) {

            }
            interestPayment = Math.round(loanSize * 100.0) / 100.0 * interest;
            interestPayment = Math.round(interestPayment * 100.0) / 100.0;
            principalPayment = annuity - interestPayment;
            principalPayment = Math.round(principalPayment * 100.0) / 100.0;
//            BigDecimal principalPayment =  new BigDecimal(annuity - interestPayment);
//            principalPayment = principalPayment.setScale(2, BigDecimal.ROUND_UP);
            sb.append(i);
            sb.append(',');
            sb.append(startDate.format(formatter));
            sb.append(',');
            sb.append(loanSize);
            sb.append(',');
            System.out.println(loanSize);
            sb.append(',');
            sb.append(Math.round(interestPayment * 100.0) / 100.0);
            sb.append(',');
            sb.append(Math.round(annuity * 100.0) / 100.0);
            sb.append(',');
            sb.append(interestRate);
            sb.append(',');
            sb.append("\n");
            startDate = startDate.plusMonths(1);
            loanSize -= Math.round((annuity - ((Math.round(loanSize * 100.0) / 100.0) * interest)) * 100.0) / 100.0;
        }

        pw.write(sb.toString());
        pw.close();

    }

    public static int roundDown(double number, double place) {
        double result = number / place;
        result = Math.floor(result);
        result *= place;
        return (int)result;
    }

    private static void countAnnuity(int amountOfPayments, PrintWriter pw, StringBuilder sb, LocalDate startDate) {

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

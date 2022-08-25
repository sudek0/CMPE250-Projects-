 import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class project5main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File(args[0]));
        PrintStream out = new PrintStream(new File(args[1]));

        String[] types = in.nextLine().split(" ");
        String[] minA = in.nextLine().split(" ");
        String[] minB = in.nextLine().split(" ");
        String[] profits = in.nextLine().split(" ");
        String[] arrivalTimes = in.nextLine().split(" ");

        ArrayList<Product> offers = new ArrayList<>();
        int totalOffer = types.length;

        for(int i = 0; i < types.length; i++){
            String type = types[i];
            int start = Integer.parseInt(arrivalTimes[i]);
            int end;
            int profit = Integer.parseInt(profits[i]);
            if(type.equalsIgnoreCase("s")){
                end = start + Integer.parseInt(minA[i]);
                Product p = new Product(start, end, profit);
                offers.add(p);
            }
            else{
                end = start + Integer.parseInt(minB[i]);
                Product p = new Product(start, end, profit);
                offers.add(p);
            }
        }

        ArrayList<Integer> maxProfit = new ArrayList<>(Collections.nCopies(totalOffer + 1, 0));
        Collections.sort(offers);
        for(int i = 1; i <= totalOffer; i++){
            int j = i - 1;
            while(j>=1 && offers.get(j - 1).endTime > offers.get(i - 1).startTime){
                j = j - 1;
            }

            maxProfit.set(i,Math.max(maxProfit.get(i - 1), maxProfit.get(j) + offers.get(i - 1).profit));
        }
        out.println(maxProfit.get(totalOffer));
    }
}

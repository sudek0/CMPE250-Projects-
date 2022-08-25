public class Product implements Comparable<Product>{
    int startTime;
    int endTime;
    int profit;

    public Product(int startTime, int endTime, int profit){
        this.startTime = startTime;
        this.endTime = endTime;
        this.profit = profit;
    }

    @Override
    public int compareTo(Product o) {
        if (this.endTime - o.endTime != 0) {
            return this.endTime - o.endTime;
        } else {
            return this.profit - o.profit;
        }
    }
}

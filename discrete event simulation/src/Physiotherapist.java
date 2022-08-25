public class Physiotherapist implements Comparable<Physiotherapist>{
    double serviceTime;
    int ID;

    public Physiotherapist(double serviceTime, int ID){
        this.serviceTime = serviceTime;
        this.ID = ID;

    }

    @Override
    public int compareTo(Physiotherapist o) {
        return this.ID-o.ID;
    }
}

import java.util.Comparator;

public class MassageQueueComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        Double d1 = o1.massageQWaitingTime;
        Double d2 = o2.massageQWaitingTime;
        if(Double.compare(d1,d2)>0){
            return 1;
        }
        else if(Double.compare(d1,d2) < 0){
            return -1;
        }
        else{
            if(o1.ID > o2.ID){
                return 1;
            }
            else if(o1.ID < o2.ID){
                return -1;
            }
            else{
                return 0;
            }
        }

    }
}

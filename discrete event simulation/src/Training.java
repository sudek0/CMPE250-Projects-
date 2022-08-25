import java.util.ArrayList;

public class Training implements Comparable<Training>{
    Player player;
    double arrivalTime;
    double duration;

    public Training(Event tEvent){
        player = tEvent.player;
        arrivalTime = tEvent.time;
        duration = tEvent.duration;
    }

    @Override
    public int compareTo(Training o) {
        if (Double.compare(this.arrivalTime, o.arrivalTime)>0) {
            return 1;
        } else if (Double.compare(this.arrivalTime, o.arrivalTime)<0) {
            return -1;
        }

        else {
            if (this.player.ID > o.player.ID) {
                return 1;
            } else {
                return -1;
            }

        }
    }
}

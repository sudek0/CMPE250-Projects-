import java.util.ArrayList;

public class Physiotherapy implements Comparable<Physiotherapy>{
    Player player;
    double arrivalTime;

    double duration;
    public Physiotherapy(Event pEvent){
        player = pEvent.player;
        arrivalTime = pEvent.time;
        duration = pEvent.duration;
    }

    @Override
    public int compareTo(Physiotherapy o) {
        if(Double.compare(this.player.trainingTime, o.player.trainingTime)>0){
            return -1;
        } else if(Double.compare(this.player.trainingTime, o.player.trainingTime)<0){
            return 1;
        } else{
            if (Double.compare(this.arrivalTime, o.arrivalTime)>0) {
                return 1;
            } else if (Double.compare(this.arrivalTime, o.arrivalTime) < 0) {
                return -1;
            } else{
                if (this.player.ID > o.player.ID) {
                    return 1;
                } else if (this.player.ID < o.player.ID) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}

import java.util.ArrayList;

public class Massage implements Comparable<Massage> {
    Player player;
    double arrivalTime;
    double duration;

    public Massage(Event mEvent){
        player = mEvent.player;
        arrivalTime = mEvent.time;
        duration = mEvent.duration;
    }

    @Override

    public int compareTo(Massage o) {
        if(this.player.skillLevel>o.player.skillLevel){
            return -1;
        } else if(this.player.skillLevel<o.player.skillLevel){
            return 1;
        } else{
            if (Double.compare(this.arrivalTime, o.arrivalTime)>0) {
                return 1;
            } else if (Double.compare(this.arrivalTime, o.arrivalTime)<0) {
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

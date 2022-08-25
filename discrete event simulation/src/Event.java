import java.util.ArrayList;

public class Event implements Comparable<Event>{
    Player player;
    double time;
    double duration;
    int eventType;

    //for entering events
    public Event(int playerID, double time, double duration, ArrayList<Player> array){
        this.player = array.get(playerID);
        this.time = time;
        this.duration = duration;
    }

    //physio entering event
    public Event(Player p, double time){
        player = p;
        this.time = time;

    }
    //for leaving physio
    public Event(Event enteringEvent, Physiotherapist p){
        player = enteringEvent.player;
        time = enteringEvent.time + p.serviceTime;
    }
    //for leaving events
    public Event(Event enteringEvent){
        player = enteringEvent.player;
        time = enteringEvent.time + enteringEvent.duration;
    }


    public Event(Training t){
        player = t.player;
        time = t.player.queueLeavingTime + t.duration;
    }
    public Event(Physiotherapy p){
        player = p.player;
        time = p.player.queueLeavingTime + p.player.physio.serviceTime;
    }
    public Event(Massage m){
        player = m.player;
        time = m.player.queueLeavingTime + m.duration;
    }

    @Override
    public int compareTo(Event o) {
        if (Double.compare(this.time, o.time)>0) {
            return 1;
        }
        else if (Double.compare(this.time, o.time)<0) {
            return -1;
        }
        else{
            if (this.eventType > o.eventType) {
                return -1;
            } else if(this.eventType < o.eventType){
                return 1;
            } else{
                if (this.player.ID > o.player.ID) {
                    return 1;
                }
                else if (this.player.ID < o.player.ID) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        }
    }
}

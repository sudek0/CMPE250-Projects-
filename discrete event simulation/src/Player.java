public class Player {
    int ID;
    int skillLevel;
    int massageCount;
    double trainingTime;
    int status;
    double queueEnteringTime;
    double queueLeavingTime;
    double trainingArrivalTime;
    double leavingPhysioTime;
    Physiotherapist physio;
    double physioQWaitingTime;
    double massageQWaitingTime;

    public Player(int ID, int skillLevel){
        this.ID = ID;
        this.skillLevel = skillLevel;
        massageCount = 0;
        trainingTime = 0;
        status = 0;
        queueEnteringTime = 0;
        queueLeavingTime = 0;
        trainingArrivalTime = 0;
        leavingPhysioTime = 0;
        physioQWaitingTime = 0;
        massageQWaitingTime = 0;
    }

    //checks whether if player is currently on a service or not
    public boolean isAvailable(){
        if(status == 0){
            return true;
        } else{
            return false;
        }
    }

    //checks if the player exceeded massage limit
    public boolean canHaveMassage(){
        if (massageCount<3) {
            return true;
        } else {
            return false;
        }
    }

    public void setTrainingTime(Training t){
        trainingTime = t.duration;
    }
    public double getQueueWaiting() {
        return queueLeavingTime - queueEnteringTime;
    }
    public double getTurnaroundTime(){return leavingPhysioTime - trainingArrivalTime;}
}

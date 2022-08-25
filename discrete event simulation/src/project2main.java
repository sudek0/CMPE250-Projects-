import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class project2main {
    public static double avgCalc(double sum, int divisor){ if(divisor != 0){return sum/divisor;} else{return 0;}}

    public static void main(String[] args) throws FileNotFoundException {
        Locale.setDefault(new Locale("en","US"));
        Scanner in = new Scanner(new File(args[0]));
        PrintStream out = new PrintStream(new File(args[1]));

        PriorityQueue<Training> trainingPQ = new PriorityQueue<>();
        PriorityQueue<Physiotherapy> physioPQ = new PriorityQueue<>();
        PriorityQueue<Massage> massagePQ = new PriorityQueue<>();
        ArrayList<Player> playersArray = new ArrayList<>();
        ArrayList<Player> playersWhoHad3Massages = new ArrayList<>();

        PriorityQueue<Event> eventPQ = new PriorityQueue<>();
        PriorityQueue<Physiotherapist> physiotherapistPQ = new PriorityQueue<>();

        int numOfPlayers = in.nextInt();

        for (int i = 0; i < numOfPlayers; i++){
            int playerID = in.nextInt();
            int skillLevel = in.nextInt();
            Player player = new Player(playerID, skillLevel);
            playersArray.add(player);
        }

        int numOfArrival = in.nextInt();
        String inLineArray[] = new String[4];

        String weirdSpace = in.nextLine();
        for(int a=0; a<numOfArrival; a++){
            inLineArray = in.nextLine().split(" ",4);
            String arrivalType = inLineArray[0];

            int arrivingID = Integer.parseInt(inLineArray[1]);
            double arrivalTime = Double.parseDouble(inLineArray[2]);
            double duration = Double.parseDouble(inLineArray[3]);

            if(arrivalType.equals("m")){
                Event massage = new Event(arrivingID, arrivalTime, duration, playersArray);
                massage.eventType = 3;
                eventPQ.add(massage);
            } else{
                Event training = new Event(arrivingID, arrivalTime, duration, playersArray);
                training.eventType = 1;
                eventPQ.add(training);
            }
        }
        int numOfPhys = in.nextInt();
        int physID = 1;
        for (int p = 0; p < numOfPhys; p++){
            double serviceTime = in.nextDouble();
            Physiotherapist phys = new Physiotherapist(serviceTime, physID);
            physiotherapistPQ.add(phys);
            physID++;
        }
        int numOfTC = in.nextInt();
        int numOfMasseurs = in.nextInt();

        Event cEvent; //c for current
        int type;
        Player cPlayer;
        int numOfCancelledEvents = 0;
        int numOfInvalidEvents = 0;
        double cTime = 0;

        int maxLengthOfTrainingQ = 0;
        int maxLengthOfPhysQ = 0;
        int maxLengthOfMassageQ = 0;

        double totalWaitingInTrainingPQ = 0;
        double totalWaitingInPhysPQ = 0;
        double totalWaitingInMassagePQ = 0;

        double totalTrainingTime = 0;
        double totalPhysTime = 0;
        double totalMassageTime = 0;

        int totalTrainingNum = 0;
        int totalPhysNum = 0;
        int totalMassageNum = 0;

        double totalTurnaroundTime = 0;
        int totalTurnaroundNum = 0;


        while (!eventPQ.isEmpty()){
            cEvent = eventPQ.poll();
            type = cEvent.eventType;
            cPlayer = cEvent.player;
            cTime = cEvent.time;

            if (type == 1) {       //Entering Training
                if(cPlayer.isAvailable()) {
                    Training t = new Training(cEvent);
                    cPlayer.trainingArrivalTime = cTime;
                    totalTrainingNum ++;
                    if (numOfTC > 0) {           //there are available coaches
                        numOfTC--;
                        cPlayer.status = 1;
                        cPlayer.setTrainingTime(t);
                        totalTrainingTime += t.duration;
                        Event leavingT = new Event(cEvent);
                        leavingT.eventType = 4;
                        eventPQ.add(leavingT);
                    } else {                      //there is no available coach
                        trainingPQ.add(t);
                        cPlayer.queueEnteringTime = cTime;
                        cPlayer.status = 1;
                        if(trainingPQ.size()>maxLengthOfTrainingQ){
                            maxLengthOfTrainingQ = trainingPQ.size();
                        }
                    }
                } else{numOfCancelledEvents++;}

            } else if (type == 2) {  //Entering Physio
                Physiotherapy p = new Physiotherapy(cEvent);
                totalPhysNum++;
                 if(!physiotherapistPQ.isEmpty()){
                    Physiotherapist pt = physiotherapistPQ.poll();
                    cPlayer.physio = pt;
                    cPlayer.status = 1;
                    totalPhysTime += pt.serviceTime;
                    Event leavingP = new Event(cEvent, pt);
                    leavingP.eventType = 5;
                    eventPQ.add(leavingP);
                } else{
                     physioPQ.add(p);
                     cPlayer.status = 1;
                     cPlayer.queueEnteringTime = cTime;
                     if(physioPQ.size() > maxLengthOfPhysQ){
                        maxLengthOfPhysQ = physioPQ.size();
                    }
                }

            } else if (type == 3) {  //Entering Massage
                if(cPlayer.canHaveMassage()){               //validity check
                    if(cPlayer.isAvailable()){              //cancellation check
                        Massage m = new Massage(cEvent);
                        totalMassageNum++;
                        cPlayer.massageCount++;
                        if(cPlayer.massageCount >= 3){
                            playersWhoHad3Massages.add(cPlayer);
                        }
                        if (numOfMasseurs > 0){            //There are available masseurs
                            numOfMasseurs --;
                            cPlayer.status = 1;
                            totalMassageTime += m.duration;
                            Event leavingM = new Event(cEvent);
                            leavingM.eventType = 6;
                            eventPQ.add(leavingM);
                        } else{                           //Entering massage queue
                            massagePQ.add(m);
                            cPlayer.queueEnteringTime = cTime;
                            cPlayer.status = 1;
                            if(massagePQ.size()>maxLengthOfMassageQ){
                                maxLengthOfMassageQ = massagePQ.size();
                            }
                        }
                    } else{numOfCancelledEvents++;}
                } else{numOfInvalidEvents++;}

            } else if (type == 4) {   //Leaving Training
                numOfTC++;
                cPlayer.status = 0;
                Event p = new Event(cPlayer, cTime);   //physio after training
                p.eventType = 2;
                eventPQ.add(p);
                if(!trainingPQ.isEmpty()) {           //Taking a player from TrainingQueue
                    Training firstOfTPQ = trainingPQ.poll();
                    Player trainingP = firstOfTPQ.player;
                    trainingP.queueLeavingTime = cTime;
                    trainingP.status = 1;
                    totalWaitingInTrainingPQ += trainingP.getQueueWaiting();
                    numOfTC--;
                    trainingP.setTrainingTime(firstOfTPQ);
                    totalTrainingTime += firstOfTPQ.duration;
                    Event leavingT = new Event(firstOfTPQ);
                    leavingT.eventType = 4;
                    eventPQ.add(leavingT);
                }
            } else if (type == 5) {   //Leaving Physio
                cPlayer.status = 0;
                physiotherapistPQ.add(cPlayer.physio);
                cPlayer.leavingPhysioTime = cTime;
                totalTurnaroundNum++;
                totalTurnaroundTime += cPlayer.getTurnaroundTime();

                if(!physioPQ.isEmpty()){
                    Physiotherapy firstOfPPQ = physioPQ.poll();
                    Player physioP = firstOfPPQ.player;
                    physioP.queueLeavingTime = cTime;
                    physioP.status = 1;
                    Physiotherapist pt = physiotherapistPQ.poll();
                    physioP.physio = pt;
                    totalWaitingInPhysPQ += physioP.getQueueWaiting();
                    physioP.physioQWaitingTime += physioP.getQueueWaiting();
                    totalPhysTime += pt.serviceTime;
                    Event leavingP = new Event(firstOfPPQ);
                    leavingP.eventType = 5;
                    eventPQ.add(leavingP);
                }

            } else {   //TYPE 6      //Leaving Massage
                numOfMasseurs++;
                cPlayer.status = 0;

                if(!massagePQ.isEmpty()){
                    Massage firstOfMPQ = massagePQ.poll();
                    Player massageP = firstOfMPQ.player;
                    massageP.queueLeavingTime = cTime;
                    massageP.status = 1;
                    massageP.massageQWaitingTime += massageP.getQueueWaiting();
                    totalWaitingInMassagePQ += massageP.getQueueWaiting();
                    numOfMasseurs--;
                    totalMassageTime += firstOfMPQ.duration;
                    Event leavingM = new Event(firstOfMPQ);
                    leavingM.eventType = 6;
                    eventPQ.add(leavingM);
                }
            }



        }  //end of while

        Collections.sort(playersArray, new QueueWaitingComparator());
        Collections.sort(playersWhoHad3Massages, new MassageQueueComparator());

        //PRINTING PART
        out.println(maxLengthOfTrainingQ);
        out.println(maxLengthOfPhysQ);
        out.println(maxLengthOfMassageQ);
        out.println(String.format("%.3f", avgCalc(totalWaitingInTrainingPQ, totalTrainingNum)));
        out.println(String.format("%.3f", avgCalc(totalWaitingInPhysPQ, totalPhysNum)));
        out.println(String.format("%.3f", avgCalc(totalWaitingInMassagePQ, totalMassageNum)));
        out.println(String.format("%.3f", avgCalc(totalTrainingTime, totalTrainingNum)));
        out.println(String.format("%.3f", avgCalc(totalPhysTime, totalPhysNum)));
        out.println(String.format("%.3f", avgCalc(totalMassageTime, totalMassageNum)));
        out.println(String.format("%.3f", avgCalc(totalTurnaroundTime, totalTurnaroundNum)));
        out.println(playersArray.get(0).ID + " " + String.format("%.3f", playersArray.get(0).physioQWaitingTime));
        out.println(playersWhoHad3Massages.get(0).ID + " " + String.format("%.3f", playersWhoHad3Massages.get(0).massageQWaitingTime));
        out.println(numOfInvalidEvents);
        out.println(numOfCancelledEvents);
        out.println(String.format("%.3f", cTime));



    }
}

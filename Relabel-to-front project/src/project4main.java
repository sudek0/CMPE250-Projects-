import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class project4main {
    public static void createVehicle(int capacity, Node sink, ArrayList<Node> nodeArray, ArrayDeque<Node> nodesDeque){
        Node vehicle = new Node();
        vehicle.height = 1;
        vehicle.capacity = capacity;
        nodeArray.add(vehicle);
        nodesDeque.addFirst(vehicle);
        int[] edge = new int[2];
        edge[0] = 0; edge[1] = capacity;
        vehicle.map.put(sink, edge);              //from vehicle to sink
        int[] edgeR = new int[2];
        edgeR[0] = capacity; edgeR[1] = capacity;
        sink.map.put(vehicle, edgeR);                //from sink to vehicle
    }
    public static void edgeToVehicle(Node bag, int start, int end, ArrayList<Node> nodes, int giftNum){
        for(int i = start; i <= end; i++){
            int[] edge = new int[2];
            int capacity = Math.min(giftNum, nodes.get(i).capacity);
            edge[0] = 0; edge[1] = capacity;
            bag.map.put(nodes.get(i), edge);   //UPWARDS EDGE
            int[] edgeR = new int[2];
            edgeR[0] = capacity; edgeR[1] = capacity;
            nodes.get(i).map.put(bag, edgeR);   //BACKWARDS EDGE
        }
    }
    public static void preFlowAndSourceEdge(Node source, Node bag, int giftNum){
        int[] edge = new int[2];
        edge[0] = giftNum; edge[1] = giftNum;
        source.map.put(bag, edge);             //from source to bag
        int[] edgeR = new int[2];
        edgeR[0] = 0; edgeR[1] = giftNum;
        bag.map.put(source, edgeR);             //from bag to source
        bag.excessFlow += giftNum;
    }

    public static void relabel(Node cNode, ArrayDeque<Node> nodesDeque){
        int min = Integer.MAX_VALUE;
        for(Node v: cNode.map.keySet()) {
            int[] edge = cNode.map.get(v);
            if (edge[0] == edge[1]) {
            }
            else {
                if(min == 0){
                    break;
                }
                min = Math.min(min, v.height);
            }
        }
        cNode.height = min + 1;
    }
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File(args[0]));
        PrintStream out = new PrintStream(new File(args[1]));
        ArrayList<Node> nodeArray = new ArrayList<>();
        Node sink = new Node();         //0th index at nodes arraylist
        nodeArray.add(sink);
        Node source = new Node();       //last index at nodes arraylist
        ArrayDeque<Node> nodesDeque = new ArrayDeque<>();

        int capacity;
        int greenTrainNumI = in.nextInt();
        int greenTrainNum = 0;
        for(int gt = 0; gt < greenTrainNumI; gt ++){
            capacity = in.nextInt();
            if(capacity != 0) {createVehicle(capacity, sink, nodeArray, nodesDeque); greenTrainNum++;}
        }
        int greenTrainEndIndex = greenTrainNum;

        int redTrainNumI = in.nextInt();
        int redTrainNum = 0;
        for(int rt = 0; rt < redTrainNumI; rt++){
            capacity = in.nextInt();
            if(capacity != 0){createVehicle(capacity, sink, nodeArray, nodesDeque); redTrainNum++;}
        }
        int redTrainEndIndex = greenTrainEndIndex + redTrainNum;

        int greenDeerNumI = in.nextInt();
        int greenDeerNum = 0;
        for(int gd = 0; gd < greenDeerNumI; gd++){
            capacity = in.nextInt();
            if(capacity != 0) {createVehicle(capacity, sink, nodeArray,nodesDeque); greenDeerNum++;}
        }
        int greenDeerEndIndex = redTrainEndIndex + greenDeerNum;

        int redDeerNumI = in.nextInt();
        int redDeerNum = 0;
        for(int rd = 0; rd < redDeerNumI; rd++){
            capacity = in.nextInt();
            if(capacity != 0) {createVehicle(capacity, sink, nodeArray, nodesDeque); redDeerNum++;}
        }
        int redDeerEndIndex = greenDeerEndIndex + redDeerNum;
        int bagNum = in.nextInt();
        Node bag;
        int totalGiftNum = 0;
        for(int b = 0; b < bagNum; b++){
            bag = new Node();
            bag.height = 2;
            String type = in.next();
            int giftNum = in.nextInt();
            totalGiftNum += giftNum;
            if(giftNum !=0) {
                preFlowAndSourceEdge(source, bag, giftNum);  //edge btw source and bags
                nodeArray.add(bag);
                nodesDeque.addFirst(bag);
                switch (type) {
                    case "a" -> edgeToVehicle(bag, 1, redDeerEndIndex, nodeArray, 1);
                    case "b" -> {
                        edgeToVehicle(bag, 1, greenTrainEndIndex, nodeArray, giftNum);
                        edgeToVehicle(bag, redTrainEndIndex + 1, greenDeerEndIndex, nodeArray, giftNum);
                    }
                    case "c" -> {
                        edgeToVehicle(bag, greenTrainEndIndex + 1, redTrainEndIndex, nodeArray, giftNum);
                        edgeToVehicle(bag, greenDeerEndIndex + 1, redDeerEndIndex, nodeArray, giftNum);
                    }
                    case "d" -> edgeToVehicle(bag, 1, redTrainEndIndex, nodeArray, giftNum);
                    case "e" -> edgeToVehicle(bag, redTrainEndIndex + 1, redDeerEndIndex, nodeArray, giftNum);
                    case "ab" -> {
                        edgeToVehicle(bag, 1, greenTrainEndIndex, nodeArray, 1);
                        edgeToVehicle(bag, redTrainEndIndex + 1, greenDeerEndIndex, nodeArray, 1);
                    }
                    case "ac" -> {
                        edgeToVehicle(bag, greenTrainEndIndex + 1, redTrainEndIndex, nodeArray, 1);
                        edgeToVehicle(bag, greenDeerEndIndex + 1, redDeerEndIndex, nodeArray, 1);
                    }
                    case "ad" -> edgeToVehicle(bag, 1, redTrainEndIndex, nodeArray, 1);
                    case "ae" -> edgeToVehicle(bag, redTrainEndIndex + 1, redDeerEndIndex, nodeArray, 1);
                    case "bd" -> edgeToVehicle(bag, 1, greenTrainEndIndex, nodeArray, giftNum);
                    case "be" -> edgeToVehicle(bag, redTrainEndIndex + 1, greenDeerEndIndex, nodeArray, giftNum);
                    case "cd" -> edgeToVehicle(bag, greenTrainEndIndex + 1, redTrainEndIndex, nodeArray, giftNum);
                    case "ce" -> edgeToVehicle(bag, greenDeerEndIndex + 1, redDeerEndIndex, nodeArray, giftNum);
                    case "abd" -> edgeToVehicle(bag, 1, greenTrainEndIndex, nodeArray, 1);
                    case "abe" -> edgeToVehicle(bag, redTrainEndIndex + 1, greenDeerEndIndex, nodeArray, 1);
                    case "acd" -> edgeToVehicle(bag, greenTrainEndIndex + 1, redTrainEndIndex, nodeArray, 1);
                    case "ace" -> edgeToVehicle(bag, greenDeerEndIndex + 1, redDeerEndIndex, nodeArray, 1);
                }
            }
        } //end of input

        source.height = nodeArray.size();
        nodeArray.add(source);
        HashSet<Node> relabelled = new HashSet<>();
        int nonRelabelledCount = redDeerEndIndex;
        Iterator<Node> it = nodesDeque.iterator();

        while(it.hasNext() && nonRelabelledCount > 0){
            Node u = it.next();
            int height = u.height;
            while(u.excessFlow > 0){
                for(Node v: u.map.keySet()){
                    int[] edge = u.map.get(v);
                    if(edge[0] == edge[1]){
                        continue;
                    }
                    if(u.height > v.height) {
                        int flow = Math.min(edge[1] - edge[0], u.excessFlow);
                        u.excessFlow -= flow;
                        v.excessFlow += flow;
                        edge[0] += flow;           //upwards edge update
                        v.map.get(u)[0] -= flow;   //backwards edge update
                    }
                }
                if(u.excessFlow == 0)break;
                relabel(u, nodesDeque);
                if(nodeArray.indexOf(u) <= redDeerEndIndex && !relabelled.contains(u) && nodeArray.indexOf(u) != 0) {
                    relabelled.add(u);
                    nonRelabelledCount -= 1;
                }
            }
            if(u.height > height){
                it.remove();
                nodesDeque.addFirst(u);
                it = nodesDeque.iterator();
            }
        }
        out.println(totalGiftNum - sink.excessFlow);
    }
}
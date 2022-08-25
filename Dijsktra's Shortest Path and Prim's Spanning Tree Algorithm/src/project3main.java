import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class project3main {
    public static int toID(String s){
        int ID = Integer.parseInt(s.substring(1));
        return ID;
    }

    public static Node createNode(int newIndex, HashMap<Integer, Node> nodesCreated){
        Node newNode;
        if(!nodesCreated.containsKey(newIndex)){
            newNode = new Node(newIndex, nodesCreated);
            nodesCreated.put(newIndex, newNode);
        }
        else{newNode = nodesCreated.get(newIndex);}
        return newNode;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File(args[0]));
        PrintStream out = new PrintStream(new File(args[1]));

        int setTimeByFather = in.nextInt();
        int cityNum = in.nextInt();
        int mecnunCityID = toID(in.next());
        int leylaCityID = toID(in.next());

        PriorityQueue<Pair> pairPQ = new PriorityQueue<>();
        PriorityQueue<Pair> pairPQD = new PriorityQueue<>();
        HashMap<Node, ArrayList<Pair>> mapC = new HashMap<>();
        HashMap<Integer, Node> nodesCreated = new HashMap<>();
        HashMap<Node, ArrayList<Pair>> mapD = new HashMap<>();
        HashMap<Integer, Node> nodesCreatedD = new HashMap<>();

        Node mecnunCity =  new Node(mecnunCityID, 0, nodesCreated);
        Pair mecnun = new Pair(mecnunCity, 0);
        pairPQ.add(mecnun);

        in.nextLine();

        while (in.hasNext()) {
            String x = in.nextLine();
            String[] lineArray = x.split(" ");
            int u = toID(lineArray[0]);

            //if it is an edge from c to c
            if(lineArray.length > 1) {
                Node vNode;
                int cityIndex;
                int edgeLength;
                Pair adjPair;

                if (lineArray[0].startsWith("c") && lineArray[1].startsWith("c")) {
                    Node uNode = createNode(u, nodesCreated);
                    ArrayList<Pair> adjacencyArrays = new ArrayList<>();

                    for (int lineIndex = 1; lineIndex < lineArray.length; lineIndex = lineIndex + 2) {
                        cityIndex = toID(lineArray[lineIndex]);
                        edgeLength = Integer.parseInt(lineArray[lineIndex + 1]);
                        if(u != cityIndex) {
                            vNode = createNode(cityIndex, nodesCreated);
                            adjPair = new Pair(vNode, edgeLength);
                            adjacencyArrays.add(adjPair);
                        }
                    }
                    mapC.put(uNode, adjacencyArrays);
                }
                else if (lineArray[0].startsWith("c") && lineArray[1].startsWith("d")) {
                    Node leylaCity = createNode(0, nodesCreatedD);  //INITIALIZED LEYLA CITY AS 0
                    Pair leyla = new Pair(leylaCity,0);
                    pairPQD.add(leyla);
                    ArrayList<Pair> adjacencyArrayD = new ArrayList<>();

                    for (int lineIndex = 1; lineIndex < lineArray.length; lineIndex = lineIndex + 2){
                        cityIndex = toID(lineArray[lineIndex]);
                        edgeLength = Integer.parseInt(lineArray[lineIndex + 1]);
                        if(u != cityIndex) {
                            vNode = createNode(cityIndex, nodesCreatedD);
                            adjPair = new Pair(vNode, edgeLength);
                            adjacencyArrayD.add(adjPair);
                        }
                    }
                    mapD.put(leylaCity, adjacencyArrayD);
                }
                else if(lineArray[0].startsWith("d") && lineArray[1].startsWith("d")){//from d to d
                    Node uNodeD = createNode(u, nodesCreatedD);
                    ArrayList<Pair> adjArrayLtoR = new ArrayList<>();
                    ArrayList<Pair> adjArrayRtoL = new ArrayList<>();

                    for (int lineIndex = 1; lineIndex < lineArray.length; lineIndex = lineIndex + 2) {
                        cityIndex = toID(lineArray[lineIndex]);
                        edgeLength = Integer.parseInt(lineArray[lineIndex + 1]);
                        if(u != cityIndex) {
                            vNode = createNode(cityIndex, nodesCreatedD);
                            if (vNode.cityIndex < u) {  //there might be a shorter path
                                if (mapD.containsKey(vNode)) { //mapte o node varsa
                                    for (Pair p : mapD.get(vNode)) {
                                        if (p.node.cityIndex == u) {
                                            if (p.weight > edgeLength) {
                                                p.weight = edgeLength;
                                            } else {
                                                edgeLength = p.weight;
                                            }
                                        }
                                    }
                                }
                            }
                            adjPair = new Pair(vNode, edgeLength);
                            adjArrayLtoR.add(adjPair);

                            if (mapD.keySet().contains(vNode)) {
                                adjPair = new Pair(uNodeD, edgeLength);
                                mapD.get(vNode).add(adjPair);
                            } else {
                                adjPair = new Pair(uNodeD, edgeLength);
                                adjArrayRtoL.add(adjPair);
                                mapD.put(vNode, adjArrayRtoL);
                            }
                        }
                    }
                    mapD.put(uNodeD,adjArrayLtoR);
                } else{ //edge from d to c ************************************************
                }
            }
        }

        while (!pairPQ.isEmpty()){
            Pair u = pairPQ.poll();
            Node uNode = u.node;
            int weight;
            Node vNode;
            if(!mapC.containsKey(uNode)) {
                continue;
            }
            ArrayList<Pair> adjArrayL = mapC.get(uNode);
            Collections.sort(adjArrayL);
            for (int adjI = 0; adjI < adjArrayL.size(); adjI++) {
                vNode = adjArrayL.get(adjI).node;
                weight = adjArrayL.get(adjI).weight;
                if (vNode.totalDistance > uNode.totalDistance + weight) {
                    vNode.totalDistance = uNode.totalDistance + weight;
                    vNode.parent = uNode.cityIndex;
                    Pair newPair = new Pair(vNode, vNode.totalDistance);
                    pairPQ.add(newPair);
                }
            }
        }

        while(!pairPQD.isEmpty()){
            Pair u = pairPQD.poll();
            Node uNode = u.node;
            if(uNode.isPartOfMST == true){
                continue;
            }
            uNode.isPartOfMST = true;

            int weight;
            Node vNode;
            if(!mapD.containsKey(uNode)) {
                continue;
            }
            ArrayList<Pair> adjArrayL = mapD.get(uNode);
            Collections.sort(adjArrayL);
            for (int adjI = 0; adjI < adjArrayL.size(); adjI++) {
                vNode = adjArrayL.get(adjI).node;
                weight = adjArrayL.get(adjI).weight;
                if (vNode.isPartOfMST == false && vNode.key > weight) {
                    vNode.key = weight;
                    vNode.parent = uNode.cityIndex;
                    Pair newPair = new Pair(vNode, vNode.key);
                    pairPQD.add(newPair);
                }
            }
        }

        Boolean married = false;

        //PRINTING PART 1
        if(nodesCreated.get(leylaCityID).parent == -2){ //there is no path
            out.println(-1);
        }
        else {
            if(setTimeByFather >= nodesCreated.get(leylaCityID).totalDistance){
                married = true;
            }
            ArrayList<Integer> pathArray = new ArrayList<>();
            int pathIndex = leylaCityID;
            while (pathIndex != -1) {
                int parent = nodesCreated.get(pathIndex).parent;
                pathIndex = parent;
                pathArray.add(parent);
            }
            for(int i = 2; i < pathArray.size()+1; i++){
                out.print("c"+pathArray.get(pathArray.size()-i)+" ");
            }
            out.println("c"+leylaCityID);
        }

        //PRINTING PART 2
        nodesCreatedD.get(0).parent = -3;
        nodesCreatedD.get(0).key = 0;
//        Boolean thereIsAPath = true;
//        for(int aaa: nodesCreatedD.keySet()){
//            if(nodesCreatedD.get(aaa).isPartOfMST == false || (dCityCount+1)!= nodesCreatedD.size()){
//                thereIsAPath = false;
//            }
//        }

        if(!married){
            out.println(-1);
        }
//        else if(!thereIsAPath){
//            out.println(-2);
//        }

        else{
            int totalLength = 0;
            for(int cityy: nodesCreatedD.keySet()) {
                totalLength += nodesCreatedD.get(cityy).key;
            }
            out.println(totalLength*2);
        }
    }
}
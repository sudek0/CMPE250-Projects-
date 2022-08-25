import java.util.HashMap;

public class Node {
    int totalDistance;
    int cityIndex;
    int parent;
    int key;
    Boolean isPartOfMST = false;

    public Node(int cityIndex, int totalDistance, HashMap<Integer, Node> nodesCreated){
        this.cityIndex = cityIndex;
        this.totalDistance = totalDistance;
        nodesCreated.put(cityIndex, this);
        parent = -1;
    }

    public Node(int cityIndex, HashMap<Integer, Node> nodesCreated){
        this.cityIndex = cityIndex;
        totalDistance = Integer.MAX_VALUE;
        nodesCreated.put(cityIndex, this);
        parent = -2;
        key = Integer.MAX_VALUE;
    }
}

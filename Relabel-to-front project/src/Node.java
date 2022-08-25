import java.util.HashMap;
public class Node implements Comparable<Node>{
    int height;
    int excessFlow;
    HashMap<Node, int[]> map;
    int capacity=0;

    public Node(){
        height = 0;
        excessFlow = 0;
        map = new HashMap<>();
    }
    @Override
    public int compareTo(Node o) {
        if(this.height - o.height == 0){return 1;}
        return this.height - o.height;
    }
}

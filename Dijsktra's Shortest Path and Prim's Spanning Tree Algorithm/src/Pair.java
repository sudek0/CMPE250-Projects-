public class Pair implements  Comparable<Pair>{
    int weight;
    Node node;

    public Pair(Node node, int weight){
        this.node = node;
        this.weight = weight;
    }

    @Override
    public int compareTo(Pair o) {
        if(this.weight-o.weight !=0) {
            return this.weight - o.weight;
        }
        else{
            return this.node.cityIndex-o.node.cityIndex;
        }
    }
}

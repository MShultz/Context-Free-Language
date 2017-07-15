import java.util.ArrayList;

/**
 * Created by Mary on 7/14/2017.
 */

public class Node {
    ArrayList<Node> children;
    Node parent;
    String word;

    public Node(){
        children = new ArrayList<>();
    }
}

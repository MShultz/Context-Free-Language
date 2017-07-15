import java.util.ArrayList;

/**
 * Created by Mary on 7/14/2017.
 */

public class Node {
    private ArrayList<Node> children;
    private Node parent;
    private String word;
    private  WordType type;


    public Node(WordType type) {
        this.type = type;
        children = new ArrayList<>();
    }

    public Node(String word, WordType type) {
        this.word = word;
        this.type = type;
    }

    public Node(){
        children = new ArrayList<>();
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node child){
        children.add(child);
    }

    public Node getParent() {
        return parent;
    }

    public WordType getType() {
        return type;
    }
}

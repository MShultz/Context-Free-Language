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

    public void print(){
         if (children == null){
            System.out.println(word);
        }else{
            for(Node child: children){
                child.print();
            }
        }
    }

    public void printResponse(){
        if (children == null){
            transformWord();
        }else{
            for(Node child: children){
                child.printResponse();
            }
        }
    }

    private void transformWord(){
        String temp;
        if(type.equals(WordType.ARTICLE)){
            temp = Generator.articleReplacements[Program.rand.nextInt(2)];
        }else if(type.equals(WordType.VERB)){
            temp = word;
            temp = temp.substring(0, temp.length()-1);
            temp += (temp.charAt(temp.length()-1) == 'e')? "d" : "ed";
        }else{
           temp = word;
        }
        System.out.print(" " + temp);
    }
}

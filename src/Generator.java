import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Mary on 7/14/2017.
 */
public class Generator {

    private KVP currentTree;
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<KVP> treeHistory = new ArrayList<>();
    protected static Random rand = new Random();
    protected static String[] articleReplacements = {"this", "that"};
    private String[] starters = {"Does it make you happy that", "Wasn't it quite interesting when", "Shouldn't we be worried that"};
    private final int TOTAL_STARTERS = 3;
    private String[] invalidResponses = {
            "That seems interesting. Tell me more?",
            "Alright now, don't go getting ahead of yourself!",
            "That doesn't interest me. Lets talk about Trash-Pandas!"
    };

    public Node generateTree(ArrayList<Word> words) {
        nodes.clear();
        Word current;
        WordType currentType;
        for (int i = 0; i < words.size(); ++i) {
            current = words.get(i);
            currentType = current.getType();
            if (isBase(currentType)) {
                nodes.add(new Node(current.getWord(), current.getType()));
            } else if (isNP(currentType)) {
                handleNP(i);
            } else if (isVP(currentType)) {
                handleVP(i);
            } else if (isPrep(currentType)) {
                handlePrep(i);
            } else if (isS(currentType)) {
                handleSentence(i);
            }
        }
        return nodes.get(nodes.size() - 1);
    }

    public void generateResponse() {

        System.out.print(starters[determineStarter(currentTree)]);
        currentTree.getKey().printResponse();
        System.out.println("?");
    }

    protected void generateInvalidResponse() {
        if (!printInvalidStarter()) {
            currentTree.getKey().printResponse();
            System.out.println("?");
        }
    }

    private void handleNP(int i) {
        Node np = new Node(WordType.NOUNPHRASE);
        nodes.add(np);
        if (hasPrevious(i) && isPrep(getPrevious(i).getType())) {
            handleNAndA(i, np);
            Node prep = getPrevious(i);
            prep.setParent(np);
            np.addChild(prep);
        } else if (has2Previous(i) && isN(getPrevious(i).getType()) && isA(getPrevious(i - 1).getType())) {
            handleNAndA(i, np);
        }
    }

    private void handleVP(int i) {
        Node vp = new Node(WordType.VERBPHRASE);
        nodes.add(vp);
        Node v = getPreviousVerb(i);
        v.setParent(vp);
        vp.addChild(v);
        if (isNP(getPrevious(i).getType())) {
            Node np = getPrevious(i);
            np.setParent(vp);
            vp.addChild(np);
        }

    }

    private void handlePrep(int i) {
        Node prep = new Node(WordType.PREPOSITION);
        nodes.add(prep);
        Node undf = getPreviousUndf(i);
        Node np = getPreviousNP(i);
        prep.addChild(undf);
        prep.addChild(np);
        undf.setParent(prep);
        np.setParent(prep);
    }

    private void handleNAndA(int i, Node np) {
        Node article = getPreviousArticle(i);
        article.setParent(np);
        np.addChild(article);
        Node noun = getPreviousNoun(i);
        noun.setParent(np);
        np.addChild(noun);
    }

    private void handleSentence(int i) {
        Node sentence = new Node(WordType.SENTENCE);
        nodes.add(sentence);
        Node vp = getPreviousVerbPhrase(i);
        Node np = getPreviousNP(i);

        sentence.addChild(np);
        sentence.addChild(vp);
        vp.setParent(sentence);
        np.setParent(sentence);
    }

    private boolean isBase(WordType type) {
        return type.equals(WordType.ARTICLE) || type.equals(WordType.NOUN) || type.equals(WordType.VERB) || type.equals(WordType.UNDEFINED);
    }

    private boolean isNP(WordType type) {
        return type.equals(WordType.NOUNPHRASE);
    }

    private boolean isN(WordType type) {
        return type.equals(WordType.NOUN);
    }

    private boolean isA(WordType type) {
        return type.equals(WordType.ARTICLE);
    }

    private boolean isUndf(WordType type) {
        return type.equals(WordType.UNDEFINED);
    }

    private boolean isVP(WordType type) {
        return type.equals(WordType.VERBPHRASE);
    }

    private boolean isS(WordType type) {
        return type.equals(WordType.SENTENCE);
    }

    private boolean isV(WordType type) {
        return type.equals(WordType.VERB);
    }

    private boolean isPrep(WordType type) {
        return type.equals(WordType.PREPOSITION);
    }

    private boolean has2Previous(int currentIndex) {
        return currentIndex >= 2;
    }

    private boolean hasPrevious(int currentIndex) {
        return currentIndex >= 1;
    }

    private Node getPreviousNoun(int currentIndex) {
        Node temp;
        for (int i = currentIndex; i >= 0; --i) {
            temp = nodes.get(i);
            if (temp.getParent() == null && isN(temp.getType())) {
                return temp;
            }
        }
        throw new IllegalStateException("No Noun was found.");
    }

    private Node getPreviousUndf(int currentIndex) {
        Node temp;
        for (int i = currentIndex; i >= 0; --i) {
            temp = nodes.get(i);
            if (temp.getParent() == null && isUndf(temp.getType())) {
                return temp;
            }
        }
        throw new IllegalStateException("No Preposition word was found.");
    }

    private Node getPreviousNP(int currentIndex) {
        Node temp;
        for (int i = currentIndex; i >= 0; --i) {
            temp = nodes.get(i);
            if (temp.getParent() == null && isNP(temp.getType())) {
                return temp;
            }
        }
        throw new IllegalStateException("No Noun Phrase was found.");
    }

    private Node getPreviousArticle(int currentIndex) {
        Node temp;
        for (int i = currentIndex; i >= 0; --i) {
            temp = nodes.get(i);
            if (temp.getParent() == null && isA(temp.getType())) {
                return temp;
            }
        }
        throw new IllegalStateException("No Article was found.");
    }

    private Node getPreviousVerb(int currentIndex) {
        Node temp;
        for (int i = currentIndex; i >= 0; --i) {
            temp = nodes.get(i);
            if (temp.getParent() == null && isV(temp.getType())) {
                return temp;
            }
        }
        throw new IllegalStateException("No Verb was found.");
    }

    private Node getPreviousVerbPhrase(int currentIndex) {
        Node temp;
        for (int i = currentIndex; i >= 0; --i) {
            temp = nodes.get(i);
            if (temp.getParent() == null && isVP(temp.getType())) {
                return temp;
            }
        }
        throw new IllegalStateException("No Noun was found.");
    }

    private Node getPrevious(int currentIndex) {
        return nodes.get(currentIndex - 1);
    }


    public void setCurrentTree(Node currentTree) {
        KVP pair = new KVP(currentTree, 0);
        if (!treeHistory.contains(pair)) {
            treeHistory.add(pair);
        } else {
            pair = treeHistory.get(treeHistory.indexOf(pair));
            pair.incrementValue();
        }
        this.currentTree = pair;
    }

    private int determineStarter(KVP current) {
        if (current.getValue() >= TOTAL_STARTERS) {
            current.resetValue();
            return 0;
        } else {
            return current.getValue();
        }
    }

    private boolean printInvalidStarter() {
        if (currentTree != null) {
            currentTree.incrementValue();
            boolean used = currentTree.getValue() >= TOTAL_STARTERS;
            int count = treeHistory.size() - 1;
            while (used && count > 0){
                if (used = currentTree.getValue() >= TOTAL_STARTERS) {
                    --count;
                    currentTree = treeHistory.get(count);

                }
            }
            if (!used) {
                System.out.print(starters[currentTree.getValue()]);
            } else {
                System.out.println(invalidResponses[rand.nextInt(invalidResponses.length)]);
            }
            return used;
        } else {
            System.out.println(invalidResponses[rand.nextInt(invalidResponses.length)]);
            return true;
        }
    }
}

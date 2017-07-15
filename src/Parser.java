import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by Mary on 7/13/2017.
 */
public class Parser {

    private Stack<Word> parseStack;
    private ArrayList<Word> parseTree;




    public boolean parse(String[] input){
        parseStack = new Stack<Word>();
        parseTree = new ArrayList<>();
        Word current;
        Word nextStackItem;
        Word nextNextItem;
        boolean hasNext;
        boolean canReduce;
        for(int i = 0; i < input.length; ++i){
            parseStack.push(new Word(input[i], WordType.UNDEFINED));
            canReduce = true;
            while(canReduce){
                current = parseStack.peek();
                if(hasNextStackItem()) {
                    nextStackItem = getNextStackItem();
                    if(hasNextNextStackItem())
                    nextNextItem = getNextNextStackItem();
                    else
                        nextNextItem = null;
                }else{
                    nextStackItem = nextNextItem = null;
                }
                hasNext = i + 1 < input.length;


                if(nextStackItem != null && (nextStackItem.getType().equals(WordType.NOUNPHRASE) && current.getType().equals(WordType.VERBPHRASE))){
                    reduceToSentence();
                }else if(nextStackItem != null && (nextStackItem.getType().equals(WordType.ARTICLE) && current.getType().equals(WordType.NOUN)) && (!hasNext || !isPreposition(input[i + 1]))){
                    reduceANToNounPhrase();
                }else if(nextNextItem != null && nextNextItem.getType().equals(WordType.ARTICLE) && nextStackItem.getType().equals(WordType.NOUN) && current.getType().equals(WordType.PREPOSITION)){
                    reduceANPToNounPhrase();
                }else if(current.getType().equals(WordType.VERB) && (!hasNext || (!isArticle(input[i + 1]) && !isNoun(input[i + 1])))){
                    reduceVToVerbPhrase();
                }else if(nextStackItem != null && (nextStackItem.getType().equals(WordType.VERB) && current.getType().equals(WordType.NOUNPHRASE))){
                    reduceToVerbPhrase();
                }else if(current.getType().equals(WordType.UNDEFINED) && isArticle(current.getWord())){
                    current.setType(WordType.ARTICLE);
                    parseTree.add( new Word(current));
                }else if(current.getType().equals(WordType.UNDEFINED) && isNoun(current.getWord())){
                    current.setType(WordType.NOUN);
                    parseTree.add(new Word(current));
                }else if(current.getType().equals(WordType.UNDEFINED) && isVerb(current.getWord())){
                    current.setType(WordType.VERB);
                    parseTree.add(new Word(current));
                }else if(nextStackItem != null && isPreposition(nextStackItem.getWord()) && current.getType().equals(WordType.NOUNPHRASE)) {
                    reduceToPreposition();
                }else{
                    if(isPreposition(current.getWord()))
                        parseTree.add(current);
                    canReduce = false;
                }
            }
        }
        if(parseStack.peek().getType().equals(WordType.SENTENCE))
            return true;
        else
            return false;
    }

    private boolean isArticle(String input){
        return (input.equals("a") || input.equals("the"));
    }

    private boolean isNoun(String input){
        return (input.equals("Netflix") || input.equals("Trash-Panda") || input.equals("Danger-Noodle") || input.equals("games") || input.equals("television"));
    }

    private boolean isVerb(String input){
        return (input.equals("plays") || input.equals("likes") || input.equals("watches"));
    }

    private boolean isPreposition(String input){
        return (input.equals("with") || input.equals("on"));
    }
    private void reduceToVerbPhrase(){
        Word verb = parseStack.pop();
        Word nounPhrase = parseStack.pop();
        parseStack.push(new Word(nounPhrase.getWord() + " " + verb.getWord(), WordType.VERBPHRASE));
        parseTree.add(parseStack.peek());
    }
    private void reduceToPreposition(){
        Word preposition = parseStack.pop();
        Word nounPhrase = parseStack.pop();

        parseStack.push(new Word(preposition.getWord() + " " + nounPhrase.getWord(), WordType.PREPOSITION));
        parseTree.add( parseStack.peek());
    }
    private void reduceVToVerbPhrase(){
        parseStack.peek().setType(WordType.VERBPHRASE);
        parseTree.add( parseStack.peek());
    }
    private void reduceANPToNounPhrase(){
        Word preposition = parseStack.pop();
        Word noun = parseStack.pop();
        Word article = parseStack.pop();
        parseStack.push(new Word(article.getWord() + " " + noun.getWord() + " " + preposition.getWord(), WordType.NOUNPHRASE));
        parseTree.add( parseStack.peek());
    }
    private void reduceANToNounPhrase(){
        Word noun = parseStack.pop();
        Word article = parseStack.pop();
        parseStack.push(new Word(article.getWord() + " " + noun.getWord(), WordType.NOUNPHRASE));
        parseTree.add(parseStack.peek());
    }
    private void reduceToSentence(){
        Word verbPhrase = parseStack.pop();
        Word nounPhrase = parseStack.pop();
        parseStack.push(new Word(nounPhrase.getWord() +" " + verbPhrase.getWord(), WordType.SENTENCE));
        parseTree.add(parseStack.peek());
    }
    private Word getNextStackItem(){
        Stack<Word> tempStack = new Stack<>();
        tempStack.push(parseStack.pop());
        Word lookAhead = parseStack.peek();
        parseStack.push(tempStack.pop());
        return lookAhead;
    }

    private boolean hasNextStackItem(){
        Word temp = parseStack.pop();
        try{
            parseStack.peek();
        }catch(EmptyStackException e){
            parseStack.push(temp);
            return false;
        }
        parseStack.push(temp);
        return true;
    }

    private boolean hasNextNextStackItem(){
        Word temp = parseStack.pop();
        Word temp2 = parseStack.pop();
        try{
            parseStack.peek();
        }catch(EmptyStackException e){
            parseStack.push(temp2);
            parseStack.push(temp);
            return false;
        }
        parseStack.push(temp2);
        parseStack.push(temp);
        return true;
    }

    private Word getNextNextStackItem(){
        Stack<Word> tempStack = new Stack<>();
        tempStack.push(parseStack.pop());
        tempStack.push(parseStack.pop());
        Word lookAhead = parseStack.peek();
        parseStack.push(tempStack.pop());
        parseStack.push(tempStack.pop());
        return lookAhead;
    }

    public ArrayList<Word> getParseTree() {
        return parseTree;
    }
}

import java.util.Random;
import java.util.Scanner;

/**
 * Created by Mary on 7/13/2017.
 */
public class Program {

    protected static Random rand = new Random();

    public void run(){
        Parser parser = new Parser();
        Generator gen = new Generator();
        Tokenizer tokenizer = new Tokenizer();
        System.out.println("Welcome to chat bot!");
        String input;
        Scanner scan = new Scanner(System.in);
        while(true){
            input = scan.nextLine();
            boolean isValid = parser.parse(tokenizer.tokenize(input));
            if(isValid){
               gen.generateResponse(gen.generateTree(parser.getParseTree()));
            }else{
               printInvalidResponse();
            }
        }
    }
    private String[] invalidResponses = {
            "That seems interesting. Tell me more?",
            "Alright now, don't go getting ahead of yourself!",
            "That doesn't interest me. Lets talk about Trash-Pandas!"
    };
    private void printInvalidResponse(){
        System.out.println(invalidResponses[rand.nextInt(invalidResponses.length)]);
    }
}

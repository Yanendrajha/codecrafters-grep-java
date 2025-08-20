import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-E")) {
            System.out.println("Usage: ./your_program.sh -E <pattern>");
            System.exit(1);
        }
        
        String pattern = args[1];
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();
        
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");
        
        if (matchPattern(inputLine, pattern)) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }
    
    public static boolean matchPattern(String inputLine, String pattern) {
        if (pattern.length() == 1) {
            return inputLine.contains(pattern);
        }
        if (pattern.startsWith("^")) {
            return matchHere(inputLine, pattern.substring(1));
        }
        
        for (int i = 0; i <= inputLine.length(); i++) {
            if (matchHere(inputLine.substring(i), pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean matchHere(String inputLine, String pattern) {
        
        // BASE CASE 1: If the pattern is empty, we have successfully matched everything.
        if(pattern.isEmpty()) {
            return true;
        }
        
        // Handle the '$' anchor for end-of-line.
        if("$".equals(pattern)) {
            return inputLine.isEmpty();
        }
        
        // Check if the next part of the pattern is a character followed by a '+'
        if(pattern.length() > 1 && pattern.charAt(1) == '+') {
            char charRepeat = pattern.charAt(0);
            
            // Check for the mandatory "one" match.
            // The first character of the text must match the character to be repeated.
            // Also handles the '.' wildcard.
            if(!inputLine.isEmpty() && (charRepeat == inputLine.charAt(0) || charRepeat == '.')){
                return matchHere(inputLine.substring(1), pattern.substring(2)) ||
                       matchHere(inputLine.substring(1), pattern);
            }
            return false;
        }
        
        // BASE CASE 2: If the pattern is not empty, But the text is.
        if(inputLine.isEmpty()){
            return false;
        }
        
        // Handle character groups like [abc] or [^abc]
        if(pattern.startsWith("[")) {
            int closingBracketIndex = pattern.indexOf(']');
            if(closingBracketIndex > 0){
                boolean isNegative = pattern.charAt(1) == '^';
                String groupChars = isNegative ?
                                    pattern.substring(2, closingBracketIndex)
                                    : pattern.substring(1, closingBracketIndex);
                
                char firstCharOfInputLine = inputLine.charAt(0);
                boolean charInGroup = groupChars.indexOf(firstCharOfInputLine) != -1;
                
                if(isNegative != charInGroup) {
                    return matchHere(inputLine.substring(1), pattern.substring(closingBracketIndex + 1));
                }
            }
        }
        
        // Handle \d - match any digit
        else if(pattern.startsWith("\\d")) {
            if(Character.isDigit(inputLine.charAt(0))) {
                return matchHere(inputLine.substring(1), pattern.substring(2));
            }
        }
        
        // Handle \w - match any alphanumeric character (plus _)
        else if(pattern.startsWith("\\w")) {
            char c = inputLine.charAt(0);
            if(Character.isLetterOrDigit(c) || c == '_') {
                return matchHere(inputLine.substring(1), pattern.substring(2));
            }
        }
        
        // Handle '.' - match any single character;
        else if (pattern.startsWith(".")){
            return matchHere(inputLine.substring(1), pattern.substring(1));
        }
        
        // Handle a literal character match (the default case)
        else if(pattern.charAt(0) == inputLine.charAt(0)) {
            return matchHere(inputLine.substring(1), pattern.substring(1));
        }
        
        return false;
    }
}

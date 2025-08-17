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
        
        // Uncomment this block to pass the first stage
        
        if (matchPattern(inputLine, pattern)) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }
    
    public static boolean matchPattern(String inputLine, String pattern) {
        if (pattern.length() == 1) {
            return inputLine.contains(pattern);
        } else if (pattern.contains("\\d")) {
            return inputLine.chars().anyMatch(Character::isDigit);
        } else if (pattern.contains("\\w")) {
            return checkIsAlphaNumeric(inputLine);
        } else if (pattern.contains("[")) { // Positive Sequence Group
            return checkPositiveSequenceGroup(pattern, inputLine);
        }
        else {
            throw new RuntimeException("Unhandled pattern: " + pattern);
        }
    }
    
    private static boolean checkIsAlphaNumeric(String inputLine) {
        for (char c : inputLine.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_') {
                return true;
            }
        }
        return false;
    }
    
    private static boolean checkPositiveSequenceGroup(String pattern, String inputLine) {
        int len =  pattern.length();
        String patternWithoutBracket = pattern.substring(1, len - 1);
        Set<Character> set = new HashSet<>();
        
        for(char c : patternWithoutBracket.toCharArray()) {
            set.add(c);
        }
        
        for(char c : inputLine.toCharArray()) {
            if(set.contains(c)) {
                return true;
            }
        }
        return false;
    }
}

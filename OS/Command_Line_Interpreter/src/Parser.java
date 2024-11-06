import java.util.Arrays;

class Parser {
String commandName;
String[] Args;
public boolean parse(String input) {
        String[] inputSplit = input.split(" ") ;
        commandName = inputSplit[0]; 
        String[] newArray = new String[inputSplit.length - 1];

        // we can replace this with 
        // Args = Arrays.copyOfRange(inputSplit, 1, inputSplit.length);
        // but i like it better this way
        for (int i = 1; i < inputSplit.length; i++) {
            newArray[i-1] = inputSplit[i] ; 
        }
        Args = Arrays.copyOf(newArray,newArray.length);

        if ((commandName.equals("pwd") || 
        commandName.equals("help")) && 
        Args.length != 0) {
            System.out.println("ERROR: " + commandName + " do not take any argumnents");
            return false;
        }

        if ((commandName.equals("mkdir") ||
        commandName.equals("rmdir") ||
        commandName.equals("rm") ||
        commandName.equals("touch") ||
        commandName.equals("cat")) && 
        Args.length == 0) {
            System.out.println("ERROR: " + commandName + " should take at least 1 argument");
            return false;
        }

        if ((commandName.equals("rmdir") ||
        commandName.equals("touch") ||
        commandName.equals("rm") ) && 
        Args.length != 1) {
            System.out.println("ERROR: " + commandName + " should take only one argument");
            return false;
        }
        return true ; 
    }
    public String getCommandName() { return commandName; }
    public String[] getArgs() { return Args; }
}

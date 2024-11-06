import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.Scanner;

class Terminal {
    Parser parser;
    File currentPath = new File(System.getProperty("user.dir"));

    public void chooseCommandAction(String command,String [] args) throws IOException {
        switch(command) {
            case "pwd":
                System.out.println(pwd());
                break; 
            case "cd":
                cd(args);
                break; 
            case "ls":
                System.out.println(ls(args));
                break;
            // case "ls -r":
            //     System.out.println(ls(args));
            //     break; 
            // case "ls -a":
            //     System.out.println(ls(args)); //new String[]{"-a"}
            //     break;
            case "mkdir":
                mkdir(args);
                break; 
            case "rmdir":
                rmdir(args);
                break; 
            case "touch":
                touch(args);
                break;
            case "rm":
                rm(args[0]);
                break;
            case "mv":
                mv(args);
                break;
            case "cat":
                cat(args);
                break; 
            case "help":
                help();
                break; 
            default:
                System.out.println("Unknown command, Type 'help' to see available commands.");
                break; 
        }
    }

    public String pwd() {
        return currentPath.getAbsolutePath();
    }

    public void cd(String[] args) {
        if (args.length == 0) {
            // case 1: go to the user's home directory if no arguments are provided
            currentPath = new File(System.getProperty("user.home"));
        } 
        else if (args.length == 1 && args[0].equals("..")) {
            // case 2: handle "cd .." to go to the parent directory
            File parent = currentPath.getParentFile();
            if (parent != null) {
                currentPath = parent;
            } 
            else {
                System.out.println("ERROR: Already at the root directory.");
            }
        } 
        else {
            // case 3: check if the path is absolute or relative
            File newPath;
            if (new File(args[0]).isAbsolute()) {
                // If the path is absolute use it directly
                newPath = new File(args[0]);
            } 
            else {
                // otherwise treat it as relative
                newPath = new File(currentPath, args[0]);
            }
    
            // check if the new path exists and is a directory
            if (newPath.exists() && newPath.isDirectory()) {
                currentPath = newPath.getAbsoluteFile();
            } 
            else {
                System.out.println("ERROR: The directory does not exist.");
            }
        }
    }
    

    public String ls(String [] args) {
        String [] contents = currentPath.list();
        StringBuilder text = new StringBuilder();
        //flags for listing behavior
        boolean showHidden = Arrays.asList(args).contains("-a");
        boolean reverseOrder = Arrays.asList(args).contains("-r");
        //handle the case where the directory is empty or if there is an issue while accessing the directory
        if (contents == null || contents.length == 0) {
            return "No files or directories found.";
        }
        //now we sort contents in alphabetical order by default
        Arrays.sort(contents);
        //reverse the order if the "-r" flag is set
        if (reverseOrder) {
            for (int i = contents.length - 1; i >= 0; i--) {
                //show hidden files if "-a" is set
                if (showHidden || !new File(currentPath, contents[i]).isHidden()) {
                    text.append(contents[i]).append("\n");
                }
            }
        } 
        else {
            for (String content : contents) {
                //Show hidden files if "-a" is set
                if (showHidden || !new File(currentPath, content).isHidden()) {  
                    text.append(content).append("\n");
                }
            }
        }
        return text.toString().trim(); 
    }

    public void mkdir(String [] args) {
        if (args.length == 1 && args[0].charAt(1) == ':' ) {
            File theDir = new File(args[0]);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
        }
        else {
            for (int i = 0; i < args.length; i++) {
                File theDir = new File(currentPath, args[i]);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
            }
        }
    }

    public void rmdir(String [] args) {
        String dir = args[0];
        try {
            if (dir.equals("*")) {
                File theDir = currentPath;
                File[] tmp = theDir.listFiles();
                for (int i = 0; i < tmp.length; i++) {
                    File file = tmp[i];
                    if (!file.isFile()) {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } 
            else {
                File theDir;
                if (dir.contains(":")) {
                    theDir = new File(dir);
                } 
                else {
                    theDir = new File(System.getProperty("user.dir") + "\\" + dir);
                }
                if (theDir.listFiles().length == 0) {
                    theDir.delete();
                }
            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void touch(String [] args) {
        File file;
        if (args[0].contains(":")) {
            file = new File(args[0]);
        } 
        else {
            file = new File(currentPath + "\\" + args[0]);
        }
        try {
            if (!new File(args[0]).exists())
                file.createNewFile();
        } 
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public void rm(String fileName) throws IOException, NoSuchFileException {
        File file;
        if (new File(fileName).isAbsolute()) {
            file = new File(fileName);
        } 
        else {
            file = new File(currentPath, fileName);
        }
        if(!file.exists())
            throw new NoSuchFileException(fileName, null, "no such file.");
        else if(file.isDirectory())
            throw new IOException("can not delete directory.");
        else if (!file.delete())
            throw  new IOException("can not delete file.");
    }

    public void mv(String[] args) {
        if (args.length != 2) {
            System.out.println("ERROR: mv command requires exactly 2 arguments.");
            return;
        }
    
        File sourceFile = new File(args[0]);
        if (!sourceFile.isAbsolute()) {
            sourceFile = new File(currentPath, args[0]);
        }
    
        File destinationFile = new File(args[1]);
        if (!destinationFile.isAbsolute()) {
            destinationFile = new File(currentPath, args[1]);
        }
    
        // if destination is a directory move the file into it
        if (destinationFile.isDirectory()) {
            destinationFile = new File(destinationFile, sourceFile.getName());
        }
    
        // attempt to move/rename the file
        if (sourceFile.exists() && sourceFile.renameTo(destinationFile)) {
            System.out.println("Done");
        } 
        else {
            System.out.println("ERROR: Unable to move/rename " + args[0] + " to " + args[1]);
        }
    }    

    public void cat(String[] args) {
        for (String arg : args) {
            File file = new File(arg);
            if (!file.isAbsolute()) {
                file = new File(currentPath, arg);
            }
            
            try (Scanner fileReader = new Scanner(file)) {
                if (file.exists() && file.isFile()) {
                    System.out.println("---- contents of " + file.getName() + " ----");
                    
                    StringBuilder text = new StringBuilder();
                    while (fileReader.hasNextLine()) {
                        text.append(fileReader.nextLine()).append('\n');
                    }
                    
                    if (text.length() > 0) {
                        System.out.println(text);
                    } 
                    else {
                        System.out.println("file is empty.");
                    }
                } 
                else {
                    System.out.println("ERROR: " + arg + " does not exist or is not a file.");
                }
            } 
            catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }    

    public void help() {
        System.out.println("pwd    -> return an absolute (full) path.");
        System.out.println("help   -> display all available commands to help you.");
        System.out.println("");
        System.out.println("mkdir  -> create one or more new directories.");
        System.out.println("        Usage: mkdir [directory_name] or mkdir [absolute_path]");
        System.out.println("        Example: mkdir folder_name");
        System.out.println("");
        System.out.println("cat    -> Displays the contents of one or more files.");
        System.out.println("        Usage: cat [file_name] [additional_file_names]...");
        System.out.println("        Example: cat file1.txt file2.txt");
        System.out.println("");
        System.out.println("mv     -> Moves or renames a file.");
        System.out.println("        Usage: mv [source_file] [destination]");
        System.out.println("        Example 1 (move file): mv file1.txt folder/");
        System.out.println("        Example 2 (rename file): mv oldname.txt newname.txt");
        System.out.println("");
        System.out.println("rmdir  -> Delete an empty directory.");
        System.out.println("        Usage: rmdir [directory_name] or rmdir * (to delete all empty directories in the current directory)");
        System.out.println("        Example: rmdir folder_name");
        System.out.println("");
        System.out.println("touch  -> Creates a new file.");
        System.out.println("        Usage: touch [file_name]");
        System.out.println("        Example: touch newfile.txt");
        System.out.println("");
        System.out.println("rm     -> Deletes a specified file.");
        System.out.println("        Usage: rm [file_name]");
        System.out.println("        Example: rm file_name.txt");
        System.out.println("");
        System.out.println("cd     -> Changes the current working directory.");
        System.out.println("        Usage: cd [path] or cd .. to move to the parent directory");
        System.out.println("        Example: cd folder_name or cd /path/to/directory");
        System.out.println("");
        System.out.println("ls     -> Lists the contents of the current directory.");
        System.out.println("        Usage: ls [-a] [-r]");
        System.out.println("        -a: Show hidden files.");
        System.out.println("        -r: Reverse the order of listing.");
        System.out.println("        Example: ls or ls -a or ls -r");
        System.out.println("");
        System.out.println("exit   -> Exits the terminal.");
    }
    
}
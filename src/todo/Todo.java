package todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Todo {
  List<TodoElement> todoList;
  String filePath;
  boolean fileIsReadable;

  public Todo(String filepath) {
    this.filePath = filepath;
    this.todoList = new ArrayList<>();
    this.fileIsReadable = false;
  }

  public static void main(String[] args) {
    System.out.println(args[0]);
    Todo myTodoList = new Todo("./csv/todo-data.csv");
    myTodoList.readFile();

    if (args.length == 0) {
      printUsage();
    } else if (myTodoList.fileIsReadable) {

      if (args[0].equals("-l")) {
        myTodoList.listTasks();
      } else if (args[0].equals("-a")) {
        myTodoList.addNewTask(args);
      } else if (args[0].equals("-r")) {
        myTodoList.modifyTask("remove", args);
      } else if (args[0].equals("-c")) {
        myTodoList.modifyTask("check", args);
      } else {
        System.out.println("Unsupported argument");
        printUsage();
      }
    }
  }

  private void modifyTask(String typeOfModification, String[] args) {
    String errorSout = "Unable to " + typeOfModification + ": ";
    if (args.length > 1) {
      if (isIndexNumber(args)) {
        int indexToCheck = indexNumber(args);
        if (indexToCheck > 0 && indexToCheck <= this.todoList.size()) {
          if (typeOfModification.equals("check")) {
            this.todoList.get(indexToCheck).isChecked = true;
          } else if (typeOfModification.equals("remove")) {
            this.todoList.get(indexToCheck).isChecked = true;
          }
        } else {
          System.out.println(errorSout + "index is out of bound");
        }
      } else {
        System.out.println(errorSout + "index is not a number");
      }
    } else {
      System.out.println(errorSout + "no index provided");
    }
  }

  public static boolean isIndexNumber(String[] args) {
    boolean isIndexNumber;
    try {
      int temp = Integer.parseInt(args[1]);
      isIndexNumber = true;
    } catch (NumberFormatException e) {
      isIndexNumber = false;
    }
    return isIndexNumber;
  }

  public int indexNumber(String[] args) {
    if (isIndexNumber(args)) {
      return Integer.parseInt(args[1]);
    }
    return -1;
  }

  public void listTasks() {
    if (!this.todoList.isEmpty()) {
      for (int i = 1; i <= this.todoList.size(); i++) {
        char tempCheck = ' ';
        if (this.todoList.get(i - 1).isChecked) {
          tempCheck = 'x';
        }
        System.out.println(i + " - [" + tempCheck + "] " + this.todoList.get(i - 1).description);
      }
    } else {
      System.out.println("No todos for today! :)");
    }
  }

  public void addNewTask(String[] args) {
    if (args.length > 1) {
      this.todoList.add(new TodoElement(args[1]));
      this.writeFile();
      System.out.println("New task succesfully added:");
      this.listTasks();
    } else {
      System.out.println("Unable to add: no task provided");
      printUsage();
    }
  }

  public void writeFile() {
    Path path = Paths.get(this.filePath);
    List<String> toWrite = new ArrayList<>();
    for (TodoElement todoElement : this.todoList) {
      char tempChecked = ' ';
      if (todoElement.isChecked) {
        tempChecked = 'x';
      }
      toWrite.add(tempChecked + ";" + todoElement.description);
    }
    try {
      Files.write(path, toWrite);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readFile() {
    Path path = Paths.get(this.filePath);
    List<String> rawLines = new ArrayList<>();
    try {
      rawLines = Files.readAllLines(path);
      this.fileIsReadable = true;
      for (String rawLine : rawLines) {
        boolean tempCheck = false;
        if (rawLine.split(";", 2)[0].equals("x")) {
          tempCheck = true;
        }
        this.todoList.add(new TodoElement(rawLine.split(";", 2)[1], tempCheck));
      }
    } catch (IOException e) {
      fileIsReadable = false;
    }
  }

  public static void printUsage() {
    System.out.println("Command Line Todo application\n" +
        "=============================\n" +
        "\n" +
        "Command line arguments:\n" +
        "    -l   Lists all the tasks\n" +
        "    -a   Adds a new task\n" +
        "    -r   Removes an task\n" +
        "    -c   Completes an task");
  }
}

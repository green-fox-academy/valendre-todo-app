package todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Todo {
  List<String> todoList;
  String filePath;
  boolean fileIsReadable;

  public Todo(String filepath) {
    this.filePath = filepath;
    this.todoList = new ArrayList<>();
    this.fileIsReadable = false;
  }

  public static void main(String[] args) {

    Todo myTodoList = new Todo("./src/todo/csv/todo-data.csv");
    myTodoList.readFile();

    if (args.length == 0) {
      printUsage();
    } else if (myTodoList.fileIsReadable) {

      if (args[0].equals("-l")) {
        myTodoList.listTasks();
      } else if (args[0].equals("-a")) {
        myTodoList.addNewTask(args);
      } else if (args[0].equals(("-r"))) {
        myTodoList.removeTask(args);
      }
    }
  }

  private void removeTask(String[] args) {
    int indexToRemove = 0;
    boolean isIndexNumber;
    try {
      indexToRemove = Integer.parseInt(args[1]);
      isIndexNumber = true;
    } catch (NumberFormatException e) {
      isIndexNumber = false;
    }
    if (args.length > 1) {
      if (isIndexNumber) {
        if (indexToRemove > 0 && indexToRemove <= this.todoList.size()) {
          this.todoList.remove(indexToRemove - 1);
          this.writeFile();
        } else {
          System.out.println("Unable to remove: index is out of bound");
        }
      } else {
        System.out.println("Unable to remove: index is not a number");
      }
    } else {
      System.out.println("Unable to remove: no index provided");
    }
  }

  public void listTasks() {
    if (!this.todoList.isEmpty()) {
      for (int i = 1; i <= this.todoList.size(); i++) {
        System.out.println(i + " - " + this.todoList.get(i - 1));
      }
    } else {
      System.out.println("No todos for today! :)");
    }
  }

  public void addNewTask(String[] args) {
    if (args.length > 1) {
      this.todoList.add(args[1]);
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
    try {
      Files.write(path, this.todoList);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void readFile() {
    Path path = Paths.get(this.filePath);
    try {
      this.todoList = Files.readAllLines(path);
      this.fileIsReadable = true;
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

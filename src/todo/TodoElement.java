package todo;

public class TodoElement {
  boolean isChecked;
  String description;

  public TodoElement(String description) {
    this(description, false);
  }

  public TodoElement(String description, boolean isChecked) {
    this.isChecked = isChecked;
    this.description = description;
  }
}

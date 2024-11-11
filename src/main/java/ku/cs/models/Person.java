package ku.cs.models;

import java.util.Objects;

public class Person {
    private String firstName;
    private String lastName;
    private String position;

    // Constructor
    public Person(String firstName, String lastName, String position) {
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
        this.position = Objects.requireNonNull(position, "Position cannot be null");
    }

    // Getter and Setter
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = Objects.requireNonNull(position, "Position cannot be null");
    }

    // toString Method for displaying Person in ListView
    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + position + ")";
    }

    // equals Method to compare Person objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                position.equals(person.position);
    }

    // hashCode Method to ensure consistency with equals
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, position);
    }
}

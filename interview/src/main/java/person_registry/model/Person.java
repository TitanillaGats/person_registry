package person_registry.model;

import org.apache.commons.validator.routines.EmailValidator;
import person_registry.model.helper.PhoneNumberValidator;

/**
 * Person class represents a person with name, phone number and email address.
 * A person is identified by its unique name.
 * Person class allows storing of phone number and email address for each person.
 */
public class Person {
    private String name;
    private String phoneNumber;
    private String email;

    /**
     * Class constructor that creates a new Person object
     * with the given name, phone number and email address.
     *
     * A Person object is created anyway, but note that
     * invalid phone number or email address won't be stored.
     *
     * @param name the person's unique name
     * @param phoneNumber the person's phone number
     * @param email the person's email address
     */
    public Person(String name, String phoneNumber, String email){
        setName(name.trim());
        setPhoneNumber(phoneNumber.trim());
        setEmail(email.trim());
    }

    /**
     * Getter method that returns the name of the person.
     * @return the person's name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person to the provided name.
     * As the name of a person is the unique identifier, it cannot be changed after creation.
     *
     * @param name the person's name
     */
    private void setName(String name) {
        name = name.trim().replaceAll("\"", "");
        this.name = name;
    }

    /**
     * Getter method that returns the phone number of the person.
     * @return the person's number as a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the person's phone number to the provided one if appropriate.
     * If the new phone number is invalid, then the stored phone number is not changed.<\br></\br>
     * If the person's phone number has not been set yet, it is set to "INVALID"
     * in case the new phone number is considered invalid.
     * If the new phone number is identical with the stored phone number, then no change takes place.
     *
     * The phone number is validated and formatted with PhoneNumberValidator.
     *
     * @param phoneNumber the new phone number to be stored
     * @return whether a change in the person's phone number occurred
     *
     * @see person_registry.model.helper.PhoneNumberValidator
     */
    public boolean setPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.trim().replaceAll("\"", "");

        if (PhoneNumberValidator.isPhoneNumberValid(phoneNumber)) {
            phoneNumber = PhoneNumberValidator.formatPhoneNumber(phoneNumber);

            if(this.phoneNumber != null && this.phoneNumber.equalsIgnoreCase(phoneNumber)){
                return false;
            }
            this.phoneNumber = phoneNumber;
            return true;
        } else if (this.phoneNumber == null) {
            this.phoneNumber = "INVALID";
        } else {
            System.out.println("Person " + name + "'s phone number will not be modified as the given number is invalid");
        }

        return false;
    }

    /**
     * Getter method that returns the email address of the person.
     * @return the person's email as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the person's email address to the provided one if appropriate.
     * If the new email is invalid, then the stored email is not changed.
     * In case the person's email address has never been set, it is set to "INVALID"
     * if the new email is considered invalid.
     * If the new email is identical with the stored email address, then no change takes place.
     *
     * Apache-commons' EmailValidator decides whether an email is valid or not.
     *
     * @param email the new email address to be stored
     * @return whether a change in the person's phone number occurred
     */
    public boolean setEmail(String email) {
        email = email.trim().replaceAll("\"", "");

        if(EmailValidator.getInstance().isValid(email)) {
            if(this.email != null && this.email.equalsIgnoreCase(email)){
                return false;
            }
            this.email = email;
            return true;
        } else if (this.email == null) {
            this.email = "INVALID";
        } else {
            System.out.println("Person " + name + "'s email address will not be modified as the given email is invalid");
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Name: " + ((name == null)? "EMPTY" : name)  + '\n' +
                "Number: " + ((phoneNumber == null)? "EMPTY" : phoneNumber) + '\n' +
                "Email: " + ((email == null)? "EMPTY" : email) + '\n';
    }
}
package person_registry;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import person_registry.model.Person;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Business logic and GUI handler class.
 * Its main task is to handle user request via GUI and maintain the registry in the memory.
 */
public class Controller {
    @FXML
    public TextField registryFilePath;
    public TextArea processingOutput;
    public TextField nameTextField;
    public TextField numberTextField;
    public TextField emailTextField;

    String firstLine = "Name:Number,Email";
    String nameNumberSeparator = ":";
    String numberEmailSeparator = ",";

    Map<String, Person> personRegistry;

    /**
     * Class constructor that instantiates the registry in the memory.
     */
    public Controller() {
        personRegistry = new ConcurrentHashMap<>();
    }

    /* ************************************
     * Event handler methods
     * ************************************/

    /**
     * Handles user request to open and process a file, that contains a registry.
     * If no path is provided, an error message is printed.
     *
     * The file processing logic is on a new thread, that checks for interruption.
     *
     * @param actionEvent Not used.
     */
    public void handleProcessButtonAction(ActionEvent actionEvent) {
        String path;

        if(registryFilePath.getText() == null || (path = registryFilePath.getText()).trim().isEmpty()) {
            logException("No registry file path provided, can't process file.");
            return;
        }

        logInfo("Processing registry from '" + path + "'");

        String finalPath = path;
        new Thread(() -> interruptableFileProcess(finalPath)).start();
    }

    /**
     * Handles user request to save the registry to a file.
     * If no path is provided, an error message is printed.
     *
     * The saving logic is on a new thread, that checks for interruption.
     *
     * @param actionEvent Not used.
     */
    public void handleSaveButtonAction(ActionEvent actionEvent) {
        String path;

        if(registryFilePath.getText() == null || (path = registryFilePath.getText()).trim().isEmpty()) {
            logException("No registry file path provided, can't save registry to file.");
            return;
        }
        logInfo("Saving registry to file: '" + path + "'");

        String finalPath = path;
        new Thread(()-> interruptableFileSave(finalPath)).start();
    }

    /**
     * Handles user request to add or modify a person in registry.
     * If no name is provided, an error message is printed.
     *
     * If the person is in the registry based on its name, then it is considered a modification.
     * Otherwise, it adds the new person to the registry with the provided phone number and email, if valid.
     *
     * Invalid email or phone number will not have effect on the current registry.
     *
     * @param actionEvent Not used.
     */
    public void handleAddModifyPersonButtonAction(ActionEvent actionEvent) {
        String name = nameTextField.getText();

        if(name == null || (name = name.trim()).isEmpty()) {
            logException("Person name is not provided, can't modify/add a new person to the registry");
            return;
        }

        name = name.substring(0,1).toUpperCase() + name.substring(1);

        if(personRegistry.containsKey(name)) {
            if (modifyPersonInRegistry(name, numberTextField.getText(), emailTextField.getText())) {
                logInfo("Updated person in registry");
            } else {
                logInfo("No update is needed to the person in registry");
            }

            String finalName = name;
            Platform.runLater(() -> processingOutput.appendText(personRegistry.get(finalName).toString()));
        } else {
            logInfo("New person added to registry");

            Person person = new Person(name, numberTextField.getText(), emailTextField.getText());
            personRegistry.put(person.getName(), person);
            Platform.runLater(() -> processingOutput.appendText(person.toString()));
        }
    }

    /**
     * Handles user request to delete a person from registry.
     * If no name is provided, an error message is printed.
     *
     * If the person is not in the registry based on its name, then an error message is printed.
     * In this case, the registry is not effected.
     *
     * @param actionEvent Not used.
     */
    public void handleDeletePersonButtonAction(ActionEvent actionEvent) {
        String name = nameTextField.getText();

        if(name == null || (name = name.trim()).isEmpty()) {
            logException("Person name is not provided, can't delete anyone from registry");
            return;
        }

        name = name.substring(0,1).toUpperCase() + name.substring(1);

        if(personRegistry.get(name) != null) {
            logInfo("Person named '" + name + "' is deleted from registry");
            personRegistry.remove(name);
        } else {
            logException("Person named '" + name + "' is not in the registry");
        }
    }

    /* ************************************
     * File handler methods
     * ************************************/

    /**
     * Opens the file at the given path.
     * If the file can be opened, it tries to parse every non-empty line to a Person.
     * The only exception is the first non-empty line, that should look something like: "Name: Number, Email".
     * Each successfully parsed person is added to the registry.
     *
     * @param path the absolute path of the file to be processed
     */
    private void interruptableFileProcess(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if(line.toLowerCase().contains("name")) {
                        firstLine = line;
                        break;
                    } else {
                        logException("First non-empty line doesn't match expected.\n\tFirst line in file: " + line + "\n\tExpected structure: " + firstLine
                                + "\n\tFile can't be processed.");
                        return;
                    }
                }
            }

            declareSeparatorCharacters();

            while ((line = reader.readLine()) != null) {
                if(Thread.currentThread().isInterrupted()){
                    logException("Interruption occurred while processing registry file");
                    return;
                }

                if(line.isEmpty())
                    continue;

                Person person = processLine(line);
                if (person != null) {
                    personRegistry.put(person.getName(), person);
                    Platform.runLater(() -> processingOutput.appendText("\n" + person.toString()));
                } else {
                    logException("Could not create a Person object based on line '" + line + "'");
                }
            }
        } catch (IOException e) {
            logException("Exception occurred during processing the file");
        }
    }

    /**
     * Saves the registry from the memory to a file at the given path.
     * If the file exists, it will be overwritten, otherwise it's created.
     * The first line is the same as the one read from a file previously, or the default "Name: Number, Email".
     * Data about a person in the registry is written to the file as single line formatted according to the first line.
     *
     * @param path the absolute path of the file to save the registry to
     */
    private void interruptableFileSave(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(((firstLine == null) ? "" : firstLine) + "\n");

            for(Person person : personRegistry.values()) {
                if(Thread.currentThread().isInterrupted()) {
                    logException("Interruption occurred while saving registry to file, file will be deleted as its content may be broken");
                    new File(path).delete();
                    return;
                }

                writer.write(getPrintablePerson(person));
                writer.write("\n");
            }

            logInfo("Registry saved successfully.");
        } catch (IOException e) {
            logException("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            logException("Exception occurred: " + e.getMessage());
        }
    }

    /* ************************************
     * Helper methods
     * ************************************/

    /**
     * It processes the first non-empty line of a registry file and saves the two separator characters.
     * The default separator of name and number is ':'.
     * The default separator of number and email is ','.
     */
    void declareSeparatorCharacters(){
        String schema = firstLine.trim().toLowerCase();
        schema = schema.replaceFirst("name", "");
        nameNumberSeparator = schema.substring(0,1);
        schema = schema.trim().substring(1);
        schema = schema.replaceFirst("number", "");
        numberEmailSeparator = schema.substring(0,1);
    }

    /**
     * Modifies the person's data in the registry.
     * To make the change permanent, the user should save the registry to a file.
     *
     * @param name the person's name provided by the user
     * @param number the person's phone number provided by the user
     * @param email the person's email address provided by the user
     * @return whether the person's data has changed
     */
    boolean modifyPersonInRegistry(String name, String number, String email) {
        Person person = personRegistry.get(name);

        boolean isPersonModified = false;

        if(!number.isEmpty()) {
            isPersonModified = person.setPhoneNumber(number);
        }

        if(!email.isEmpty()) {
            isPersonModified |= person.setEmail(email);
        }

        return isPersonModified;
    }

    /**
     * Creates a new Person object based on the given registry file line.
     *
     * @param line the data line read from a file
     * @return The new Person object if the data line is properly formatted, otherwise null
     */
    Person processLine(String line){
        try {
            String name = line.split(nameNumberSeparator)[0];
            String number = line.split(nameNumberSeparator)[1].split(numberEmailSeparator)[0];
            String email = line.split(nameNumberSeparator)[1].split(numberEmailSeparator)[1];

            return new Person(name, number, email);
        } catch (Exception e) {
            logException("Exception occurred during processing line: '" + line + "'");
            return null;
        }
    }

    /**
     * Returns a registry file line as a string, that represents the given person.
     *
     * @param person Person object that needs to be written to a registry file
     * @return The person's data formatted according to the first line of the registry
     */
    String getPrintablePerson(Person person) {
        String phoneNumber = (person.getPhoneNumber().equalsIgnoreCase("invalid")) ? "" : person.getPhoneNumber();
        String email = (person.getEmail().equalsIgnoreCase("invalid")) ? "" : person.getEmail();

        return person.getName() +
                nameNumberSeparator +
                " " +
                phoneNumber +
                numberEmailSeparator +
                " " +
                email +
                "\n";
    }

    /* ************************************
     * Logger methods
     * ************************************/
    private void logException(String message) {
        Platform.runLater(() ->{
            System.out.println(message);
            processingOutput.appendText("\nEXCEPTION: " + message + " \n");
        });
    }

    private void logInfo(String message) {
        Platform.runLater(() ->{
            System.out.println(message);
            processingOutput.appendText("\nINFO: " + message + "\n");
        });
    }
}
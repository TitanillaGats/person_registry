package person_registry;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import person_registry.model.Person;
import person_registry.model.helper.PhoneNumberValidator;

public class ControllerTest {
    private Controller controller;

    private String name = "Name";
    private String number = "06301234567";
    private String email = "person@email.com";

    @Before
    public  void initialize() {
        controller = new Controller();
    }

    @Test
    public void separatorCharactersSetCorrectly(){
        controller.firstLine = "Name;Number|Email";

        controller.declareSeparatorCharacters();

        TestCase.assertEquals(";", controller.nameNumberSeparator);
        TestCase.assertEquals("|", controller.numberEmailSeparator);
    }

    @Test
    public void personLineProcessedCorrectly(){
        Person expected = new Person(name, number, email);

        String personLine = name + controller.nameNumberSeparator + number + controller.numberEmailSeparator + email;

        Person actual = controller.processLine(personLine);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void personObjectPrintedCorrectly(){
        Person person = new Person(name, number, email);

        String expected = name + controller.nameNumberSeparator + " " + PhoneNumberValidator.formatPhoneNumber(number) + controller.numberEmailSeparator + " " + email + "\n";

        String actual = controller.getPrintablePerson(person);

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void personObjectsNumberModifiedInRegistry(){
        String newNumber = "0620/1234567";
        controller.personRegistry.put(name, new Person(name, number, email));

        Person expected = new Person(name, newNumber, email);

        boolean isPersonModified = controller.modifyPersonInRegistry(name, newNumber, email);

        TestCase.assertEquals(expected, controller.personRegistry.get(name));
        TestCase.assertTrue(isPersonModified);
    }

    @Test
    public void personObjectsEmailModifiedInRegistry(){
        String newEmail = "mail@mail.com";
        controller.personRegistry.put(name, new Person(name, number, email));

        Person expected = new Person(name, number, newEmail);

        boolean isPersonModified = controller.modifyPersonInRegistry(name, number, newEmail);

        TestCase.assertEquals(expected, controller.personRegistry.get(name));
        TestCase.assertTrue(isPersonModified);
    }

    @Test
    public void personObjectsNumberNotModifiedIfIdenticalInRegistry(){
        String newNumber = number;
        controller.personRegistry.put(name, new Person(name, number, email));

        boolean actual = controller.modifyPersonInRegistry(name, newNumber, email);

        TestCase.assertFalse(actual);
    }

    @Test
    public void personObjectsEmailNotModifiedIfIdenticalInRegistry(){
        String newEmail = email;
        controller.personRegistry.put(name, new Person(name, number, email));

        boolean actual = controller.modifyPersonInRegistry(name, number, newEmail);

        TestCase.assertFalse(actual);
    }

    @Test
    public void personObjectsNumberNotModifiedIfNewValueIsInvalid(){
        String newNumber = number;
        controller.personRegistry.put(name, new Person(name, number, email));

        boolean actual = controller.modifyPersonInRegistry(name, newNumber, email);

        TestCase.assertFalse(actual);
    }

    @Test
    public void personObjectsEmailNotModifiedIfNewValueIsInvalid(){
        String newEmail = email;
        controller.personRegistry.put(name, new Person(name, number, email));

        boolean actual = controller.modifyPersonInRegistry(name, number, newEmail);

        TestCase.assertFalse(actual);
    }
}
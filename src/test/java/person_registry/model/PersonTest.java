package person_registry.model;

import junit.framework.TestCase;
import org.junit.Test;
import person_registry.model.helper.PhoneNumberValidator;

public class PersonTest {
    private String testName = "TestName";
    private String testPhone = "0630/1234567";
    private String testEmail = "test@email.com";

    @Test
    public void createPersonObjectOfValidData(){
        Person person = new Person(testName, testPhone, testEmail);

        TestCase.assertEquals(testName, person.getName());
        TestCase.assertEquals(PhoneNumberValidator.formatPhoneNumber(testPhone), person.getPhoneNumber());
        TestCase.assertEquals(testEmail, person.getEmail());
    }

    /* ***********************
     * Positive tests
     * ***********************/
    @Test
    public void apostropheRemovedFromName(){
        String name = "\"Name\"";

        Person person = new Person(name, testPhone, testEmail);

        TestCase.assertEquals("Name", person.getName());
    }

    /* ***********************
     * Negative tests
     * ***********************/
    @Test
    public void tooShortPhoneNumberIsNotSet(){
        String shortNumber = "0630123456";

        Person person = new Person(testName, shortNumber, testEmail);

        TestCase.assertEquals("INVALID", person.getPhoneNumber());
    }

    @Test
    public void tooLongPhoneNumberIsNotSet(){
        String longNumber = "063012345678";

        Person person = new Person(testName, longNumber, testEmail);

        TestCase.assertEquals("INVALID", person.getPhoneNumber());
    }

    @Test
    public void emailStartingWithDotIsNotSet(){
        String dotEmail = ".@barmi.hu";

        Person person = new Person(testName, testPhone, dotEmail);

        TestCase.assertEquals("INVALID", person.getEmail());
    }

    @Test
    public void emailContainingSpaceIsNotSet(){
        String spaceEmail = "space space@barmi.hu";

        Person person = new Person(testName, testPhone, spaceEmail);

        TestCase.assertEquals("INVALID", person.getEmail());
    }

    @Test
    public void emailWithInvalidDomainIsNotSet(){
        String invalidDomainEmail = "vki@barmi";

        Person person = new Person(testName, testPhone, invalidDomainEmail);

        TestCase.assertEquals("INVALID", person.getEmail());
    }

    /* ***********************
     * Update data tests
     * ***********************/
    @Test
    public void invalidPhoneSettingHasNoEffectOnPersonObject(){
        String invalidNumber = "0630123456";

        Person person = new Person(testName, testPhone, testEmail);
        person.setPhoneNumber(invalidNumber);

        TestCase.assertEquals(PhoneNumberValidator.formatPhoneNumber(testPhone), person.getPhoneNumber());
    }

    @Test
    public void invalidEmailSettingHasNoEffectOnPersonObject(){
        String invalidEmail = ".@email.com";

        Person person = new Person(testName, testPhone, testEmail);
        person.setEmail(invalidEmail);

        TestCase.assertEquals(testEmail, person.getEmail());
    }
}

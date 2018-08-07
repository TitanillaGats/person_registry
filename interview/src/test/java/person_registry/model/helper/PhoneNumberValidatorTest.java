package person_registry.model.helper;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class PhoneNumberValidatorTest {
    @Test
    public void validPhoneNumberIsRecognised(){
        ArrayList<String> validPhoneNumbers = new ArrayList<>();
        validPhoneNumbers.add("06301234567");
        validPhoneNumbers.add("0630123-4567");
        validPhoneNumbers.add("06(30)1234567");
        validPhoneNumbers.add("06(30)123-4567");
        validPhoneNumbers.add("06/301234567");
        validPhoneNumbers.add("06/30123-4567");
        validPhoneNumbers.add("0630/1234567");
        validPhoneNumbers.add("0630/123-4567");

        validPhoneNumbers.add("+36301234567");
        validPhoneNumbers.add("+3630123-4567");
        validPhoneNumbers.add("+36(30)1234567");
        validPhoneNumbers.add("+36(30)123-4567");
        validPhoneNumbers.add("+36/301234567");
        validPhoneNumbers.add("+36/30123-4567");
        validPhoneNumbers.add("+3630/1234567");
        validPhoneNumbers.add("+3630/123-4567");

        for(String validNumber : validPhoneNumbers){
            TestCase.assertTrue(PhoneNumberValidator.isPhoneNumberValid(validNumber));
        }
    }

    @Test
    public void validPhoneNumberFormattedProperly(){
        ArrayList<String> validPhoneNumbers = new ArrayList<>();
        validPhoneNumbers.add("06301234567");
        validPhoneNumbers.add("0630123-4567");
        validPhoneNumbers.add("06(30)1234567");
        validPhoneNumbers.add("06(30)123-4567");
        validPhoneNumbers.add("06/301234567");
        validPhoneNumbers.add("06/30123-4567");
        validPhoneNumbers.add("0630/1234567");
        validPhoneNumbers.add("0630/123-4567");

        String formattedPhoneNumber = "06(30)123-4567";

        for(String validNumber : validPhoneNumbers){
            TestCase.assertEquals(formattedPhoneNumber, PhoneNumberValidator.formatPhoneNumber(validNumber));
        }
    }

    @Test
    public void validPhoneNumberWithPlusFormattedProperly(){
        ArrayList<String> validPhoneNumbersWithPlus = new ArrayList<>();
        validPhoneNumbersWithPlus.add("+36301234567");
        validPhoneNumbersWithPlus.add("+3630123-4567");
        validPhoneNumbersWithPlus.add("+36(30)1234567");
        validPhoneNumbersWithPlus.add("+36(30)123-4567");
        validPhoneNumbersWithPlus.add("+36/301234567");
        validPhoneNumbersWithPlus.add("+36/30123-4567");
        validPhoneNumbersWithPlus.add("+3630/1234567");
        validPhoneNumbersWithPlus.add("+3630/123-4567");

        String formattedPhoneNumberWithPlus = "+36(30)123-4567";

        for(String validNumber : validPhoneNumbersWithPlus){
            TestCase.assertEquals(formattedPhoneNumberWithPlus, PhoneNumberValidator.formatPhoneNumber(validNumber));
        }
    }

    /* ***********************
     * Negative tests
     * ***********************/
    @Test
    public void tooShortPhoneNumberIsNotValid(){
        String shortNumber = "0630123456";

        TestCase.assertFalse(PhoneNumberValidator.isPhoneNumberValid(shortNumber));
    }

    @Test
    public void tooLongPhoneNumberIsNotValid(){
        String longNumber = "063012345678";

        TestCase.assertFalse(PhoneNumberValidator.isPhoneNumberValid(longNumber));
    }

    @Test
    public void tooShortPhoneNumberWithPlusIsNotValid(){
        String shortNumber = "+3630123456";

        TestCase.assertFalse(PhoneNumberValidator.isPhoneNumberValid(shortNumber));
    }

    @Test
    public void tooLongPhoneNumberWithPlusIsNotValid(){
        String longNumber = "+363012345678";

        TestCase.assertFalse(PhoneNumberValidator.isPhoneNumberValid(longNumber));
    }

    @Test
    public void tooShortPhoneNumberIsNotFormatted(){
        String shortNumber = "0630123456";

        TestCase.assertNull(PhoneNumberValidator.formatPhoneNumber(shortNumber));
    }

    @Test
    public void tooLongPhoneNumberIsNotFormatted(){
        String longNumber = "063012345678";

        TestCase.assertNull(PhoneNumberValidator.formatPhoneNumber(longNumber));
    }

    @Test
    public void tooShortPhoneNumberWithPlusIsNotFormatted(){
        String shortNumber = "+3630123456";

        TestCase.assertNull(PhoneNumberValidator.formatPhoneNumber(shortNumber));
    }

    @Test
    public void tooLongPhoneNumberWithPlusIsNotFormatted(){
        String longNumber = "+363012345678";

        TestCase.assertNull(PhoneNumberValidator.formatPhoneNumber(longNumber));
    }
}

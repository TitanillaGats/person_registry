package person_registry.model.helper;

/**
 * Helper class to validate phone numbers.
 */
public class PhoneNumberValidator {
    /**
     * String defining the unitary format of phone numbers.
     */
    private static final String PHONE_NUMBER_FORMAT = "%s(%s)%s-%s";

    /**
     * Regular expression to match valid phone numbers.
     */
    private static final String JUST_NUMBERS = "\\+?\\d{11}";

    /**
     * Decides whether the provided phone number is valid.
     * A valid phone number should have 11 digits and may start with a '+'.
     *
     * @param phoneNumber the phone number in question
     * @return Whether the phone number given as parameter is valid or not.
     *
     * @see PhoneNumberValidator#JUST_NUMBERS
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        phoneNumber = simplifyPhoneNumber(phoneNumber);

        return phoneNumber.matches(JUST_NUMBERS);
    }

    /**
     * Formats the given phone number according to the PHONE_NUMBER_FORMAT.
     *
     * @param phoneNumber the phone number to be formatted
     * @return The formatted phone number
     *
     * @see PhoneNumberValidator#PHONE_NUMBER_FORMAT
     */
    public static String formatPhoneNumber(String phoneNumber) {
        phoneNumber = simplifyPhoneNumber(phoneNumber);

        if(!isPhoneNumberValid(phoneNumber)){
            return null;
        }

        if(phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.substring(1);
            phoneNumber = doSubstitution(phoneNumber);
            return "+" + phoneNumber;
        } else {
            return doSubstitution(phoneNumber);
        }
    }

    /**
     * Substitutes a non-formatted phone number's digits to the required unitary format.
     *
     * @param phoneNumber the non-formatted phone number
     * @return the phone number formatted properly
     */
    private static String doSubstitution(String phoneNumber){
        String prefix;
        String supplier;
        String number_part1;
        String number_part2;

        prefix = phoneNumber.substring(0,2);
        supplier = phoneNumber.substring(2,4);
        number_part1 = phoneNumber.substring(4,7);
        number_part2 = phoneNumber.substring(7);

        return String.format(PHONE_NUMBER_FORMAT, prefix, supplier, number_part1, number_part2);
    }

    /**
     * Deletes any formatting characters from a phone number string.
     * Formatting characters are: '/', '-', '(', ')' and space.
     *
     * @param phoneNumber the phone number string that may contain formatting characters
     * @return the phone number as a string without the known formatting characters
     */
    private static String simplifyPhoneNumber(String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("/", "");
        phoneNumber = phoneNumber.replaceAll("-", "");
        phoneNumber = phoneNumber.replaceAll("\\(", "");
        phoneNumber = phoneNumber.replaceAll("\\)", "");
        phoneNumber = phoneNumber.replaceAll(" ", "");

        return phoneNumber;
    }
}

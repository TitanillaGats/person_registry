package person_registry.model.helper;

public class PhoneNumberValidator {
    private static final String PHONE_NUMBER_FORMAT = "%s(%s)%s-%s";

    private static final String JUST_NUMBERS = "\\+?\\d{11}";

    public static boolean isPhoneNumberValid(String phoneNumber) {
        phoneNumber = simplifyPhoneNumber(phoneNumber);

        return phoneNumber.matches(JUST_NUMBERS);
    }

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

    private static String simplifyPhoneNumber(String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("/", "");
        phoneNumber = phoneNumber.replaceAll("-", "");
        phoneNumber = phoneNumber.replaceAll("\\(", "");
        phoneNumber = phoneNumber.replaceAll("\\)", "");
        phoneNumber = phoneNumber.replaceAll(" ", "");

        return phoneNumber;
    }
}

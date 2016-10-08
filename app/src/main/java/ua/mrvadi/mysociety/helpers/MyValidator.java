package ua.mrvadi.mysociety.helpers;

import ua.mrvadi.mysociety.models.Admin;
import ua.mrvadi.mysociety.models.Contact;

/**
 * Created by mrvadi on 04.10.16.
 */
public class MyValidator {

    public static boolean isCorrect(String login, String pass) {
        Admin correct = DBHelper.getInstance().getAdminInfo();
        if (login.toLowerCase().equals(correct.getLogin().toLowerCase())
        && pass.equals(correct.getPassword().toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public static String displayName(Contact contact) {
        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();

        boolean firstNameEmpty = firstName.replaceAll(" ", "").isEmpty();
        boolean lastNameEmpty = lastName.replaceAll(" ", "").isEmpty();



        if (!firstNameEmpty && !lastNameEmpty) {
            return normalize(firstName + " " + lastName);
        }   else if (!firstNameEmpty) {
            return normalize(firstName);
        } else if (!lastNameEmpty) {
            return normalize(lastName);
        } else {
            return contact.getPhoneNumber();
        }
    }

    private static String normalize(String oldString) {
        String[] words = oldString.split("\\s+");
        String finalString = "";

        for (String word : words) {
            if(!word.isEmpty() )
              finalString = finalString + " " + word;
        }
        if (finalString.length() > 0)
            finalString = finalString.substring(1, finalString.length());

        return finalString;
    }
}


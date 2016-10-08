package ua.mrvadi.mysociety.models;

/**
 * Created by mrvadi on 03.10.16.
 */
public class Admin {

    public static final int DEFAULT_ID = 111;
    public static final String DEFAULT_LOGIN = "admin";
    public static final String DEFAULT_PASSWORD = "123456";
    public static final String DEFAULT_PHONE_NUM = "+380951718501";
    public static final String DEFAULT_LINKED_IN = "https://ua.linkedin.com/in/mrvadi";
    public static final String DEFAULT_EMAIL = "vadonchik123@gmail.com";

    int id;
    String login;
    String password;
    String firstName;
    String lastName;
    String dateOfBirth;
    String phoneNumber;
    byte[] photo;
    String info;
    String linkedIn;
    String email;
    int logined;

    public Admin(String login, String password, String firstName,
                 String lastName, String dateOfBirth, String phoneNumber,
                 byte[] photo, String info, String linkedIn, String email, int logined) {

        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.info = info;
        this.linkedIn = linkedIn;
        this.email = email;
        this.logined = logined;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int isLogined() {
        return logined;
    }

    public void setLogined(int logined) {
        this.logined = logined;
    }
}

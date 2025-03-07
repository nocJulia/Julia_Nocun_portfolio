package pl.lodz.coordinationsystem.notification.model;

public class Victim {
    private Long id;
    private String firstName;
    private String lastName;
    private Character gender;
    private Integer age;
    private String phoneNumber;
    private String email;
    private String bloodType;
    private String medicalCondition;
    private String note;

    public Victim(Long id, String firstName, String lastName, Character gender,
                  Integer age, String phoneNumber, String email, String bloodType,
                  String medicalCondition, String note) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bloodType = bloodType;
        this.medicalCondition = medicalCondition;
        this.note = note;
    }

    public Victim(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Victim() {
    }

    public Long getId() {
        return id;
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

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Victim{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", medicalCondition='" + medicalCondition + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

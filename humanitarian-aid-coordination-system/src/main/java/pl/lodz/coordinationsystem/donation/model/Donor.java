package pl.lodz.coordinationsystem.donation.model;

import pl.lodz.coordinationsystem.security.model.Role;
import pl.lodz.coordinationsystem.security.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class Donor extends User {
    private String organizationName;
    private String donationHistory;
    private String personalId;
    private Character gender;
    private LocalDateTime birthDate;
    private String phoneNumber;
    private List<Donation> donations;

    public Donor() {
        setActive(true);
    }

    public Donor(Long id, String firstName, String lastName, String email, String password, Role role, Boolean active,
                 LocalDateTime createdAt, String organizationName, String donationHistory, String personalId,
                 Character gender, LocalDateTime birthDate, String phoneNumber, List<Donation> donations) {
        super(id, firstName, lastName, email, password, role, active, createdAt);
        this.organizationName = organizationName;
        this.donationHistory = donationHistory;
        this.personalId = personalId;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.donations = donations;
    }

    //TODO Update this constructor when the security module implements the minimal version of User constructor
    public Donor(String firstName, String lastName, String email, String password, Role role, Boolean active,
                 LocalDateTime createdAt) {
        super(firstName, lastName, email, password, role, active, createdAt);
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDonationHistory() {
        return donationHistory;
    }

    public void setDonationHistory(String donationHistory) {
        this.donationHistory = donationHistory;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}
package pl.lodz.coordinationsystem.donation.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.security.entity.BaseUserEntity;
import pl.lodz.coordinationsystem.security.entity.RoleEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "donors")
@DiscriminatorValue("DONOR")
public class DonorEntity extends BaseUserEntity {
    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "donation_history")
    private String donationHistory;

    @Column(name = "personal_id", length = 16)
    private String personalId;

    @Column(name = "gender", length = 1)
    private Character gender;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @OneToMany
    @JoinColumn(name = "donor_id")
    private List<DonationEntity> donations;

    public DonorEntity() {
    }

    public DonorEntity(Long id, String firstName, String lastName, String email, String password, Boolean active,
                       LocalDateTime createdAt, RoleEntity role, String organizationName, String donationHistory,
                       String personalId, Character gender, LocalDateTime birthDate, String phoneNumber) {
        super(id, firstName, lastName, email, password, active, createdAt, role);
        this.organizationName = organizationName;
        this.donationHistory = donationHistory;
        this.personalId = personalId;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
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

    public List<DonationEntity> getDonations() {
        return donations;
    }

    public void setDonations(List<DonationEntity> donations) {
        this.donations = donations;
    }
}

package pl.lodz.coordinationsystem.donation.entity;

import jakarta.persistence.*;
import pl.lodz.coordinationsystem.donation.model.DonationCurrency;
import pl.lodz.coordinationsystem.donation.model.DonationSize;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;
import pl.lodz.coordinationsystem.donation.model.DonationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class DonationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "donation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    @Column(name = "amount", nullable = false, precision = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private DonationCurrency currency;

    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private DonationSize size;

    @Column(name = "weight", precision = 2)
    private BigDecimal weight;

    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @Column(name = "donor_id", nullable = false)
    private Long donorId;

    public DonationEntity() {
    }

    public DonationEntity(Long id, String name, DonationType donationType,
                          BigDecimal amount, DonationCurrency currency, DonationSize size,
                          BigDecimal weight, LocalDateTime donationDate, DonationStatus status, Long donorId) {
        this.id = id;
        this.name = name;
        this.donationType = donationType;
        this.amount = amount;
        this.currency = currency;
        this.size = size;
        this.weight = weight;
        this.donationDate = donationDate;
        this.status = status;
        this.donorId = donorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DonationType getDonationType() {
        return donationType;
    }

    public void setDonationType(DonationType donationType) {
        this.donationType = donationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DonationCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(DonationCurrency currency) {
        this.currency = currency;
    }

    public DonationSize getSize() {
        return size;
    }

    public void setSize(DonationSize size) {
        this.size = size;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
}

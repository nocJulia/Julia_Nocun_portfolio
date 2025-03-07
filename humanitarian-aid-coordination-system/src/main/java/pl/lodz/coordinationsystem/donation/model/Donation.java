package pl.lodz.coordinationsystem.donation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Donation {
    private Long id;
    private String name;
    private DonationType donationType;
    private BigDecimal amount;
    private DonationCurrency currency;
    private DonationSize size;
    private BigDecimal weight;
    private LocalDateTime donationDate;
    private DonationStatus status;
    private Long donorId;

    public Donation() {}

    public Donation(Long id, String name, DonationType donationType, BigDecimal amount, DonationCurrency currency,
                    DonationSize size, BigDecimal weight, LocalDateTime donationDate, DonationStatus status,
                    Long donorId) {
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

    public Donation(String name, DonationType donationType, BigDecimal amount, DonationCurrency currency,
                    DonationSize size, LocalDateTime donationDate, DonationStatus status, Long donorId) {
        this.name = name;
        this.donationType = donationType;
        this.amount = amount;
        this.currency = currency;
        this.size = size;
        this.donationDate = donationDate;
        this.status = status;
        this.donorId = donorId;
    }

    public Long getId() {
        return id;
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
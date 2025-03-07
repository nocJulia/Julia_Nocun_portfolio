package pl.lodz.coordinationsystem.donation.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.donation.entity.DonationEntity;
import pl.lodz.coordinationsystem.donation.model.Donation;

public class DonationMapper {
    public static Donation toModel(@NonNull DonationEntity entity) {
        return new Donation(entity.getId(), entity.getName(), entity.getDonationType(), entity.getAmount(),
                entity.getCurrency(), entity.getSize(), entity.getWeight(), entity.getDonationDate(),
                entity.getStatus(), entity.getDonorId()
        );
    }

    public static DonationEntity toEntity(@NonNull Donation model) {
        return new DonationEntity(model.getId(), model.getName(), model.getDonationType(), model.getAmount(),
                model.getCurrency(), model.getSize(), model.getWeight(), model.getDonationDate(), model.getStatus(),
                model.getDonorId());
    }
}
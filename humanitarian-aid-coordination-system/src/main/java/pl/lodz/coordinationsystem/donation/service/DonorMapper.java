package pl.lodz.coordinationsystem.donation.service;

import io.micrometer.common.lang.NonNull;
import pl.lodz.coordinationsystem.donation.entity.DonorEntity;
import pl.lodz.coordinationsystem.donation.model.Donor;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.security.services.RoleMapper;

import java.util.List;

public class DonorMapper {
    public static Donor toModel(@NonNull DonorEntity entity) {
        List<Donation> donations = entity.getDonations().stream()
                .map(DonationMapper::toModel)
                .toList();

        return new Donor(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getPassword(), RoleMapper.toModel(entity.getRole()), entity.getActive(), entity.getCreatedAt(), entity.getOrganizationName(),
                entity.getDonationHistory(), entity.getPersonalId(), entity.getGender(), entity.getBirthDate(),
                entity.getPhoneNumber(), donations);
    }

    public static DonorEntity toEntity(@NonNull Donor model) {
        return new DonorEntity(model.getId(), model.getFirstName(), model.getLastName(), model.getEmail(),
                model.getPassword(), model.getActive(), model.getCreatedAt(), RoleMapper.toEntity(model.getRole()),
                model.getOrganizationName(), model.getDonationHistory(), model.getPersonalId(), model.getGender(),
                model.getBirthDate(), model.getPhoneNumber());
    }
}
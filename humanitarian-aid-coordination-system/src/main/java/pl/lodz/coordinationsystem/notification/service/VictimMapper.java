package pl.lodz.coordinationsystem.notification.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.notification.entity.VictimEntity;
import pl.lodz.coordinationsystem.notification.model.Victim;

public class VictimMapper {
    public static VictimEntity toEntity(@NonNull Victim victim) {
        return new VictimEntity(victim.getId(), victim.getFirstName(), victim.getLastName(),
                victim.getGender(), victim.getAge(), victim.getPhoneNumber(), victim.getEmail(),
                victim.getBloodType(), victim.getMedicalCondition(), victim.getNote());
    }
    
    public static Victim toModel(@NonNull VictimEntity victimEntity) {
        return new Victim(victimEntity.getId(), victimEntity.getFirstName(), victimEntity.getLastName(),
                victimEntity.getGender(), victimEntity.getAge(), victimEntity.getPhoneNumber(), victimEntity.getEmail(),
                victimEntity.getBloodType(), victimEntity.getMedicalCondition(), victimEntity.getNote());
    }
}

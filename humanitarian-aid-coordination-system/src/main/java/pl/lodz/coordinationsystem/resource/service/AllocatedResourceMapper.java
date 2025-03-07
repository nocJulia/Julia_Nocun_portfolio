package pl.lodz.coordinationsystem.resource.service;

import org.springframework.lang.NonNull;
import pl.lodz.coordinationsystem.donation.service.DonationMapper;
import pl.lodz.coordinationsystem.notification.service.NotificationMapper;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.resource.entity.AllocatedResourceEntity;

public class AllocatedResourceMapper {
    public static AllocatedResourceEntity toEntity(@NonNull AllocatedResource allocatedResource) {
        return new AllocatedResourceEntity(
                DonationMapper.toEntity(allocatedResource.getDonation()),
                NotificationMapper.toEntity(allocatedResource.getNotification()),
                allocatedResource.getQuantity()
                ,TransportMapper.toEntity(allocatedResource.getTransport()));

    }

    public static AllocatedResource toModel(@NonNull AllocatedResourceEntity allocatedResourceEntity) {

        return new AllocatedResource(DonationMapper.toModel(allocatedResourceEntity.getDonation()),
                NotificationMapper.toModelForResources(allocatedResourceEntity.getNotification()),
                allocatedResourceEntity.getQuantity(),
                TransportMapper.toModel(allocatedResourceEntity.getTransportEntity()));
    }
}

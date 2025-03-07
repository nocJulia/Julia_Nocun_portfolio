package pl.lodz.coordinationsystem.resource.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.donation.model.Donation;
import pl.lodz.coordinationsystem.donation.model.DonationStatus;
import pl.lodz.coordinationsystem.donation.service.DonationMapper;
import pl.lodz.coordinationsystem.donation.service.DonationService;
import pl.lodz.coordinationsystem.maps.model.Location;
import pl.lodz.coordinationsystem.maps.model.LocationType;
import pl.lodz.coordinationsystem.maps.services.LocationMapper;
import pl.lodz.coordinationsystem.maps.services.MapsService;
import pl.lodz.coordinationsystem.notification.entity.NotificationEntity;
import pl.lodz.coordinationsystem.notification.model.Notification;
import pl.lodz.coordinationsystem.notification.service.NotificationMapper;
import pl.lodz.coordinationsystem.resource.entity.AllocatedResourceEntity;
import pl.lodz.coordinationsystem.resource.entity.LogisticianEntity;
import pl.lodz.coordinationsystem.resource.entity.TransportEntity;
import pl.lodz.coordinationsystem.resource.entity.VehicleEntity;
import pl.lodz.coordinationsystem.resource.model.AllocatedResource;
import pl.lodz.coordinationsystem.resource.model.Logistician;
import pl.lodz.coordinationsystem.resource.model.Vehicle;
import pl.lodz.coordinationsystem.resource.repository.AllocatedResourceRepository;
import pl.lodz.coordinationsystem.resource.repository.LogisticianRepository;
import pl.lodz.coordinationsystem.resource.repository.TransportRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class ResourcesService implements IResources {
    @Autowired
    private AllocatedResourceRepository allocatedResourceRepository;

    @Autowired
    private TransportRepository transportRepository;
    @Autowired
    private LogisticianRepository logisticianRepository;
    @Autowired
    private DonationService donationService;

    @Autowired
    private MapsService mapsService;

    @Transactional
    @Override
    public Long allocateResource(Donation donation, Notification notification, Integer quantity, VehicleEntity vehicle) {
        if (donation.getStatus() != DonationStatus.PENDING) {
            throw new IllegalArgumentException("Donation must be in PENDING status to be allocated.");
        }
        donationService.updateDonationStatusById(donation.getId(), DonationStatus.ASSIGNED);
        TransportEntity transportEntity = new TransportEntity(
                LocalDateTime.now(), null, vehicle, null);
        transportRepository.save(transportEntity);
        var allocatedResourceEntity = new AllocatedResourceEntity(DonationMapper.toEntity(donation),
                NotificationMapper.toEntity(notification), quantity, transportEntity);
        transportEntity.setAllocatedResourceEntity(allocatedResourceEntity);

        return allocatedResourceRepository.save(allocatedResourceEntity).getId();
    }

    @Override
    public List<AllocatedResource> getAllAllocatedResources() {
        Iterable<AllocatedResourceEntity> entities = allocatedResourceRepository.findAll();
        return StreamSupport.stream(entities.spliterator(), false)
                .map(AllocatedResourceMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public void setLocationTologistician(Long id, Double latitude, Double longitude) {
        Optional<LogisticianEntity> logisticianEntityOptional = logisticianRepository.findById(id);
        logisticianEntityOptional.ifPresent(logisticianEntity -> {
            if (logisticianEntity.getLocation() != null) {
                mapsService.deleteLocation(LocationMapper.toModel(logisticianEntity.getLocation()));
            }
            Long location_id = mapsService.saveLocation(new Location(latitude, longitude, "Location of logistician with id: " + id, LocationType.RESOURCE));
            logisticianEntity.setLocation(LocationMapper.toEntity(mapsService.getLocationById(location_id).orElseThrow()));
            logisticianRepository.save(logisticianEntity);
        });
    }

    @Override
    public Optional<AllocatedResource> getAllocatedResourceById(Long id) {
        return allocatedResourceRepository.findById(id).map(AllocatedResourceMapper::toModel);
    }

    @Override
    @Transactional
    public void removeAllocatedResourceById(Long id) {
        allocatedResourceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long createLogistician(Logistician logistician) {
        var logisticianEntity = LogisticianMapper.toEntity(logistician);
        return logisticianRepository.save(logisticianEntity).getId();
    }


    @Override
    public List<Logistician> getAllLogisticians() {
        Iterable<LogisticianEntity> entities = logisticianRepository.findAll();
        return StreamSupport.stream(entities.spliterator(), false)
                .map(LogisticianMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Logistician> getLogisticianById(Long id) {
        return logisticianRepository.findById(id).map(LogisticianMapper::toModel);
    }

    @Override
    @Transactional
    public void removeLogisticianById(Long id) {
        logisticianRepository.deleteById(id);
    }
}


package pl.lodz.coordinationsystem.resource.service;

import org.springframework.stereotype.Service;
import pl.lodz.coordinationsystem.resource.entity.TransportEntity;
import pl.lodz.coordinationsystem.resource.repository.TransportRepository;

import java.util.List;

@Service
public class TransportService {

    private final TransportRepository transportRepository;

    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<TransportEntity> getAllTransportsRunning(){
        return transportRepository.getAllTransportsRunning();
    }
    public void finishTransport(Long id){
        transportRepository.setEndDateToNowIfNotSet(id);
    }
    public TransportEntity findById(Long id){
       return transportRepository.findById(id).get();
    }
}

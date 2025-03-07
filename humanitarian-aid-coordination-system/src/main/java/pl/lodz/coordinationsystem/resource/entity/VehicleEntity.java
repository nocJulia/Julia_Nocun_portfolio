package pl.lodz.coordinationsystem.resource.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registry_number")
    private String registryNumber;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VehicleType vehicleType;

    @Column(name = "boot_capacity")
    private Integer bootCapacity;

    @OneToMany(mappedBy = "vehicle")
    private List<TransportEntity> transportList;

    @Column(name = "is_available")
    private boolean isAvailable;

    public VehicleEntity() {
    }

    public VehicleEntity(String registryNumber, String brand, String model, VehicleType vehicleType, Integer bootCapacity, boolean isAvailable) {
        this.registryNumber = registryNumber;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.bootCapacity = bootCapacity;
        this.isAvailable = isAvailable;
    }

    // Getters and setters
    public String getRegistryNumber() {
        return registryNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getBootCapacity() {
        return bootCapacity;
    }

    public void setBootCapacity(Integer bootCapacity) {
        this.bootCapacity = bootCapacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }


    public List<TransportEntity> getTransportList() {
        return transportList;
    }

    public void setTransportList(List<TransportEntity> transportList) {
        this.transportList = transportList;
    }
}

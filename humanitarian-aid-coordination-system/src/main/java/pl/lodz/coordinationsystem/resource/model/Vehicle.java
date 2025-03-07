package pl.lodz.coordinationsystem.resource.model;

import pl.lodz.coordinationsystem.resource.entity.VehicleType;

public class Vehicle {
    private Long id;
    private String registryNumber;
    private String brand;
    private String model;
    private VehicleType vehicleType;
    private Integer bootCapacity;
    private boolean isAvailable;

    public Long getId() {
        return id;
    }

    public Vehicle(String registryNumber, String brand, String model, VehicleType vehicleType, Integer bootCapacity, boolean isAvailable) {
        this.registryNumber = registryNumber;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.bootCapacity = bootCapacity;
        this.isAvailable = isAvailable;
    }

    public Vehicle() {
    }

    public String getRegistryNumber() {
        return registryNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Integer getBootCapacity() {
        return bootCapacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setBootCapacity(Integer bootCapacity) {
        this.bootCapacity = bootCapacity;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}

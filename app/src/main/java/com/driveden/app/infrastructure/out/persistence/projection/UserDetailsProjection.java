package com.driveden.app.infrastructure.out.persistence.projection;

public interface UserDetailsProjection {

        Long getVehicleId();

        String getUsername();
    
        String getNickname();
    
        String getBrand();
    
        String getModel();
    
        Integer getYear();

}

package com.voyage.service.google.map;

import java.util.List;


public class Geometry {

    private Location location;
    
    private List<String> types;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
    
}

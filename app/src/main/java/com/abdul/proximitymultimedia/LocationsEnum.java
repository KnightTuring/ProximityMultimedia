package com.abdul.proximitymultimedia;

public enum LocationsEnum {
    // 18.619166, 73.771446 -> Thergaon
    // 18.579125, 73.737141 -> Qubix
    LOCATION_1("18.619166"," 73.771446","location1"),
    LOCATION_2("18.579125","73.737141","location2");

    private final String lattitude;
    private final String longitude;
    private final String identifier;

    private LocationsEnum(String latt, String longit, String id) {
        this.lattitude = latt;
        this.longitude = longit;
        this.identifier = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLattitude() {
        return lattitude;
    }
}
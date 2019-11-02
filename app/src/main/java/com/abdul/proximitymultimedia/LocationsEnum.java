package com.abdul.proximitymultimedia;

public enum LocationsEnum {
    // "33.374315"," 74.295325","location1"
    // 18.619166, 73.771446 -> Thergaon
    // 18.579125, 73.737141 -> Qubix
    LOCATION_1("33.374315"," 74.295325","location1"),
    LOCATION_2("18.619153","73.771517","location2_empress"),
    LOCATION_3("18.616820", "73.770660","location3_greensend");

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
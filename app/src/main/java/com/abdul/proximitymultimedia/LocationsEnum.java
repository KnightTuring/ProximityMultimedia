package com.abdul.proximitymultimedia;

public enum LocationsEnum {
    // Home co-ordinates
    //LOCATION_1("18.6183308","73.771543","location1"),
    //LOCATION_2("19.615496","75.765218","location2");
    // Jammu co-ordinates
    // University of Jammu: 32.718269, 74.868504
    // Jammu station: 32.704066, 74.876604
    LOCATION_1("32.718269","74.868504","location1"),
    LOCATION_2("32.704066","74.876604","location2");

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
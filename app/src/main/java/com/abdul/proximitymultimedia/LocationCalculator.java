package com.abdul.proximitymultimedia;

import android.location.Location;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class LocationCalculator {

    double THREHOLD_DISTANCE_IN_METERS = 800;
    Map<String, Location> predefinedLocations;

    LocationCalculator() {
        predefinedLocations = new HashMap<>();
        // populate HashMap
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(LocationsEnum.LOCATION_1.getLattitude()));
        location.setLongitude(Double.parseDouble(LocationsEnum.LOCATION_1.getLongitude()));

        predefinedLocations.put(LocationsEnum.LOCATION_1.getIdentifier(), location);

        location = new Location("");
        location.setLatitude(Double.parseDouble(LocationsEnum.LOCATION_2.getLattitude()));
        location.setLongitude(Double.parseDouble(LocationsEnum.LOCATION_2.getLongitude()));
        predefinedLocations.put(LocationsEnum.LOCATION_2.getIdentifier(), location);

        location = new Location("");
        location.setLatitude(Double.parseDouble(LocationsEnum.LOCATION_3.getLattitude()));
        location.setLongitude(Double.parseDouble(LocationsEnum.LOCATION_3.getLongitude()));
        predefinedLocations.put(LocationsEnum.LOCATION_3.getIdentifier(), location);

        Log.i("tag","Predefined locations map set");
    }

    /**
     *
     * @return
     */
    public String getShortestDistance(Location currLocation) {

        Map<String, Double> distFromAllLocations = distanceFromAllPredefinedLocations(currLocation);

        String minValueLocationIdentifier = getMinValueFromMap(distFromAllLocations);

        return minValueLocationIdentifier;
    }

    /**
     *
     * @param distFromAllLocations
     * @return
     */
    private String getMinValueFromMap(Map<String, Double> distFromAllLocations) {
        String locationIdentifier = null;

        Map.Entry<String, Double> min = null;
        for(Map.Entry<String, Double> entry : distFromAllLocations.entrySet()) {
            if(min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }

        Log.i("Tag", "Minimum value from map is of: "+min.getKey());
        if(min.getValue() <= THREHOLD_DISTANCE_IN_METERS) {
            Log.i("Tag", "Minimum value is within threshold");
            locationIdentifier = min.getKey();
        }

        return locationIdentifier;
    }


    /**
     *
     * @param currLocation
     * @return
     */
    private Map<String, Double> distanceFromAllPredefinedLocations(Location currLocation) {
        Map<String, Double> distFromAllPreDefLocations = new HashMap<>();
        Log.i("Tag", "Current location is Latt: "+currLocation.getLatitude()+" Long: "+currLocation.getLongitude());
        for(Map.Entry<String, Location> entry: predefinedLocations.entrySet()) {
            Log.i("Tag", "Calculating distance of "+entry.getKey()+"Latt: "+entry.getValue().getLatitude()+"Long: "+entry.getValue().getLongitude()+" from current location");
            Location location = entry.getValue();
            double distance = calculateDistance(currLocation, location);
            distFromAllPreDefLocations.put(entry.getKey(), distance);
            Log.i("Tag", "Distance of "+entry.getKey()+" from current location is: "+Double.toString(distance));
        }
        Log.i("Tag","Finished calculating distances from all predefined locations");
        return distFromAllPreDefLocations;
    }
    /**
     *
     * @param locA
     * @param locB
     * @return
     */
    private double calculateDistance(Location locA, Location locB) {
        double distanceInMeters = locA.distanceTo(locB);
        return  distanceInMeters;
    }
}

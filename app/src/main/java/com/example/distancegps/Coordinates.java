package com.example.distancegps;

/**
 * This class keeps values of the coordinates and includes methods to calculate distances
 */
class Coordinates {
    private double longitude = 0;
    private double latitude = 0;
    private double altitude = 0;
    private float accuracy = 0;


    /**
     * Non-argument constructor
     */
    public Coordinates(){
        this.longitude = 0;
        this.latitude = 0;
        this.altitude = 0;
        this.accuracy = 0;
    }

    /**
     * Constructor
     *
     * @param latitude latitude in degrees
     * @param longitude longitude in degrees
     */
    public  Coordinates(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor
     *
     * @param latitude latitude in degrees
     * @param longitude longitude in degrees
     * @param altitude altitude in meters
     */
    public Coordinates(double latitude, double longitude, double altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }


    /**
     * Set method for longitude
     * @param longitude longitude in degrees
     */
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    /**
     * Get method for longitude
     * @return Returns longitude in degrees
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     * Set method for latitude
     * @param latitude latitude in degrees
     */
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    /**
     * Get method for latitude
     * @return Returns latitude in degrees
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * Set method for altitude
     * @param altitude Returns altitude in meters
     */
    public void setAltitude(double altitude){
        this.altitude = altitude;
    }

    /**
     * Get method for altitude
     * @return Returns altitude in meters
     */
    public double getAltitude(){
        return altitude;
    }

    /**
     * Set method for accuracy
     * @param accuracy accuracy in meters
     */
    public void setAccuracy(float accuracy){
        this.accuracy = accuracy;
    }

    /**
     * Get method for accuracy
     * @return Returns accuracy in meters
     */
    public float getAccuracy() {
        return accuracy;
    }


    /**
     * "This uses the ‘haversine’ formula to calculate the great-circle distance between two points
     * – that is, the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’
     * distance between the points (ignoring any hills they fly over, of course!)."
     * from http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param latitudeStart Start latitude in degrees
     * @param longitudeStart Start longitude in degrees
     * @param latitudeEnd End latitude in degrees
     * @param longitudeEnd End longitude in degrees
     * @return Returns distance between starting and ending points with haversine method in meters
     */
    public static double getDistanceHav(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(latitudeEnd - latitudeStart);  // deg2rad below
        double dLon = deg2rad(longitudeEnd - longitudeStart);
        double a = Math.pow(Math.sin(dLat / 2), 2) +  Math.cos(deg2rad(latitudeStart)) * Math.cos(deg2rad(latitudeEnd)) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c * 1000; // Distance in m
        return d;
    }


    /**
     * Converts degrees to radians
     * @param deg Degrees
     * @return Radians
     */
    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }


    /**
     * Calculates distance in flat 2D, works only with small distances
     *
     * @param latitudeStart Start latitude in degrees
     * @param longitudeStart Start longitude in degrees
     * @param latitudeEnd End latitude in degrees
     * @param longitudeEnd End longitude in degrees
     * @return Returns distance between starting and ending points in 2D-flat with Pythagoras method in meters
     */
    public static double getDistancePyth2D(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {
        int R = 6371; // Radius of the earth in km
        double x = deg2rad(longitudeEnd - longitudeStart) * Math.cos(deg2rad(latitudeEnd + latitudeStart) / 2);
        double y = deg2rad(latitudeEnd - latitudeStart);

        return Math.sqrt(x * x + y * y) * R * 1000;
    }



    /**
     * Calculates distance with case to 2D where is added altitude so we get 3D-distance, works only with small distances
     *
     * @param latitudeStart Start latitude in degrees
     * @param longitudeStart Start longitude in degrees
     * @param altitudeStart Start altitude in meters
     * @param latitudeEnd End latitude in degrees
     * @param longitudeEnd End longitude in degrees
     * @param altitudeEnd End altitude in meters
     * @return Returns distance between starting and ending points with altitudes in 3D with Pythagoras method in meters
     */
    public static double getDistancePyth3D(double latitudeStart, double longitudeStart, double altitudeStart,
                                           double latitudeEnd, double longitudeEnd, double altitudeEnd) {
        int R = 6371; // Radius of the earth in km
        double x = deg2rad(longitudeEnd - longitudeStart) * Math.cos(deg2rad(latitudeEnd + latitudeStart) / 2);
        double y = deg2rad(latitudeEnd - latitudeStart);
        double z = (altitudeEnd - altitudeStart) / 1000;

        return Math.sqrt(x * x * R * R + y * y * R * R + z * z) * 1000;
    }



    /**
     * Calculates tunnel distance in spherical coordinates
     *
     * @param latitudeStart Start latitude in degrees
     * @param longitudeStart Start longitude in degrees
     * @param latitudeEnd End latitude in degrees
     * @param longitudeEnd End longitude in degrees
     * @return Returns distance between starting and ending points with tunnel distance in meters. Method is in spherical coordinates
     */
    public static double getTunnelDistance(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {
        int R = 6371; // Radius of the earth in km
        double dx = Math.cos(deg2rad(latitudeEnd)) * Math.cos(deg2rad(longitudeEnd)) - Math.cos(deg2rad(latitudeStart)) * Math.cos(deg2rad(longitudeStart));
        double dy = Math.cos(deg2rad(latitudeEnd)) * Math.sin(deg2rad(longitudeEnd)) - Math.cos(deg2rad(latitudeStart)) * Math.sin(deg2rad(longitudeStart));
        double dz = Math.sin(deg2rad(latitudeEnd)) - Math.sin(deg2rad(latitudeStart));

        return Math.sqrt(dx * dx + dy * dy + dz * dz) * R * 1000;
    }
}

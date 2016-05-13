package ex5.it.mcm.fhooe.classroomplusplus.utils;

import ex5.it.mcm.fhooe.classroomplusplus.BuildConfig;

/**
 * Created by Tob0t on 04.05.2016.
 */
public final class Constants {
    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;

    /**
     * Constants for Firebase object locations
     */
    public static final String FIREBASE_LOCATION_ROOMS = "rooms";
    public static final String FIREBASE_LOCATION_LECTURES = "lectures";
    public static final String FIREBASE_LOCATION_VOTES = "votes";
    public static final String FIREBASE_LOCATION_SURVEYS = "surveys";

    /**
     * Properties for Firebase object properties
     */
    public static final String FIREBASE_PROPERTY_ROOMS_ROOM_NUMBER = "roomNumber";

    public static final String FIREBASE_PROPERTY_LECTURES_NAME = "name";
    public static final String FIREBASE_PROPERTY_LECTURES_START_TIME = "startTime";
    public static final String FIREBASE_PROPERTY_LECTURES_END_TIME = "endTime";

    public static final String FIREBASE_PROPERTY_VOTES_QUESTION = "question";
    public static final String FIREBASE_PROPERTY_VOTES_LEFT_ANSWER = "leftAnswer";
    public static final String FIREBASE_PROPERTY_VOTES_RIGHT_ANSWER = "rightAnswer";

    public static final String FIREBASE_PROPERTY_SURVEY_LEFT_ANSWER = "leftAnswer";
    public static final String FIREBASE_PROPERTY_SURVEY_RIGHT_ANSWER = "rightAnswer";
    public static final String FIREBASE_PROPERTY_SURVEY_TIMESTAMP_LAST_CHANGE = "timestampLastChange";

    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";

    /**
     * Constants for Firebase URLs
     */
    public static final String FIREBASE_URL_ROOMS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ROOMS;
    public static final String FIREBASE_URL_LECTURES = FIREBASE_URL + "/" + FIREBASE_LOCATION_LECTURES;
    public static final String FIREBASE_URL_VOTES = FIREBASE_URL + "/" + FIREBASE_LOCATION_VOTES;
    public static final String FIREBASE_URL_SURVEYS = FIREBASE_URL + "/" + FIREBASE_LOCATION_SURVEYS;

    /**
     * Constants for bundles, extras and shared preferences keys
     */
    public static final String KEY_SURVEY_ID = "SURVEY_ID";
    public static final String KEY_ROOM_ID = "ROOM_ID";

    /**
     * For Shared preferences
     */
    public static final String PREF_USERNAME = "username";
    public static final String PREF_EMAIL = "email";

    /**
     * Mock Data for Demo
     */
    public static final String MOCK_ROOM_ID = "L1";
    public static final String NFC_ID = "123456789";


    /**
     * Enums
     */
    public enum Answer{
        LEFT, RIGHT
    }

    public enum RoomStatus{
        FREE, OCCUPIED
    }

}

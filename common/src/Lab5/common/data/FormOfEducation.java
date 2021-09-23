package Lab5.common.data;

import java.io.Serializable;

/**
 * Enumeration with form of education constants
 */
public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION,
    FULL_TIME_EDUCATION,
    EVENING_CLASSES;

    /**
     * Generates a list of enum string values.
     * @return String with all enum values split by a comma
     */
    public static String nameList(){
        String nameList = "";

        for(FormOfEducation formOfEducation : values()){
            nameList += formOfEducation.name()+", ";
        }
        nameList=nameList.substring(0,nameList.length());
        nameList+=" \n ";

        return nameList;
    }
}



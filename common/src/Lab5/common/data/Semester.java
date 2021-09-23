package Lab5.common.data;

import java.io.Serializable;

/**
 * Enumeration with semester types
 */
public enum Semester implements Serializable {
    THIRD,
    FOURTH,
    SIXTH,
    SEVENTH;

    /**
     * Generates a list of enum string values
     * @return String with enum values split by a comma
     */
    public static String nameList(){
        String nameList = " \n   Semester: \n";

        for(Semester semesterType : values()){
            nameList += semesterType.name()+", ";
        }
        nameList=nameList.substring(0,nameList.length()-2);
        nameList+=" \n  ";

        return nameList;
    }

}


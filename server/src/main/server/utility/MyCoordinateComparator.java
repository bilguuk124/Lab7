package server.utility;

import Lab5.common.data.StudyGroup;

import java.util.Comparator;

public class MyCoordinateComparator implements Comparator<StudyGroup> {

    @Override
    public int compare(StudyGroup studyGroup1, StudyGroup studyGroup2){
        int first = (int) (studyGroup1.getCoordinates().getX() + studyGroup1.getCoordinates().getY());
        int second = (int) (studyGroup2.getCoordinates().getX() + studyGroup2.getCoordinates().getY());
        if(first > second){
            return 1;
        } else return -1;

    }
}

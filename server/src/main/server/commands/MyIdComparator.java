package server.commands;

import Lab5.common.data.StudyGroup;

import java.util.Comparator;

public class MyIdComparator implements Comparator<StudyGroup> {

    @Override
    public int compare(StudyGroup studyGroup1, StudyGroup studyGroup2){
        if(studyGroup1.getId()>studyGroup2.getId()){
            return 1;
        }
        else return -1;
    }
}

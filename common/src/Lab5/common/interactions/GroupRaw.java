package Lab5.common.interactions;

import Lab5.common.data.*;
import java.io.Serializable;

public class GroupRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private Long studentsCount;
    private int transferredStudents;
    private FormOfEducation formOfEducation;
    private Semester semester;
    private Person groupAdmin;

    public GroupRaw(String name,Coordinates coordinates, Long studentsCount, FormOfEducation formOfEducation,
                    int transferredStudents, Semester semester, Person groupAdmin){
        this.name   =   name;
        this.coordinates = coordinates;
        this.groupAdmin = groupAdmin;
        this.semester = semester;
        this.studentsCount = studentsCount;
        this.transferredStudents = transferredStudents;
        this.formOfEducation = formOfEducation;
    }

    public String getName(){
        return name;
    }
    public Coordinates getCoordinates(){
        return coordinates;
    }
    public Long getStudentsCount(){
        return  studentsCount;
    }
    public int getTransferredStudents(){
        return transferredStudents;
    }
    public Person getGroupAdmin(){
        return groupAdmin;
    }
    public Semester getSemester(){
        return semester;
    }
    public FormOfEducation getFormOfEducation(){
        return formOfEducation;
    }

    @Override
    public String toString(){
        String info = "";
        info += "Учебная группа";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates.getX() + " " +coordinates.getY();
        info += "\n Число студентов: " + studentsCount;
        info += "\n Число переведённых студентов: " + transferredStudents;
        info += "\n Номер семестра: " + semester;
        info += "\n Форма обучение: " + formOfEducation;
        info += "\n Админ группы: " + groupAdmin;
        return info;
    }

    @Override
    public int hashCode(){
        return name.hashCode() + coordinates.hashCode() + studentsCount.intValue() + transferredStudents + semester.hashCode() + formOfEducation.hashCode() + groupAdmin.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj) return true;
        if(obj instanceof StudyGroup){
            StudyGroup studyGroup = (StudyGroup) obj;
            return name.equals(studyGroup.getName()) && coordinates.equals(studyGroup.getCoordinates()) &&
                    (studentsCount == studyGroup.getStudentsCount()) && (transferredStudents == studyGroup.getTransferredStudents()) &&
                    (semester.equals(studyGroup.getSemesterEnum())) && formOfEducation.equals(studyGroup.getFormOfEducation());
        }
        return false;
    }

}

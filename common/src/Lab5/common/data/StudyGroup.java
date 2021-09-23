package Lab5.common.data;
import Lab5.common.interactions.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Main class: StudyGroup. It is store in the collection.
 */
public class StudyGroup implements Comparable<StudyGroup>, Serializable {

    private Integer id ; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long studentsCount; //Значение поля должно быть больше 0, Поле может быть null
    private int transferredStudents; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Semester semesterEnum; //Поле может быть null
    private Person groupAdmin; //Поле может быть null
    private User owner;

    public StudyGroup(Integer id, String name, Coordinates coordinates, Date creationDate, Long studentsCount, int transferredStudents, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin, User owner){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.transferredStudents = transferredStudents;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
        this.owner = owner;
    }

    /**
     *
     * @return id of a group
     */
    public Integer getId(){
        return id;
    }

    /**
     * @return the name of the group.
     */

    public String getName(){
        return name;
    }

    /**
     *
     * @return Coordinates of the group.
     */
    public Coordinates getCoordinates(){
        return coordinates;
    }

    /**
     *
     * @return Creation date of the group.
     */
    public Date getCreationDate(){
        return creationDate;
    }

    /**
     *
     * @return students count of the group.
     */
    public Long getStudentsCount(){
        return studentsCount;
    }

    /**
     *
     * @return transfer student count of the group.
     */
    public int getTransferredStudents(){
        return transferredStudents;
    }

    /**
     *
     * @return form of education of the group
     */
    public FormOfEducation getFormOfEducation(){
        return formOfEducation;
    }

    /**
     *
     * @return semester of the group.
     */
    public Semester getSemesterEnum(){
        return semesterEnum;
    }

    /**
     *
     * @return group admin.
     */
    public Person getGroupAdmin(){
        return groupAdmin;
    }

    public User getOwner(){
        return owner;
    }

    @Override
    public int compareTo(StudyGroup studyGroupObj) {
        return id.compareTo(studyGroupObj.getId());
    }
    @Override
    public String toString(){
        String info = "";
        info += "Группа № " + id;
        info += " (добавлен " + creationDate + "" + creationDate.getTime() + ")";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Число студентов: " + studentsCount;
        info += "\n Студенты перевелись: " + transferredStudents;
        info += "\n Форма обучения: " + formOfEducation;
        info += "\n Семестр: " + semesterEnum;
        info += "\n Админ группы: " + groupAdmin;
        return info;
    }

    public int compareToTransferredStudents(StudyGroup s1){
        if(Integer.valueOf(transferredStudents).equals(s1.getTransferredStudents())) return -1;
        if(Integer.valueOf(transferredStudents).compareTo(s1.getTransferredStudents())>0) return 1;
        else return -1;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int)studentsCount.hashCode() + transferredStudents + formOfEducation.hashCode() + semesterEnum.hashCode() + groupAdmin.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (this==obj) return true;
        if (obj instanceof StudyGroup) {
            StudyGroup studyGroupObj = (StudyGroup) obj;
            return name.equals(studyGroupObj.getName()) && coordinates.equals(studyGroupObj.getCoordinates()) &&
                    (studentsCount.equals(studyGroupObj.getStudentsCount())) && (transferredStudents == studyGroupObj.getTransferredStudents()) &&
                    formOfEducation.equals(studyGroupObj.formOfEducation) && semesterEnum.equals(studyGroupObj.getSemesterEnum()) &&
                    groupAdmin.equals(studyGroupObj.getGroupAdmin());
        }
        return false;
    }
}





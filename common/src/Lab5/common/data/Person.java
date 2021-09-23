package Lab5.common.data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Person class for group admin.
 */
public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private LocalDate birthday; //Поле не может быть null
    private String passportID; //Значение этого поля должно быть уникальным, Строка не может быть пустой, Длина строки должна быть не меньше 5, Поле может быть null

    public Person(String name, LocalDate birthday, String passportID){
        this.name = name;
        this.birthday = birthday;
        this.passportID = passportID;
    }

    /**
     *
     * @return name of the person.
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return date of birthday of group admin.
     */
    public LocalDate getBirthday(){
        return birthday;
    }

    /**
     *
     * @return passport ID of group admin.
     */
    public String getPassportID(){
        return passportID;
    }
    @Override
    public String toString() {
        return name + " ( родился в " + birthday + ", № Паспрорта: " + passportID +" )";
    }

    @Override
    public boolean equals(Object obj){
        if (this==obj) return true;
        if (obj instanceof Person){
            Person groupAdmin = (Person) obj;
            return name.equals(groupAdmin.getName()) && (birthday.equals(groupAdmin.getBirthday())) && (passportID.equals(groupAdmin.getPassportID()));
        }
        return false;
    }

}

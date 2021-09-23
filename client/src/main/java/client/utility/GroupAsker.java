package client.utility;

import Lab5.common.data.Coordinates;
import Lab5.common.data.FormOfEducation;
import Lab5.common.data.Person;
import Lab5.common.data.Semester;
import Lab5.common.exceptions.IncorrectInputInScriptException;
import Lab5.common.exceptions.MustBeNotEmptyException;
import Lab5.common.utility.Outputer;
import Lab5.common.exceptions.NotInDeclaredLimitsException;
import client.App;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks user a group's value.
 */
public class GroupAsker {
    private final int MAX_Y = 262;
    private final long MIN_StudentsCount = 1;
    private final long Min_TrasferredStudentsCount = 0;
    private final long MAX_StudentsCount = 40;

    private Scanner userScanner;
    private boolean fileMode;

    public GroupAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Sets a scanner to scan user input.
     * @param userScanner Scanner to set
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     *
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return userScanner;
    }

    /**
     * Sets group asker mode to 'File mode'
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets group asker mode to 'User Mode'
     */
    public void setUserMode() {
        fileMode = false;
    }

    public boolean checkIfNull() {
        return true;
    }

    /**
     * Asks a user the group's name.
     * @return Group's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong
     */
    public String askName() throws IncorrectInputInScriptException {
        String name = null;
        while(true) {
            try {
                System.out.println("Введите имя: ");
                System.out.println(App.PS2);
                name = userScanner.nextLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException e) {
                Outputer.printerror("Пользовательский ввод не обнаружен!");
                if(fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (MustBeNotEmptyException e) {
                System.err.println("имя не может быть пустым");
            } catch (IllegalStateException e) {
                System.err.println("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Asks user the X-coordinate
     * @return the X-coordinate
     * @throws IncorrectInputInScriptException if script was wrong
     */
    public int askX() throws IncorrectInputInScriptException {
        String strX;
        int x=0;
        while(true) {
            try {
                Outputer.println("Введите координату х:");
                Outputer.print(App.PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strX);
                x = Integer.parseInt(strX);
                break;
            } catch (NoSuchElementException e) {
                Outputer.printerror("Пользовательский ввод не обнаружен!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (NumberFormatException e) {
                Outputer.printerror("Коодината Х должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException e) {
                Outputer.printerror("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks user Y-coordinates
     * @return Y-coordinate
     * @throws IncorrectInputInScriptException the script was wrong
     */
    public float askY() throws IncorrectInputInScriptException {
        String strY=null;
        float y=0;
        while(true) {
            try {
                Outputer.println("Введите координату Y < " + (MAX_Y + 1) + ":");
                Outputer.print(App.PS2);
                strY = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strY);
                y = Float.parseFloat(strY);
                if (y > MAX_Y) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Пользовательский ввод не обнаружен!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Координата Y не может превышать " + MAX_Y + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата Y должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Combines the X-Y coordinate
     * @return Group Coordinates XY
     * @throws IncorrectInputInScriptException if script was wrong
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        int x;
        float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks user the student count of the group
     * @return Student count of the group
     * @throws IncorrectInputInScriptException If script was bas
     */
    public Long askStundentsCount() throws IncorrectInputInScriptException {
        String strStudentsCount=null;
        Long studentsCount = 0L;
        while(true) {
            try {
                Outputer.println("Введите число студентов: ");
                Outputer.print(App.PS2);
                strStudentsCount = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strStudentsCount);
                studentsCount = Long.parseLong(strStudentsCount);
                if (studentsCount < MIN_StudentsCount || studentsCount > MAX_StudentsCount)
                    throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException e) {
                Outputer.printerror("Пользовательский ввод не обнаружен! ");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (NotInDeclaredLimitsException e) {
                Outputer.printerror("Число студентов должно быть больше нуля и меньше 40 ");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException e) {
                Outputer.printerror("Число студентов должна быть число");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException e) {
                Outputer.printerror("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return studentsCount;
    }

    /**
     * Asks a user the groups transfer students
     * @return Transfer student count
     * @throws IncorrectInputInScriptException if script was bad
     */
    public int askTransferredStundentsCount() throws IncorrectInputInScriptException {
        String strStudentsCount = null;
        int transferredStudentsCount = 0;
        while(true) {
            try {
                Outputer.println("Введите число переведённых студентов: ");
                Outputer.print(App.PS2);
                strStudentsCount = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strStudentsCount);
                transferredStudentsCount = Integer.parseInt(strStudentsCount);
                if (transferredStudentsCount < Min_TrasferredStudentsCount) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException e) {
                Outputer.printerror("Пользовательсзий ввод не обнаружен!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (NotInDeclaredLimitsException e) {
                Outputer.printerror("Число студентов должно быть больше или равно нулю");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException e) {
                Outputer.printerror("Число студентов должна быть число");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException e) {
                Outputer.printerror("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return transferredStudentsCount;
    }

    /**
     * Asks a user in which semester the group is in
     * @return Group's semester.
     * @throws IncorrectInputInScriptException If script was bad.
     */
    public Semester askSemester() throws IncorrectInputInScriptException {
        String strSemester ;
        Semester semester ;
        while(true) {
            try {
                Outputer.println("Список семестрa - " + Semester.nameList());
                Outputer.println("Введите семестр: ");
                Outputer.print(App.PS2);
                strSemester = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strSemester);
                semester = Semester.valueOf(strSemester.toUpperCase());
                break;
            } catch (NoSuchElementException e) {
                Outputer.printerror("Пользовательсзий ввод не обнаружен!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (IllegalArgumentException e) {
                Outputer.printerror("Семестра нет в списке");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException e) {
                Outputer.printerror("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return semester;
    }

    /**
     * Asks user the groups form of education.
     * @return Group's form of eduction.
     * @throws IncorrectInputInScriptException if script was bad
     */
    public FormOfEducation askFormOfEducation() throws IncorrectInputInScriptException {
        String strFormOfEducation = null;
        FormOfEducation formOfEducation = null;
        while(true) {
            try {
                Outputer.println("Список форма обучения - " + FormOfEducation.nameList());
                Outputer.println("Введите форму обучения:");
                Outputer.print(App.PS2);
                strFormOfEducation = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strFormOfEducation);
                formOfEducation = FormOfEducation.valueOf(strFormOfEducation.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Пользовательский ввод не обнаружен");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("Форма обучения нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return formOfEducation;
    }

    /**
     * Asks user the group admin's name
     * @return Group admin's name.
     * @throws IncorrectInputInScriptException If the script was bad.
     */
    public String askGroupAdminsName() throws IncorrectInputInScriptException {
        String groupAdminsName = null;
        while(true) {
            try {
                Outputer.println("Введите имя админа:");
                Outputer.print(App.PS2);
                groupAdminsName = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(groupAdminsName);
                if (groupAdminsName.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Пользовательский ввод не обнаружена!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя админа не может быть пустым");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }

        } return groupAdminsName;
    }

    /**
     * Asks user group admin's birthday
     * @return Admin's date of birth
     * @throws IncorrectInputInScriptException If script was bad
     */
    public LocalDate askBirthday() throws IncorrectInputInScriptException {
        String strBirthday = null;
        LocalDate birthday = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while(true) {
            try{
                Outputer.println("Введите дату рождения админа (дд/мм/гггг): ");
                Outputer.print(App.PS2);
                strBirthday = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strBirthday);
                birthday = LocalDate.parse(strBirthday,formatter);
                if(strBirthday.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (DateTimeParseException e){

                Outputer.printerror("Формат даты неверно ");

            } catch (NoSuchElementException e) {
                System.err.println("Пользовательский ввод не обнаружен!");
                System.exit(0);
            } catch (MustBeNotEmptyException e) {
                System.err.println("Дата не может быть пустым");
            } catch (IllegalStateException e) {
                System.err.println("Непредвиденная ошибка");
                System.exit(0);
            }
        }
        return birthday;
    }

    /**
     * Asks user admin's Passport Id
     * @return Admin's Passport Id
     * @throws IncorrectInputInScriptException If script was bad.
     */
    public String askPassportId() throws IncorrectInputInScriptException {
        String passportId = null;
        while(true) {
            try {
                Outputer.println("Введите № паспорта:");
                Outputer.print(App.PS2);
                passportId = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(passportId);
                if (passportId.equals("")) throw new MustBeNotEmptyException();
                if (passportId.length() < 5) throw new NotInDeclaredLimitsException();
                break;
            } catch (NotInDeclaredLimitsException e) {
                Outputer.printerror("№ паспорта должен иметь больше 5 символов");
            } catch (NoSuchElementException e) {
                Outputer.printerror("Пользовательский ввод не обнаружен!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (MustBeNotEmptyException e) {
                Outputer.printerror("№ паспорта не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException e) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
            return passportId;
    }

    /**
     * Combines information about admin
     * @return Admin
     * @throws IncorrectInputInScriptException if script  was bad
     */
    public Person askGroupAdmin() throws IncorrectInputInScriptException{
        String name;
        LocalDate birthday;
        String passportId;
        name = askGroupAdminsName();
        birthday = askBirthday();
        passportId = askPassportId();
        return new Person(name,birthday,passportId);
}

    /**
     * Asks user a question
     * @param question A question
     * @return (type/false)
     * @throws IncorrectInputInScriptException if script was bad
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer = null;
        while(true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(App.PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Пользовательский ввод не обнаружен!");
                if (fileMode) throw new IncorrectInputInScriptException();
                System.exit(0);
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Ответ должен быть представлен знаками '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return (answer.equals("+")) ? true : false;
    }




    @Override
    public String toString(){
        return "GroupAsker (вспомогательный класс для запросов пользователю)";
    }

}

package server.utility;

import Lab5.common.data.*;
import Lab5.common.exceptions.DatabaseHandlingException;
import Lab5.common.interactions.GroupRaw;
import Lab5.common.interactions.User;
import Lab5.common.utility.Outputer;
import server.App;

import java.sql.*;
import java.time.LocalDate;


/**
 * Operates the database collection itself
 */
public class DatabaseCollectionManager {
    // Group Table
    private final String SELECT_ALL_GROUPS = "SELECT * FROM " + DatabaseHandler.GROUP_TABLE;
    private final String SELECT_GROUP_BY_ID = SELECT_ALL_GROUPS + " WHERE " + DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_GROUP_BY_ID_AND_USER_ID = SELECT_GROUP_BY_ID + " AND " + DatabaseHandler.GROUP_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_GROUP = "INSERT INTO " +
            DatabaseHandler.GROUP_TABLE + " (" +
            DatabaseHandler.GROUP_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_NUMBER_OF_TOTAL_STUDENTS_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_NUMBER_OF_TRANSFERRED_STUDENTS_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_FORM_OF_EDUCATION_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_SEMESTER_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_ADMIN_ID_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_USER_ID_COLUMN + ") VALUES (?,?,?,?,?," +
            "?, ?, ?)";
    private final String DELETE_GROUP_BY_ID = "DELETE FROM " +
            DatabaseHandler.GROUP_TABLE + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ? ";
    private final String UPDATE_GROUP_NAME_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_GROUP_SEMESTER_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_SEMESTER_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_GROUP_FORM_OF_EDUCATION_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_FORM_OF_EDUCATION_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_GROUP_TOTAL_STUDENTS_BY_ID = " UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_NUMBER_OF_TOTAL_STUDENTS_COLUMN + " = ?" +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_GROUP_TRANSFERRED_STUDENTS_BY_ID = " UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_NUMBER_OF_TRANSFERRED_STUDENTS_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    //COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_GROUP_ID = SELECT_ALL_COORDINATES + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_GROUP_ID_COLUMN + " =?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_GROUP_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?,?,?)";
    private final String UPDATE_COORDINATES_BY_GROUP_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_GROUP_ID_COLUMN + " = ?";

    //ADMIN_TABLE
    private final String SELECT_ALL_ADMIN = "SELECT * FROM " + DatabaseHandler.ADMIN_TABLE;
    private final String SELECT_ADMIN_BY_ID = SELECT_ALL_ADMIN + " WHERE " +
            DatabaseHandler.ADMIN_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_ADMIN = "INSERT INTO " +
            DatabaseHandler.ADMIN_TABLE + " (" +
            DatabaseHandler.ADMIN_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.ADMIN_TABLE_DATE_OF_BIRTH_COLUMN + ", " +
            DatabaseHandler.ADMIN_TABLE_PASSPORT_ID_COLUMN + ") VALUES (?,?,?)";
    private final String UPDATE_ADMIN_BY_ID = "UPDATE " + DatabaseHandler.ADMIN_TABLE + " SET " +
            DatabaseHandler.ADMIN_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseHandler.ADMIN_TABLE_DATE_OF_BIRTH_COLUMN + " = ?, " +
            DatabaseHandler.ADMIN_TABLE_PASSPORT_ID_COLUMN + " = ?" + "WHERE" +
            DatabaseHandler.ADMIN_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_ADMIN_BY_ID = "DELETE FROM " +
            DatabaseHandler.ADMIN_TABLE + " WHERE " + DatabaseHandler.ADMIN_TABLE_ID_COLUMN;
    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager){
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    /**
     *
     * @param resultSet
     * @return Create a group
     * @throws SQLException
     */
    private StudyGroup createGroup(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt(DatabaseHandler.GROUP_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.GROUP_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByGroupId(id);
        Date creationDate =  resultSet.getDate(DatabaseHandler.GROUP_TABLE_CREATION_DATE_COLUMN);
        Long studentCount = resultSet.getLong(DatabaseHandler.GROUP_TABLE_NUMBER_OF_TOTAL_STUDENTS_COLUMN);
        int transferredStudents = resultSet.getInt(DatabaseHandler.GROUP_TABLE_NUMBER_OF_TRANSFERRED_STUDENTS_COLUMN);
        FormOfEducation formOfEducation = FormOfEducation.valueOf(resultSet.getString(DatabaseHandler.GROUP_TABLE_FORM_OF_EDUCATION_COLUMN));
        Semester semester = Semester.valueOf(resultSet.getString(DatabaseHandler.GROUP_TABLE_SEMESTER_COLUMN));
        Person admin = getAdminById(resultSet.getInt(DatabaseHandler.GROUP_TABLE_ADMIN_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseHandler.GROUP_TABLE_USER_ID_COLUMN));
        return new StudyGroup(
                id,
                name,
                coordinates,
                creationDate,
                studentCount,
                transferredStudents,
                formOfEducation,
                semester,
                admin,
                owner);
    }

    /**
     *
     * @return Group list
     * @throws DatabaseHandlingException
     */
    public CachedLinkedHashSet<StudyGroup> getCollection() throws DatabaseHandlingException{
     CachedLinkedHashSet<StudyGroup> groupList = new CachedLinkedHashSet<>();
     PreparedStatement preparedSelectAllStatement = null;
     try{
         preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_GROUPS,false);
         ResultSet resultSet = preparedSelectAllStatement.executeQuery();
         int groupCount = 0;
         while(resultSet.next()){
             groupList.add(createGroup(resultSet));
             groupCount++;
         }
         Outputer.println("Всего " + groupCount + " групп загружено.");
     }catch (SQLException exception){
         throw new DatabaseHandlingException();
     }finally {
         databaseHandler.closePreparedStatement(preparedSelectAllStatement);
     }
     return groupList;
    }

    /**
     *
     * @param groupId
     * @return Admin Id
     * @throws SQLException
     */
    private long getAdminIdByGroupId(long groupId) throws SQLException {
        long adminId;
        PreparedStatement preparedSelectMarineByIdStatement = null;
        try {
            preparedSelectMarineByIdStatement = databaseHandler.getPreparedStatement(SELECT_GROUP_BY_ID, false);
            preparedSelectMarineByIdStatement.setLong(1, groupId);
            ResultSet resultSet = preparedSelectMarineByIdStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_GROUP_BY_ID.");
            if (resultSet.next()) {
                adminId = resultSet.getLong(DatabaseHandler.GROUP_TABLE_ADMIN_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при выполнении запроса SELECT_ADMIN_ID_BY_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectMarineByIdStatement);
        }
        return adminId;
    }

    /**
     *
     * @param groupId
     * @return Coordinates
     * @throws SQLException
     */
    private Coordinates getCoordinatesByGroupId(long groupId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByGroupIdStatement = null;
        try {
            preparedSelectCoordinatesByGroupIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_GROUP_ID, false);
            preparedSelectCoordinatesByGroupIdStatement.setLong(1, groupId);
            ResultSet resultSet = preparedSelectCoordinatesByGroupIdStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_COORDINATES_BY_GROUP_ID.");
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_GROUP_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinatesByGroupIdStatement);
        }
        return coordinates;
    }

    /**
     *
     * @param adminId
     * @return Admin
     * @throws SQLException
     */
    private Person getAdminById(int adminId){
        Person person = null;
        PreparedStatement preparedSelectAdminByIdStatement = null;
        try {
            preparedSelectAdminByIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_ADMIN_BY_ID, false);
            preparedSelectAdminByIdStatement.setInt(1, adminId);
            ResultSet resultSet = preparedSelectAdminByIdStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_ADMIN_BY_ID.");
            if (resultSet.next()) {
                person = new Person(
                        resultSet.getString(DatabaseHandler.ADMIN_TABLE_NAME_COLUMN),
                        resultSet.getDate(DatabaseHandler.ADMIN_TABLE_DATE_OF_BIRTH_COLUMN).toLocalDate(),
                        resultSet.getString(DatabaseHandler.ADMIN_TABLE_PASSPORT_ID_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при выполнении запроса SELECT_ADMIN_BY_ID!");
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectAdminByIdStatement);
        }
        return person;
    }

    /**
     *
     * @param groupRaw
     * @param user
     * @return studyGroup
     * @throws DatabaseHandlingException
     */
    public StudyGroup insertGroup(GroupRaw groupRaw, User user) throws DatabaseHandlingException {
        StudyGroup group;
        PreparedStatement preparedInsertGroupStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertAdminStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavePoint();

            Date creationDate = Date.valueOf(LocalDate.now());

            preparedInsertGroupStatement = databaseHandler.getPreparedStatement(INSERT_GROUP, true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertAdminStatement = databaseHandler.getPreparedStatement(INSERT_ADMIN, true);

            preparedInsertAdminStatement.setString(1, groupRaw.getGroupAdmin().getName());
            preparedInsertAdminStatement.setDate(2, Date.valueOf(groupRaw.getGroupAdmin().getBirthday()));
            preparedInsertAdminStatement.setString(3,groupRaw.getGroupAdmin().getPassportID());
            if (preparedInsertAdminStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedAdminKeys = preparedInsertAdminStatement.getGeneratedKeys();
            long adminId;
            if (generatedAdminKeys.next()) {
                adminId = generatedAdminKeys.getLong(1);
            } else throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_ADMIN.");

            preparedInsertGroupStatement.setString(1, groupRaw.getName());
            preparedInsertGroupStatement.setDate(2, creationDate);
            preparedInsertGroupStatement.setLong(3, groupRaw.getStudentsCount());
            preparedInsertGroupStatement.setInt(4, groupRaw.getTransferredStudents());
            preparedInsertGroupStatement.setString(5, groupRaw.getFormOfEducation().toString());
            preparedInsertGroupStatement.setString(6, groupRaw.getSemester().toString());
            preparedInsertGroupStatement.setLong(7, adminId);
            preparedInsertGroupStatement.setLong(8, databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertGroupStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedGroupKeys = preparedInsertGroupStatement.getGeneratedKeys();
            int studyGroupId;
            if (generatedGroupKeys.next()) {
                studyGroupId = generatedGroupKeys.getInt(1);
            } else throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_GROUP.");

            preparedInsertCoordinatesStatement.setLong(1, studyGroupId);
            preparedInsertCoordinatesStatement.setDouble(2, groupRaw.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setFloat(3, groupRaw.getCoordinates().getY());
            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_COORDINATES.");

            group = new StudyGroup(
                    studyGroupId,
                    groupRaw.getName(),
                    groupRaw.getCoordinates(),
                    creationDate,
                    groupRaw.getStudentsCount(),
                    groupRaw.getTransferredStudents(),
                    groupRaw.getFormOfEducation(),
                    groupRaw.getSemester(),
                    groupRaw.getGroupAdmin(),
                    user
            );

            databaseHandler.commit();
            return group;
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при выполнении группы запросов на добавление нового объекта!");
            exception.printStackTrace();
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertGroupStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertAdminStatement);
            databaseHandler.setNormalMode();
        }
    }


    /**
     *
     * @param groupId
     * @param groupRaw
     * @throws DatabaseHandlingException
     */
    public void updateGroupByID(long groupId, GroupRaw groupRaw) throws DatabaseHandlingException{
        PreparedStatement preparedUpdateGroupNameByIdStatement = null;
        PreparedStatement preparedUpdateGroupTotalStudentsByIdStatement = null;
        PreparedStatement preparedUpdateGroupTransferredStudentsByIdStatement = null;
        PreparedStatement preparedUpdateGroupFormOfEducationByIdStatement = null;
        PreparedStatement preparedUpdateGroupSemesterByIdStatement = null;
        PreparedStatement preparedUpdateGroupCoordinatesByIdStatement = null;
        PreparedStatement preparedUpdateGroupAdminByIdStatement = null;

        try{
            databaseHandler.setCommitMode();
            databaseHandler.setSavePoint();

            preparedUpdateGroupNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_NAME_BY_ID, false);
            preparedUpdateGroupTotalStudentsByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_TOTAL_STUDENTS_BY_ID,false);
            preparedUpdateGroupTransferredStudentsByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_TRANSFERRED_STUDENTS_BY_ID,false);
            preparedUpdateGroupFormOfEducationByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_FORM_OF_EDUCATION_BY_ID, false);
            preparedUpdateGroupSemesterByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_SEMESTER_BY_ID, false);
            preparedUpdateGroupCoordinatesByIdStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_GROUP_ID, false);
            preparedUpdateGroupAdminByIdStatement = databaseHandler.getPreparedStatement(UPDATE_ADMIN_BY_ID, false);

            if (groupRaw.getName() != null){
                preparedUpdateGroupNameByIdStatement.setString(1, groupRaw.getName());
                preparedUpdateGroupNameByIdStatement.setLong(2, groupId);
                if (preparedUpdateGroupNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_GROUP_NAME_BY_ID.");
            }
            if (groupRaw.getCoordinates() != null){
                preparedUpdateGroupCoordinatesByIdStatement.setInt(1, groupRaw.getCoordinates().getX());
                preparedUpdateGroupCoordinatesByIdStatement.setFloat(2, groupRaw.getCoordinates().getY());
                preparedUpdateGroupCoordinatesByIdStatement.setLong(3, groupId);
                if(preparedUpdateGroupCoordinatesByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_COORDINATES_BY_GROUP_ID.");
            }
            if(groupRaw.getStudentsCount() != -1){
                preparedUpdateGroupTotalStudentsByIdStatement.setLong(1,groupRaw.getStudentsCount());
                preparedUpdateGroupTotalStudentsByIdStatement.setLong(2,groupId);
                if(preparedUpdateGroupTotalStudentsByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_GROUP_TOTAL_STUDENTS_BY_ID");
            }
            if(groupRaw.getTransferredStudents() != -1){
                preparedUpdateGroupTotalStudentsByIdStatement.setInt(1,groupRaw.getTransferredStudents());
                preparedUpdateGroupTotalStudentsByIdStatement.setLong(2,groupId);
                if(preparedUpdateGroupTotalStudentsByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_GROUP_TRANSFERRED_STUDENTS_BY_ID");
            }
            if(groupRaw.getFormOfEducation() != null){
                preparedUpdateGroupFormOfEducationByIdStatement.setString(1,groupRaw.getFormOfEducation().toString());
                preparedUpdateGroupFormOfEducationByIdStatement.setLong(2,groupId);
                if(preparedUpdateGroupFormOfEducationByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_GROUP_FORM_OF_EDUCATION_BY_ID");
            }
            if(groupRaw.getSemester() != null){
                preparedUpdateGroupSemesterByIdStatement.setString(1, groupRaw.getSemester().toString());
                preparedUpdateGroupSemesterByIdStatement.setLong(2,groupId);
                if(preparedUpdateGroupSemesterByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_GROUP_SEMESTER_BY_ID");
            }
            if(groupRaw.getGroupAdmin() != null){
                preparedUpdateGroupAdminByIdStatement.setString(1, groupRaw.getGroupAdmin().getName());
                preparedUpdateGroupAdminByIdStatement.setDate(2, Date.valueOf(groupRaw.getGroupAdmin().getBirthday()));
                preparedUpdateGroupAdminByIdStatement.setString(3,groupRaw.getGroupAdmin().getPassportID());
                preparedUpdateGroupAdminByIdStatement.setLong(4, groupId);
                if(preparedUpdateGroupAdminByIdStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_GROUP_ADMIN_BY_ID");
            }
            databaseHandler.commit();
        } catch (SQLException e){
            App.logger.error("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        }finally {
            databaseHandler.closePreparedStatement(preparedUpdateGroupNameByIdStatement);

        }
    }

    /**
     *
     * @param groupId
     * @throws DatabaseHandlingException
     */
    public void deleteAdminById(long groupId) throws DatabaseHandlingException{
        PreparedStatement preparedDeleteAdminByIdStatement = null;
        try{
            preparedDeleteAdminByIdStatement = databaseHandler.getPreparedStatement(DELETE_ADMIN_BY_ID,false);
            preparedDeleteAdminByIdStatement.setLong(1,getAdminIdByGroupId(groupId));
            if(preparedDeleteAdminByIdStatement.executeUpdate() == 0) Outputer.println(3);
            App.logger.info("Выполнен запрос DELETE_ADMIN_BY_ID");
        }catch (SQLException exception){
            App.logger.error("Произошла ошибка при выполнении запроса DELETE_ADMIN_BY_ID");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteAdminByIdStatement);
        }
    }

    /**
     *
     * @param groupId
     * @throws DatabaseHandlingException
     */
    public void deleteGroupById(long groupId) throws DatabaseHandlingException{
        PreparedStatement preparedDeleteGroupByIdStatement = null;
        try{
            preparedDeleteGroupByIdStatement = databaseHandler.getPreparedStatement(DELETE_GROUP_BY_ID,false);
            preparedDeleteGroupByIdStatement.setLong(1,getAdminIdByGroupId(groupId));
            if(preparedDeleteGroupByIdStatement.executeUpdate() == 0) Outputer.println(3);
            App.logger.info("Выполнен запрос DELETE_GROUP_BY_ID");
        }catch (SQLException exception){
            App.logger.error("Произошла ошибка при выполнении запроса DELETE_GROUP_BY_ID");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteGroupByIdStatement);
        }
    }

    /**
     *
     * @param groupId
     * @param user
     * @return Is everything ok?
     * @throws DatabaseHandlingException
     */
    public boolean checkGroupUserId(long groupId, User user) throws DatabaseHandlingException{
        PreparedStatement preparedSelectGroupByIdAndUserIdStatement = null;
        try{
            preparedSelectGroupByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_GROUP_BY_ID_AND_USER_ID,false);
            preparedSelectGroupByIdAndUserIdStatement.setLong(1, groupId);
            preparedSelectGroupByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectGroupByIdAndUserIdStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_GROUP_BY_ID_AND_USER_ID.");
            return  resultSet.next();
        }catch (SQLException exception){
            App.logger.error("Произошла при выполнении запроса SELECT_GROUP_BY_ID_AND_USER_ID!");
            throw new DatabaseHandlingException();
        }finally {
            databaseHandler.closePreparedStatement(preparedSelectGroupByIdAndUserIdStatement);
        }
    }

    /**
     *
     * @throws DatabaseHandlingException
     */
    public void clearCollection() throws DatabaseHandlingException{
        CachedLinkedHashSet<StudyGroup> groupList = getCollection();
        for(StudyGroup group : groupList){
            deleteGroupById(group.getId());
        }
    }


}

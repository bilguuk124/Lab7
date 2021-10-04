package server.utility;

import Lab5.common.exceptions.ConnectionErrorException;
import Lab5.common.utility.Outputer;
import server.App;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to handle Database.
 */
public class DatabaseHandler {
    // Table names
    public static final String GROUP_TABLE = "groups";
    public static final String USER_TABLE = "my_user";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String ADMIN_TABLE = "admin";
    // GROUP_TABLE column Names
    public static final String GROUP_TABLE_ID_COLUMN = "id";
    public static final String GROUP_TABLE_NAME_COLUMN = "name";
    public static final String GROUP_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String GROUP_TABLE_NUMBER_OF_TOTAL_STUDENTS_COLUMN = "total_students";
    public static final String GROUP_TABLE_NUMBER_OF_TRANSFERRED_STUDENTS_COLUMN = "transferred_students";
    public static final String GROUP_TABLE_SEMESTER_COLUMN = "semester";
    public static final String GROUP_TABLE_FORM_OF_EDUCATION_COLUMN = "form_of_education";
    public static final String GROUP_TABLE_ADMIN_ID_COLUMN = "admin_id";
    public static final String GROUP_TABLE_USER_ID_COLUMN = "user_id";
    // USER_TABLE column names
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    // COORDINATES_TABLE column names
    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_GROUP_ID_COLUMN = "group_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    // ADMIN_TABLE column names
    public static final String ADMIN_TABLE_ID_COLUMN = "id";
    public static final String ADMIN_TABLE_NAME_COLUMN = "name";
    public static final String ADMIN_TABLE_DATE_OF_BIRTH_COLUMN = "date_of_birth";
    public static final String ADMIN_TABLE_PASSPORT_ID_COLUMN = "passport_id";

    private final String JDBC_DRIVER = "org.postgresql.Driver";
    Logger logging = Logger.getLogger("org.postgresql.Driver");


    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connectToDataBase();
    }

    /**
     * Class for connecting database
     */

    private void connectToDataBase(){
        try{
            logging.setLevel(Level.OFF);
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url,user,password);
            Outputer.println("Соединение с базой данных установлено.");
            App.logger.info("Соединение с базой данных установлено.");
        } catch (SQLException e) {
            Outputer.printerror("Произошла ошибка при подключении к базе данных!");
            App.logger.error("Произошла ошибка при подключении к базе данных! Попробуйте ещё раз");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            Outputer.printerror("Драйвер управления базой данных не найден!");
            App.logger.error("Драйвер управления базой данных не найден!");
        }
    }

    /**
     *
     * @param sqlStatement
     * @param generateKeys
     * @return
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException{
        PreparedStatement preparedStatement;
        try{

            if(connection == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS: Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGeneratedKeys);
            return preparedStatement;

        } catch (SQLException e){
            if (connection == null) App.logger.error("Соединение с базой данных не установлено! ");
            throw new SQLException(e);
        }
    }


    public void closePreparedStatement (PreparedStatement sqlStatement){
        if (sqlStatement == null) return;;
        try{
            sqlStatement.close();
        }catch (SQLException e){
            App.logger.error("error");
        }
    }

    public void closeConnection(){
        if (connection ==null) return;
        try{
            connection.close();
            Outputer.println("Соединение с базой данных разорвано");

        }catch (SQLException e){
            Outputer.printerror("Произошла ошибка при разрыве соединения с базой данных");
        }
    }

    public void setCommitMode(){
        try{
            if(connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        }catch (SQLException e){
            App.logger.error("Произошла ошибка при установлении режима транзакции базы данных! ");

        }
    }
    public void setNormalMode(){
        try{
            if(connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        }catch (SQLException e ){
            App.logger.error("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }
    public void commit(){
        try{
            if(connection==null) throw new SQLException();
            connection.commit();
        }catch (SQLException e ){
            App.logger.error("Произошла ошибка при подтверждении нового состояния базы данных");

        }
    }
    public void rollback(){
        try{
            if(connection==null) throw new SQLException();
            connection.rollback();
        }catch (SQLException e ){
            App.logger.error("Произошла ошибка при возврате исходного состояния базы данных");
        }
    }
    public void setSavePoint(){
        try{
            if(connection==null) throw new SQLException();
            connection.setSavepoint();
        }catch (SQLException e ){
            App.logger.error("Произошла ошибка при сохранении состояния базы данных");

        }
    }
}

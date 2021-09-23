package Lab5.common.interactions;

import java.io.Serializable;

public class User  implements Serializable{
    private String username;
    private String password;

    public User(String username, String password){
        this.password = password;
        this.username = username;
    }

    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return password
     */
    public String getPassword(){
        return password;
    }

    @Override
    public String toString(){
        return username + ":" + password;
    }

    @Override
    public int hashCode(){
        return username.hashCode()+password.hashCode();
    }

    @Override
    public boolean equals (Object obj){
        if (this == obj) return true;
        if (obj instanceof User) {
            User userObj = (User) obj;
            return username.equals(userObj.getUsername()) && password.equals(userObj.getPassword());
        }
        return false;
    }

}

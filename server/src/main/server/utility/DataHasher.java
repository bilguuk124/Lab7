package server.utility;

import server.App;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataHasher {

    public static String hash(String data){
        try{
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] messageDigest = md.digest(data.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while(hashtext.length()<32){
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            App.logger.error("Не найден алгоритм хэширования пароля!");
            throw new IllegalStateException(e);
        }
    }
}

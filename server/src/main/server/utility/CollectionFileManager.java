package server.utility;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import Lab5.common.utility.Outputer;
import server.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParseException;
import Lab5.common.data.StudyGroup;

/**
 * Operates file for saving/loading collection
 */
public class CollectionFileManager {
    public Gson gson = new Gson();
    private String envVariable;
    private BufferedOutputStream bfos;
    public CollectionFileManager(String envVariable) {
        this.envVariable = envVariable;
    }


    public boolean checkCanRead(File file)  {
          return file.canRead();
    }
    public boolean checkCanWrite(File file){
          return (file.canWrite());
    }




    /**
     * Writes collection to file.
     * @param collection Collection to write.
     */

    public void writeCollection(LinkedHashSet<StudyGroup> collection){
        String newFile = envVariable;
        File file = new File(newFile);
        if(file.exists()){
            if (!checkCanWrite(new File(envVariable))) {
                newFile = "Default.json";
                Outputer.printerror("Не достаточен прав write или execute! Сохраняю в файл Default.json по умолчанию");
            }
        }
            try {
                bfos = new BufferedOutputStream(new FileOutputStream(newFile, false), 1024);
                String a = gson.toJson(collection);
                byte[] saveFile = a.getBytes(StandardCharsets.UTF_8);
                bfos.write(saveFile);
                bfos.flush();
                ResponseOutputer.appendln("Коллекция успешно сохранена в файл.");
            } catch (IOException e) {
                ResponseOutputer.appenderror("Загрузочный файл не может быть открыт");
            }

    }

    /**
     * Reads collection from file.
     * @return Read collection.
     */
    public CachedLinkedHashSet<StudyGroup> readCollection() {
        String newFile = envVariable;
        if(new File(newFile).exists()) {
            if (!checkCanRead(new File(envVariable))) {
                newFile = "Default.json";
            }
        }
        try (Scanner collectionFileScanner = new Scanner(new File(newFile))) {
            CachedLinkedHashSet<StudyGroup> collection;
            Type collectionType = new TypeToken<CachedLinkedHashSet<StudyGroup>>() {
            }.getType();
            collection = gson.fromJson(collectionFileScanner.nextLine().trim(), collectionType);
            Outputer.println("Коллекция успешна загружена");
            App.logger.info("Коллекция успешно загружена");
            return collection;
        } catch (FileNotFoundException e) {
            Outputer.printerror("Не достаточен прав чтение! Файл Default.json будет загружена по умолчанию");
            App.logger.warn("Загрузочный файл не найден!");
        } catch (NoSuchElementException e) {
            Outputer.printerror("Файл пуст");
            App.logger.error("Файл пуст");
        } catch (JsonParseException | NullPointerException e) {
            Outputer.printerror("Нет  необходимой коллекции");
            App.logger.error("Нет необходимой коллекции");
        } catch (IllegalStateException e) {
            Outputer.printerror("Непредвиденная ошибка");
            App.logger.error("Непредвиденная ошибка!");
            System.exit(0);
        }
        return new CachedLinkedHashSet<>();
    }

    @Override
    public String toString(){
        String string = "FileManager ";
        return string;

    }


}

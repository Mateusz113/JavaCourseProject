package com.mateusz113.shop.io.file;

import com.mateusz113.shop.io.console.ConsolePrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Class that holds static methods to serialize and deserialize objects.
 */
public class Serializer {
    /**
     * Creates a folder resources/data that is the default app spot to hold serialize data.
     *
     * @throws IOException if folder could not be created.
     */
    public static void createSerializeFolder() throws IOException {
        Path serializeFolderPath = Paths
                .get("src", "main", "resources", "data");
        if (!Files.exists(serializeFolderPath)) {
            Files.createDirectories(serializeFolderPath);
        }
    }

    /**
     * Imports the {@code Optional} of object data from serialized files.
     *
     * @param filePath path to the serialized file.
     * @return {@code Optional} of the serialized object.
     * @param <R> serializable class.
     */
    public static <R extends Serializable> Optional<R> importData(String filePath) {
        try (
                var ois = new ObjectInputStream(new FileInputStream(filePath))
        ) {
            return Optional.of((R) ois.readObject());
        } catch (FileNotFoundException e) {
            ConsolePrinter.printLine("Brak pliku lub folderu w ścieżce: " + filePath);
        } catch (IOException e) {
            ConsolePrinter.printLine("Błąd odczytu pliku ze ścieżki: " + filePath);
        } catch (ClassNotFoundException e) {
            ConsolePrinter.printLine("Niezgodny typ danych z pliku ze ścieżki: " + filePath);
        }
        return Optional.empty();
    }

    /**
     * Exports the object into serialized file.
     *
     * @param object object to serialize.
     * @param filePath path to the serialize file of the object.
     * @param <R> serializable class.
     */
    public static <R extends Serializable> void exportData(R object, String filePath) {
        try (
                var oos = new ObjectOutputStream(new FileOutputStream(filePath))
        ) {
            oos.writeObject(object);
        } catch (FileNotFoundException e) {
            ConsolePrinter.printLine("Brak pliku lub folderu w ścieżce: " + filePath);
        } catch (IOException e) {
            ConsolePrinter.printLine("Błąd zapisu do pliku o nazwie: " + filePath);
        }
    }
}

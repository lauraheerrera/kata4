package software.ulpgc.kata4.commands;

import software.ulpgc.kata4.io.DatabaseTitleWriter;
import software.ulpgc.kata4.io.FileTitleReader;
import software.ulpgc.kata4.io.TsvTitleDeserializer;
import software.ulpgc.kata4.model.Title;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ImportCommand implements Command {
    public ImportCommand() {
    }

    @Override
    public void execute() {
        try {
            File input = getInputFile();
            File output = getOutputFile();
            doExecute(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getOutputFile() {
        return new File("titles.db");
    }

    private File getInputFile() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Introduce la ruta del archivo de entrada: ");
            File file = new File(scanner.nextLine());
            if (file.exists()) {
                return file;
            }
            System.out.println("El archivo no existe. Inténtalo de nuevo.");
        }
    }

    private void doExecute(File input, File output) throws Exception {
        try (FileTitleReader reader = createTitleReader(input);
             DatabaseTitleWriter writer = createTitleWriter(output)) {
            List<Title> titles = reader.read();
            for (Title title : titles) {
                writer.write(title);
            }
        }
    }

    private static DatabaseTitleWriter createTitleWriter(File file) throws SQLException {
        return new DatabaseTitleWriter(deleteIfExists(file));
    }

    private static File deleteIfExists(File file) {
        if (file.exists()) file.delete();
        return file;
    }

    private static FileTitleReader createTitleReader(File file) throws IOException {
        return new FileTitleReader(file, new TsvTitleDeserializer());
    }
}

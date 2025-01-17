package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Title;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTitleReader implements TitleReader, AutoCloseable {
    private final TitleDeserializer deserializer;
    private final BufferedReader bufferedReader;

    public FileTitleReader(File file, TitleDeserializer deserializer) throws IOException {
        this.deserializer = deserializer;
        this.bufferedReader = new BufferedReader(new FileReader(file));
        this.bufferedReader.readLine();
    }

    @Override
    public List<Title> read() throws IOException {
        List<Title> titles = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            titles.add(deserializer.deserialize(line));
        }
        return titles;
    }

    @Override
    public void close() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }
}

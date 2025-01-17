package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Title;

import java.io.IOException;

public interface TitleWriter extends AutoCloseable {
    void write(Title title) throws IOException;
}

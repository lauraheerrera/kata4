package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Title;

import java.util.Arrays;
import java.util.List;

public class TsvTitleDeserializer implements TitleDeserializer {

    @Override
    public Title deserialize(String line) {
        return deserialize(line.split("\t"));
    }

    private Title deserialize(String[] split) {
        return new Title(
                split[0],
                split[3],
                split[8].equals("\\N") ? List.of() : Arrays.asList(split[8].split(",")),
                split[4].equals("1")
        );
    }
}

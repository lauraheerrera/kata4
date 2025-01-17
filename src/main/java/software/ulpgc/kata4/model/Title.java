package software.ulpgc.kata4.model;

import java.util.List;

public record Title(String id, String title, List<String> genre, boolean isAdult) {
}

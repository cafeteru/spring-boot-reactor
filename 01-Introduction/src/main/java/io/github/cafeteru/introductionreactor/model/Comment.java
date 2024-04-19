package io.github.cafeteru.introductionreactor.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Comment {
    private List<String> comments = new ArrayList<>();

    public void addComment(String comment) {
        comments.add(comment);
    }
}

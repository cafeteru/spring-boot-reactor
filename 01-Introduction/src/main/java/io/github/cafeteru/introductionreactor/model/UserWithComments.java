package io.github.cafeteru.introductionreactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserWithComments {
    private User user;
    private Comment comment;
}

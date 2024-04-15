package io.github.cafeteru.springbootreactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserWithComments {
    private User user;
    private Comment comment;
}

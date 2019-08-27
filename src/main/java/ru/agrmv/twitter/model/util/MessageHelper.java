package ru.agrmv.twitter.model.util;

import ru.agrmv.twitter.model.user.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author.getUsername();
    }
}

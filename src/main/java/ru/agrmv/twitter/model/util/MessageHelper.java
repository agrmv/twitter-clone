package ru.agrmv.twitter.model.util;

import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.user.User;

import java.util.Base64;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author.getUsername();
    }

    public static String getImage(File file) {
        if (file != null && file.getFileType().contains("image")) {
            return Base64.getEncoder().encodeToString(file.getData());
        }
        return null;
    }

    public static String getFileDownloadUri(File file) {
        if (file != null && !file.getFileType().contains("image")) {
            return file.getFileDownloadUri();
        }
        return null;
    }
}

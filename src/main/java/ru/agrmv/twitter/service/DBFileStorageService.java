package ru.agrmv.twitter.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.agrmv.twitter.exception.FileStorageException;
import ru.agrmv.twitter.exception.MyFileNotFoundException;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.repository.DBFileRepository;

import java.io.IOException;

@Service
public class DBFileStorageService {

    private static DBFileRepository dbFileRepository;

    public DBFileStorageService(DBFileRepository dbFileRepository) {
        DBFileStorageService.dbFileRepository = dbFileRepository;
    }

    public static File storeFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            File dbFile = new File(fileName, file.getContentType(), file.getSize(), file.getBytes());

            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public static File getFile(Integer fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}

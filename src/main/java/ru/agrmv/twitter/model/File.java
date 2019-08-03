package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс File описывает модель файла
 *
 * @author agrmv
 * */

@Entity
@Data
@NoArgsConstructor
@Table(name = "files")
public class File {
    /** Идентификатор файла */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** Имя файла */
    private String fileName;

    /** Тип файла */
    private String fileType;

    /* Размер файла */
    private Long fileSize;

    /** Ссылка для загрузки файла */
    private String fileDownloadUri;

    /** Содержимое файла будет храниться в виде байтового массива в базе данных */
    @Lob
    private byte[] data;

    /**
     * Конструктор - создание нового объекта(файла) с определенными значениями
     * @param fileName - имя файла
     * @param fileType - тип файла
     * @param data - содержимое файла в виде байтового массива
     * */
    public File(String fileName, String fileType, Long fileSize,  byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
    }
}

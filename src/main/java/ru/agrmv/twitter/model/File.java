package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String fileDownloadUri;

    /** Содержимое файла будет храниться в виде байтового массива в базе данных */
    @Lob
    private byte[] data;

    public File(String fileName, String fileType, Long fileSize,  byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
    }
}

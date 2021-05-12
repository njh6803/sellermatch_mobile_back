package com.sellermatch.process.file.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FILELIST")
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_idx")
    private Integer fileIdx;

    @Column(name = "orgin_name")
    private String orginName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;

    @Column(name = "file_reg_date")
    private Date fileRegDate;

    @Column(name = "proj_id")
    private String projId;

    @Column(name = "profile_id")
    private String projThumbnail;

}

package com.sellermatch.process.project.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PROJECTLIST")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proj_idx")
    private int projIdx;

    @Column(name = "proj_id")
    private String projId;
}

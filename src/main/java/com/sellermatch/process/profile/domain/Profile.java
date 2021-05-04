package com.sellermatch.process.profile.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PROFILELIST")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_idx")
    private int profileIdx;

    @Column(name = "profile_id")
    private String profileId;


}

package com.sellermatch.process.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sellermatch.process.profile.domain.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MemList")
@Getter
@Setter
@ToString
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_idx")
    private Integer memIdx;

    @Column(name = "mem_id")
    private String memId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "mem_pw")
    private String memPw;

    @Column(name = "mem_state")
    private String memState;

    @Column(name = "mem_class", columnDefinition = "char")
    private String memClass;

    @Column(name = "mem_class_sdate")
    private Date memClassSdate;

    @Column(name = "mem_class_edate")
    private Date memClassEdate;

    @Column(name = "mem_sort", columnDefinition = "char")
    private String memSort;

    @Column(name = "mem_country")
    private String memCountry;

    @Column(name = "mem_nation")
    private String memNation;

    @Column(name = "mem_addr")
    private String memAddr;

    @Column(name = "mem_addr2")
    private String memAddr2;

    @Column(name = "mem_post")
    private String memPost;

    @Column(name = "mem_tel")
    private String memTel;

    @Column(name = "mem_name")
    private String memName;

    @Column(name = "mem_rname", columnDefinition = "char")
    private String memRname;

    @Column(name = "mem_nick")
    private String memNick;

    @Column(name = "mem_photo")
    private String memPhoto;

    @Column(name = "mem_sns_ch")
    private String memSnsCh;

    @Column(name = "googleId")
    private String googleId;

    @Column(name = "naverId")
    private String naverId;

    @Column(name = "kakaoId")
    private String kakaoId;

    @Column(name = "mem_sns_ch_tkn")
    private String memSnsChTkn;

    @Column(name = "mem_ip")
    private String memIp;

    @Column(name = "mem_login_date")
    private Date memLoginDate;

    @Column(name = "mem_out_date")
    private Date memOutDate;

    @Column(name = "mem_date")
    private Date memDate;

    @Column(name = "mem_edit_date")
    private Date memEditDate;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "session_limit")
    private Date sessionLimit;

    @Column(name = "withdraw_auth_code")
    private String widthdrawAuthCode;

    @Transient
    private String memPwChk;

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Transient
    @Override
    public String getPassword() {
        return null;
    }

    @Transient
    @Override
    public String getUsername() {
        return null;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Transient
    public Profile profile;
}

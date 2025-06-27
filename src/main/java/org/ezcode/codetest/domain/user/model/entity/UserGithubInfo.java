package org.ezcode.codetest.domain.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_github")
@NoArgsConstructor
public class UserGithubInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private String owner;

    private String repo;

    private String branch;

    private String githubAccessToken;

    @Builder
    public UserGithubInfo(User user, String owner) {
        this.user = user;
        this.owner = owner;
    }

    public void setGithubAccessToken(String githubAccessToken){
        this.githubAccessToken = githubAccessToken;
    }

}

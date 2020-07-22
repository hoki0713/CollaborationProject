package com.mobeom.local_currency.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobeom.local_currency.board.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name="post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id", nullable = false)
    private Long postId;

    @CreationTimestamp
    @Column(name="reg_date", nullable = false)
    private DateTime regDate;

    @Column(name="category", nullable = false)
    private String category;

    @Column(name="post_title", nullable = false)
    private String postTitle;

    @Column(name="contents", nullable = false)
    private String contents;

    @Column(name="read_count", nullable = false)
    private Integer readCount;

    @CreationTimestamp
    @Column(name="modi_date", nullable = false)
    private DateTime modiDate;

    @Column(name="notice_yn", nullable = false)
    private Boolean noticeYn;

    @Column(name="delete_yn", nullable = false)
    private Boolean deleteYn;

    @Column(name="star_rating")
    private Integer starRating;

    @Column(name="comment")
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="board_id")
    @Column(name="board_id", nullable = false)
    private Board board;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name="recommend_id")
//    @Column(name="recommend_id", nullable = false)
//    private Recommend recommend;
//
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name="user_id")
//    @Column(name="user_id", nullable = false)
//    private User user;
}
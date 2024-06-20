package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

// Entity = Board 라는 클래스가 db의 테이블을 의미
@Entity
// Lombok 라이브러리를 사용하여 getter, setter, toString, equals, hashCode 메서드를 자동으로 생성함
@Data
// Board 클래스
public class Board {

    // 이 필드는 엔티티의 기본 키(primary key)임을 나타냄
    @Id
    // 사용하는 이유는 주로 데이터베이스에서 기본 키(primary key)를 자동으로 생성하고 관리하도록 하기 위함
    // 기본 키의 생성 전략을 IDENTITY로 설정하여 데이터베이스에서 자동으로 증가되는 값을 사용
    // 기존의 id = 1 에서 새로운 레코드가 추가되면 id = 2 이런식으로 자동 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Board 엔티티의 각 인스턴스를 고유하게 식별하는 기본 키 필드
    private Integer id;
    // 게시물의 제목을 저장하는 필드
    private String title;
    // 게시물의 내용을 저장하는 필드
    private String content;
    // 업로드된 파일의 이름을 저장하는 필드
    private String filename;
    // 업로드된 파일의 경로를 저장하는 필드
    private String filepath;
}

package com.study.board.repository;


import com.study.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// spring에서 데이터 접근 계층을 관리하고 예외를 변환하며, 해당 클래스가 데이터 접근 계층임을 명확히 나타내는 데 사용됨
// 데이터베이스와 상호작용하는 클래스임을 나타낼 때 사용
@Repository
// 게시판 저장보관소 인터페이스, Jpa저장보관소 인터페이스를 상속받고 있음
public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 특정 키워드와 페이지네이션 정보를 Borard 클래스를 자료형으로 가지는 Page 객체를 반환함
    // Spring Data JPA가 메서드 이름을 분석하여 적절한 쿼리를 생성하고, 특정 키워드를 포함하는 항목들을 페이징 처리하여 반환함
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}




// @Repository 어노테이션의 역할

// 자동 등록:
// @Repository를 붙이면 Spring이 이 클래스를 자동으로 인식하고,
// 프로젝트를 실행할 때 이 클래스를 자동으로 등록함
// 이렇게 하면 다른 클래스에서 이 클래스를 쉽게 사용할 수 있음


// 예외 처리:
// 데이터베이스와 관련된 작업을 할 때 문제가 발생할 수 있음 예를 들어,
// 데이터베이스에 연결할 수 없거나 쿼리가 잘못된 경우 등이 있음
// @Repository를 사용하면, 이런 데이터베이스 관련 문제들이 일관된 방식으로 처리될 수 있도록 도움


// 명확한 역할:
// @Repository를 붙이면, 이 클래스가 데이터베이스와 관련된 작업을 처리하는
// 클래스라는 것을 명확하게 알 수 있고, 코드를 읽는 사람들은 이 클래스가
// 어떤 역할을 하는지 쉽게 이해할 수 있음
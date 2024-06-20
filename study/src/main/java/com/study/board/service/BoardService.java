package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

//    JPA Repository
//    findBy(컬럼 이름) 컬럼에서 키워드를 넣어 찾음  ex) 사과 ->  사과 인 것을 찾음
//    findBy(컬럼 이름)containing 컬럼에서 키워드가 포함된 것을 찾음  ex) 사과 -> 사과가 포함된 모든것을 찾음



    // 필요한 의존 "객체"의 타입에 해당하는 빈을 찾아 주입함, (생성자, setter, 필드)
    // Autowired는 기본값이 true 이기 떄문에 의존성 주입을 할 대상을 찾지 못한다면 애플리케이션 구동에 실패함.
    // @Autowired 어노테이션을 사용하면, Spring 컨테이너가 자동으로 의존성을 주입해주므로,
    // 개발자가 수동으로 객체를 생성하거나 주입할 필요가 없음
    @Autowired
    // 게시판 데이터관리 저장소 필드
    // @Autowired 어노테이션을 이용하여 Spring이 해당 타입의 빈을 자동으로 주입해줌
    // 이를 통해 해당 클래스에서 BoardService 객체를 사용할 수 있게 됨
    private BoardRepository boardRepository;  // = new 를 해서 생성하는데 어노테이션이 자동으로 해줌

    // 글 작성 처리

    // Board 는 entity패키지에 class로 존재.
    // MultipartFile 은 파일 업로드를 처리하기 위해 Spring에서 제공하는 인터페이스, 업로드된 파일을 쉽게 다룰 수 있음
    // 파일 업로드는 일반적인 요청 매개변수와 다르게 HTTP 요청의 멀티파트 폼 데이터로 전달되기 때문에,
    // 이를 명시적으로 처리하기 위해 @RequestParam 어노테이션을 사용함
    // 이 어노테이션은 요청 파라미터의 이름을 지정하여 해당 이름의 파일 데이터를 MultipartFile 객체로 바인딩함
    public void write(Board board, MultipartFile file) throws Exception {

        // 파일 업로드시 프로젝트 경로 설정: 현재 작업 디렉토리의 경로를 가져와 파일 저장 경로를 설정    현재 디렉토리경로 : C:\Users\gwswj\Desktop\Study\board
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        // 고유 식별자(UUID)를 생성하여 파일 이름 중복을 방지
        // UUID(Universally Unique Identifier)는 범용 고유 식별자를 의미하며, 전 세계적으로 고유한 값을 생성하기 위해 사용됨
        // UUID는 주로 데이터베이스의 기본 키, 파일 이름, 네트워크 식별자 등 고유해야 하는 항목에 사용됨
        // UUID는 128비트 숫자로 표현되며, 일반적으로 32개의 16진수 문자와 4개의 하이픈(-)으로 구성된 문자열 형태로 나타냄
        // 예를 들어, 550e8400-e29b-41d4-a716-446655440000 같은 형식
        UUID uuid = UUID.randomUUID();
        // 고유 식별자와 원본 파일 이름을 결합하여 저장할 파일 이름 생성
        String fileName = uuid + "_" + file.getOriginalFilename();
        // 저장할 파일 객체 생성: 프로젝트 경로와 파일 이름을 결합하여 파일 객체 생성
        File saveFile = new File(projectPath, fileName);

        // 업로드된 파일을 지정한 경로에 저장
        file.transferTo(saveFile);
        // Board 객체에 파일 이름 설정
        board.setFilename(fileName);
        // Board 객체에 파일 경로 설정
        board.setFilepath("/files/" + fileName);
        // Board 객체를 데이터베이스에 저장     boardRepository = BoardRepositoy 인터페이스의 인스턴스.
        boardRepository.save(board);
    }

    // 게시글 리스트 처리

    // Page 인터페이스가 Board 클래스 타입의 데이터를 반환함
    // pageable은 페이지네이션 정보를 담고 있는 객체 (페이지 번호, 크기, 정렬 정보 등)
    public Page<Board> boardList(Pageable pageable) {

        // Page<Board> 페이지네이션된 Board 객체의 리스트를 반환
        return boardRepository.findAll(pageable);
    }

    // 특정 게시글 불러오기

    // Board 클래스 타입으로 반환함
    public Board boardView(Integer id) {
        // 주어진 ID를 사용하여 특정 게시글을 가져옴
        // findById(id) 메서드는 주어진 id에 해당하는 Board 엔티티를 데이터베이스에서 검색함
        // 이 메서드는 Optional<Board> 타입을 반환함 Optional은 결과가 있을 수도 있고 없을 수도 있는 값을 표현하는 컨테이너 객체
        // get() 메서드는 Optional<Board> 객체에서 실제 Board 객체를 가져오는 메서드
        // get() 메서드는 Optional 객체가 비어있을 경우(NoSuchElementException) 예외를 던짐
        // 이 방법은 Optional 객체가 반드시 값을 가지고 있다고 확신할 때만 사용해야 함
        return boardRepository.findById(id).get();
    }

    // 제목에 특정 키워드가 포함된 게시글 불러오기

    // Page 인터페이스가 Board 클래스 타입의 데이터를 반환함
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        // 데이터베이스에서 제목에 searchKeyword가 포함된 Board 엔티티들을 검색하고, 그 결과를 Page<Board> 형태로 반환함 (Board 객체의 리스트와 페이지네이션)
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 삭제

    // 반환값이 없는 게시판삭제 메서드
    public void boardDelete(Integer id) {
        // 주어진 ID를 사용하여 특정 게시글을 삭제
        boardRepository.deleteById(id);
    }
}

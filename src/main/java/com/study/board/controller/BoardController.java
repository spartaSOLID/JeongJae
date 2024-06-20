package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;



// 해당 클래스가 Controller임을 나타내기 위한 어노테이션
// View를 반환하기위해 사용, Spring MVC Container는 Client의 요청으로부터 View를 반환
// Data를 반환해야 하는 경우도 있음 @ResponseBody 어노테이션을 활용해 Json 형테로 Data를 반환 할 수 있음
@Controller
// 게시판컨트롤러 클래스
public class BoardController {

    // 필요한 의존 "객체"의 타입에 해당하는 빈을 찾아 주입함, (생성자, setter, 필드)
    // Autowired는 기본값이 true 이기 떄문에 의존성 주입을 할 대상을 찾지 못한다면 애플리케이션 구동에 실패함.
    // @Autowired 어노테이션을 사용하면, Spring 컨테이너가 자동으로 의존성을 주입해주므로,
    // 개발자가 수동으로 객체를 생성하거나 주입할 필요가 없음
    @Autowired
    // 게시판서비스 필드
    // @Autowired 어노테이션을 이용하여 Spring이 해당 타입의 빈을 자동으로 주입해줌
    // 이를 통해 해당 클래스에서 BoardService 객체를 사용할 수 있게 됨
    private BoardService boardService;

    // 생성자를 통해 boardService 주입  주입? 객체를 생성할 떄 필요한 의존성을 생정자의 매개변수로 전달하여 주입하는 것을 의미
    public BoardController(BoardService boardService) {
        // 게시판서비스 필드와 매개변수를 구분하기 위해 this를 사용하므로써 게시판서비스 필드를 초기화함
        this.boardService = boardService;
    }

    // Spring Application 구동을 위해 refresh과정에서 많은 Bean 들이 생성되고, 그중 하나가
    // RequestMappingHandlerMapping  이 Bean은 @RequestMapping 으로 등록한 메서드 들을 가지고 있다가
    // 요청이 들어오면 Mapping해주는 역할을 수행함

    //                       이름        타입                        설명
    // RequestMapping 속성에 value      String[]           URL 값으로 매핑 조건을 부여(default)
    //                       method     RequestMethod[]    HTTP Request 메소드 값을 매핑 조건으로 부여
    //                                                     사용 가능한 메소드는 GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE 등
    //                       params     Stirng[]           HTTP Request 파라미터를 매핑 조건으로 부여
    //                       consumes   String[]           설정과 Content-Type request 헤더가 일치할 경우에만 URL이 호출됨
    //                       produces   String[]           설정과 Accept request 헤더가 일치할 경우에만 URL이 호출됨

    // @RequestMapping 은 Class와 Method 에 붙일수 있고
    // @GetMappin, @PostMapping, @PutMapping, @DeleteMapping 등은 Method 에만 붙일 수 있음


    // 게시글 작성

    // Get : URL에 데이터를 포함시켜 요청하고 데이터를 헤더에 포함하여 전송.
    // 클라이언트에서 /board/write URL로 GET 요청이 들어오면 메서드가 실행
    @GetMapping("/board/write") //localgost:8080/board/write
    // 글쓰기양식 메서드
    public String boardWriteForm() {
        // veiw를 반환하는데 여기서는 veiw ->  boardwrite 파일을 반환.
        return "boardwrite";
    }

    // 게시글 작성 처리

    // Post : URL에 데이터를 노출하지 않고 요청, 데이터를 바디에 포함, 캐싱 불가
    // 클라이언트에서 /board/writepro URL로 POST 요청이 들어오면 이 메소드가 실행됩니다.
    @PostMapping("/board/writepro")
    // Board 는 entity패키지에 class로 존재.
    // Model은 Spring Framework에서 제공하는 인터페이스로, 컨트롤러에서 뷰로 데이터를 전달하는 데 사용됨
    // Model 인터페이스는 Spring MVC 패키지 (org.springframework.ui.Model)에 속해 있음
    public String boardWritePro(Board board, Model model,
                                // MultipartFile 은 파일 업로드를 처리하기 위해 Spring에서 제공하는 인터페이스, 업로드된 파일을 쉽게 다룰 수 있음
                                // 파일 업로드는 일반적인 요청 매개변수와 다르게 HTTP 요청의 멀티파트 폼 데이터로 전달되기 때문에,
                                // 이를 명시적으로 처리하기 위해 @RequestParam 어노테이션을 사용함
                                // 이 어노테이션은 요청 파라미터의 이름을 지정하여 해당 이름의 파일 데이터를 MultipartFile 객체로 바인딩함
                                @RequestParam("file") MultipartFile file) throws Exception{
        // board와 파일을 사용하여 게시물을 작성하는 서비스 메서드 호출
        boardService.write(board, file);
        // 모델에 메시지를 추가하여 글 작성 완료 메시지 설정
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        // 모델에 이동할 URL을 추가하여 게시물 목록 페이지로 이동 설정
        model.addAttribute("searchUrl", "/board/list");
        // message 뷰를 반환하여 메세지를 사용자에게 표시
        return "message";
    }

    // 게시글 리스트

    // 클라이언트에서 이 경로로 GET 요청이 들어오면 메서드를 실행하라는 의미
    @GetMapping("/board/list")
    // Model은 Spring Framework에서 제공하는 인터페이스로, 컨트롤러에서 뷰로 데이터를 전달하는 데 사용됨
    // Model 인터페이스는 Spring MVC 패키지 (org.springframework.ui.Model)에 속해 있음
    public String boardList(Model model,
                            // @PageableDefault 어노테이션은 기본 페이지 설정을 정의할때 사용
                            // 페이지네이션(대량의 데이터를 여러페이지로 나누어 보여주는 기술)과 정렬 기본값을 설정함  (페이지:0 크기:10 정렬:id기준 내림차순)
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            // @RequestParam 이 어노테이션은 HTTP 요청의 파라미터를 메소드의 파라미터에 바인딩함
                            // 클라이언트가 보낸 쿼리 스트링, 폼 데이터, URL 경로 변수 등에서 값을 추출하여 메소드 파라미터에 전달함
                            // 요청 파라미터로부터 searchKeyword 를 받음 이 파라미터는 필수가 아니므로 값이 없을 수 있음 (null값을 가짐)
                            // value 속성은 요청 파라미터의 이름을 지정함,  "searchKeyword" 라는 이름의 요청 파라미터를 메소드의 searchKeyword 변수에 바인딩함
                            // 예를 들어, 클라이언트가 /board/list?searchKeyword=spring 이라는 요청을 보낸 경우, searchKeyword 변수에는 "spring" 값이 할당됨
                            @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {

        // Board 엔티티의 페이지 객체를 담을 변수,  Page 인터페이스를 사용하여 Board 객체의 페이지를 나타내는 변수
        Page<Board> list;
        // 검색어가 없거나 비어있으면 전체 게시물 리스트를 가져옴   boardService = BoardService 클래스의 인스턴스.
        if(searchKeyword == null || searchKeyword.isEmpty()) {
            list = boardService.boardList(pageable);
        // 그렇지 않으면 검색 결과 리스트를 가져옴
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }
        // 현재 페이지 번호 (0부터 시작하므로 +1)
        int nowPage = list.getPageable().getPageNumber() + 1;
        // 페이지네이션 시작 페이지 번호 (최소 1) 현재 페이지가 6일경우 앞쪽으로 4 만큼 보여줌  2,3,4,5,6=nowpage
        int startPage = Math.max(nowPage - 4,1);
        // 페이지네이션 끝 페이지 번호 (총 페이지 수를 초과하지 않음) 전체 페이지가 100일 경우  현재페이지가 97이면 102가 아니라 100이 된다는 뜻
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        // 모델에 리스트와 페이지네이션 정보를 추가
        model.addAttribute("list", list); // 리스트
        model.addAttribute("nowPage", nowPage); // 현재 페이지
        model.addAttribute("startPage", startPage); // 시작 페이지
        model.addAttribute("endPage", endPage); // 끝 페이지

        // boardlist 뷰를 반환하여 게시판 리스트를 보여줌
        return "boardlist";
    }

    // 게시글 내용보기
    // 클라이언트에서 /board/view 경로로 GET 요청이 들어오면 메서드를 실행
    @GetMapping("/board/view")        // localgost:8080/board/view?id=1
    public String boardView(Model model, @RequestParam("id") Integer id) {
        // id 파라미터로 전달된 게시물 ID를 이용해 해당 게시물의 내용을 조회하여 모델에 추가
        model.addAttribute("board", boardService.boardView(id));
        // boardview 뷰를 반환하여 해당 뷰에서 게시물 내용을 표시
        return "boardview";

    }

    // 게시글 삭제
    // 클라이언트에서 /board/delete 경로로 GET 요청이 들어오면 메서드를 실행
    @GetMapping("/board/delete")
    // HTTP 요청 파라미터에서 id 값을 가져와 id 변수에 바인딩함 여기서 id는 삭제할 게시물의 고유 식별자,
    public String boardDelete(@RequestParam("id") Integer id) {
        // id 파라미터로 전달된 게시물 ID를 이용해 해당 게시물을 삭제하는 서비스 메서드 호출
        boardService.boardDelete(id);
        // 게시물 목록 페이지로 리다이렉트
        return "redirect:/board/list";
    }

    // 게시글 수정
    // 클라이언트에서 /board/modify/{id} 경로로 GET 요청이 들어오면 메서드를 실행
    @GetMapping("/board/modify/{id}")
    // @PathVariable 어노테이션을 사용하는 이유는 URL 경로에 포함된 변수를 메소드의 파라미터로 바인딩하기 위함
    // 클라이언트가 요청한 URL의 일부를 메소드에 직접 전달하여 해당 값을 이용할 수 있음
    // @PathVariable("id") Integer id: URL 경로에서 id 값을 가져와 id 변수에 바인딩함 여기서 id는 수정할 게시물의 고유 식별자,
    // @PathVariable 어노테이션은 경로 변수를 쉽게 처리하고, RESTful API 설계를 따르며, 가독성을 높이는 데 도움이 됨
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        // id 경로 변수로 전달된 게시물 ID를 이용해 해당 게시물의 내용을 조회하여 모델에 추가
        model.addAttribute("board", boardService.boardView(id));
        // boardmodify 뷰를 반환하여 해당 뷰에서 게시물 수정 폼을 표시
        return "boardmodify";
    }

    // 게시글 수정처리
    // 클라이언트에서 /board/update/{id} 경로로 POST 요청이 들어오면 메서드를 실행
    @PostMapping("/board/update/{id}")
    // String 자료형으로 반환함
    public String boardUpdate (@PathVariable("id") Integer id, Board board, Model model, @RequestParam("file") MultipartFile file) throws Exception {
        // id 경로 변수로 전달된 게시물 ID를 이용해 해당 게시물의 내용을 조회
        Board boardTemp = boardService.boardView(id);
        // 요청으로 전달된 Board 객체의 제목을 기존 게시물에 반영
        boardTemp.setTitle(board.getTitle());
        // 요청으로 전달된 Board 객체의 내용을 기존 게시물에 반영
        boardTemp.setContent(board.getContent());

        // 업데이트된 게시물 정보와 파일을 저장
        boardService.write(boardTemp, file);

        // 모델에 메시지를 추가하여 수정 완료 메시지 설정
        model.addAttribute("message", "글 작성이 수정되었습니다..");
        // 모델에 리다이렉트할 URL을 추가하여 게시물 목록 페이지로 이동할 수 있도록 설정
        model.addAttribute("searchUrl", "/board/list");

        // message 뷰를 반환하여 수정 완료 메시지를 사용자에게 표시
        return "message";
    }
}
말을 다듬는 연습
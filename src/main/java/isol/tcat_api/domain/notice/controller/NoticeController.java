package isol.tcat_api.domain.notice.controller;

import isol.tcat_api.domain.notice.dto.NoticeDTO;
import isol.tcat_api.domain.notice.service.NoticeService;
import isol.tcat_api.global.entity.CommonRequest;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 목록 검색
     * @param noticeTp 공지사항 타입 - 0: 공지사항 | 1: 이벤트
     * @param noticeTitle 공지사항 제목
     * @param commonRequest 검색 조건이 담긴 객체
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResponse<?>> getList(
            @RequestParam(name = "noticeTp", required = false) String noticeTp,
            @RequestParam(name = "noticeTitle", required = false) String noticeTitle,
            @ModelAttribute CommonRequest commonRequest) {
        NoticeDTO.List<NoticeDTO.Item> list = noticeService.getList(noticeTp, noticeTitle, commonRequest);

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    /**
     * 메인화면 공지사항 리스트
     * @param noticeTp 공지사항 타입 - 0: 공지사항 | 1: 이벤트
     */
    @GetMapping("/list/main/{noticeTp}")
    public ResponseEntity<CommonResponse<?>> getMainList(@PathVariable(value = "noticeTp") int noticeTp) {
        NoticeDTO.List<NoticeDTO.Item> list = noticeService.getMainList(noticeTp);

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    /**
     * 메인화면 이벤트 배너 리스트
     */
    @GetMapping("/banner-list")
    public ResponseEntity<CommonResponse<?>> getMainBannerList() {
        NoticeDTO.List<NoticeDTO.Item> list = noticeService.getMainBannerList();

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    /**
     * 공지사항 단일 검색
     * @param noticeIdx 공지사항 PK
     */
    @GetMapping("/{noticeIdx}")
    public ResponseEntity<CommonResponse<?>> getItem(@PathVariable(value = "noticeIdx") long noticeIdx) {
        NoticeDTO.Item item = noticeService.getItem(noticeIdx);

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(item)
                .build(), HttpStatus.OK);
    }

}

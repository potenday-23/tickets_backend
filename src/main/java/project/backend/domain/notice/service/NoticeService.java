package project.backend.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.notice.dto.NoticePostRequestDto;
import project.backend.domain.notice.dto.NoticePatchRequestDto;
import project.backend.domain.notice.entity.Notice;
import project.backend.domain.notice.mapper.NoticeMapper;
import project.backend.domain.notice.repository.NoticeRepository;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    public Notice createNotice(NoticePostRequestDto noticePostRequestDto){
        Notice notice = Notice.builder()
                .title(noticePostRequestDto.getTitle())
                .content(noticePostRequestDto.getContent()).build();
        noticeRepository.save(notice);
        return notice;
    }

    public Notice getNotice(Long id) {
        return verifiedNotice(id);
    }

    public List<Notice> getNoticeList() {
        return noticeRepository.findAll();
    }

    public Notice patchNotice(Long id, NoticePatchRequestDto noticePatchRequestDto) {
        Notice notice = verifiedNotice(id).patchNotice(noticePatchRequestDto);
        noticeRepository.save(notice);
        return notice;
    }

    public void deleteNotice(Long id) {
        noticeRepository.delete(verifiedNotice(id));
    }

    private Notice verifiedNotice(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}

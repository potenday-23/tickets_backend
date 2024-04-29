package project.backend.domain.memberTicketLike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.entity.Member;
import project.backend.domain.memberTicketLike.entity.MemberTicketLike;
import project.backend.domain.memberTicketLike.repository.MemberTicketLikeRepository;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.ticket.service.TicketService;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberTicketLikeService {
    private final MemberTicketLikeRepository memberTicketLikeRepository;
    private final TicketService ticketService;
    private final JwtService jwtService;

    public Boolean changeMemberTicketLike(Long ticketId, String accessToken){

        // member와 ticket조회
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        Ticket ticket = ticketService.verifiedTicket(ticketId);
        List<MemberTicketLike> memberTicketLikeList = memberTicketLikeRepository.findByMemberAndTicket(member, ticket);

        // 찜하기 삭제
        if (memberTicketLikeList.size() > 0) {
            memberTicketLikeRepository.deleteAll(memberTicketLikeList);
            return false;
        }

        // 찜하기 생성
        else {
            MemberTicketLike memberTicketLike = MemberTicketLike.builder()
                    .member(member)
                    .ticket(ticket).build();
            memberTicketLike.setMemberTicket(member, ticket);
            memberTicketLikeRepository.save(memberTicketLike);
            return true;
        }
    }

    public Boolean getMemberTicketLike(Long ticketId, String accessToken){

        // member와 ticket조회
        Member member = jwtService.getMemberFromAccessToken(accessToken);
        Ticket ticket = ticketService.verifiedTicket(ticketId);
        List<MemberTicketLike> memberTicketLikeList = memberTicketLikeRepository.findByMemberAndTicket(member, ticket);

        // 찜하기 상태
        if (memberTicketLikeList.size() > 0) {
            return true;
        }

        // 찜 안하기 상태
        else {
            return false;
        }
    }

    public List<Ticket> getMemberTicketLikeList(String accessToken) {
        List<MemberTicketLike> memberTicketLikeList = jwtService.getMemberFromAccessToken(accessToken).getMemberTicketLikes();
        List<Ticket> ticketList = new ArrayList<>();
        for (MemberTicketLike memberTicketLike : memberTicketLikeList) {
            ticketList.add(memberTicketLike.ticket);
        }
        return ticketList;
    }


    public void deleteMemberTicketLike(Long id) {
        memberTicketLikeRepository.delete(verifiedMemberTicketLike(id));
    }

    private MemberTicketLike verifiedMemberTicketLike(Long id) {
        return memberTicketLikeRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

}

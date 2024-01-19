package project.backend.domain.ticket.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.backend.domain.jwt.service.JwtService;
import project.backend.domain.member.entity.Member;
import project.backend.domain.memberTicketLike.service.MemberTicketLikeService;
import project.backend.domain.ticket.dto.TicketPatchRequestDto;
import project.backend.domain.ticket.dto.TicketPostRequestDto;
import project.backend.domain.ticket.dto.TicketStatusPostRequestDto;
import project.backend.domain.ticket.entity.Ticket;
import project.backend.domain.ticket.mapper.TicketMapper;
import project.backend.domain.ticket.service.TicketService;
import project.backend.domain.ticket.dto.TicketResponseDto;
import project.backend.global.error.exception.BusinessException;
import project.backend.global.error.exception.ErrorCode;
import project.backend.global.s3.service.ImageService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

import static project.backend.global.validator.LocalDateTimeValidation.convertStringToLocalDateTime;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Api(tags = "티켓 API")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final ImageService imageService;
    private final JwtService jwtService;
    private final MemberTicketLikeService memberTicketLikeService;


    @ApiOperation(
            value = "티켓 생성하기",
            notes = " - Header['Authorization'] : AccessToken값 입력\n" +
                    " - image : MultipartFile 입력(사용자가 추가한 이미지)\n" +
                    " - request : {\n" +
                    "    \"title\" : \"레미제라블\",\n" +
                    "    \"ticketDate\" : \"2023-11-04T16:26:39.098\",\n" +
                    "    \"rating\" : 1,\n" +
                    "    \"memo\" : \"재미없는 공연이였다.(100자 이내 입력)\",\n" +
                    "    \"seat\" : \"E292\",\n" +
                    "    \"location\" : \"서울시 서울스퀘어\",\n" +
                    "    \"price\" : 15000,\n" +
                    "    \"friend\" : \"김가영\",\n" +
                    "    \"color\" : \"#102920\"\n" +
                    "    \"isPrivate\" : \"PUBLIC\",\n" +
                    "    \"categoryName\" : \"기타\"\n" +
                    "    \"ticketType\" : \"A\"\n" +
                    "    \"layoutType\" : \"B\"\n" +
                    "}\n" +
                    "1. ticketDate는 다음과 같은 형식으로 추가해주세요\n" +
                    "2. isPrivate는 PUBLIC, PRIVATE으로만 추가 가능합니다.\n" +
                    "3. rating은 별점으로, 0 ~ 5까지 소수점으로 입력할 수 있습니다.(n.5 권장)\n" +
                    "4. request는 application/json 형식입니다.\n" +
                    "5. 필수 : 제목, 날짜, 메모, 별점, 이미지, 카테고리, 레이아웃 타입")
    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity postTicket(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Valid @RequestPart(required = false) TicketPostRequestDto request) {

        if (ObjectUtils.isEmpty(accessToken) || ObjectUtils.isEmpty(request) || (ObjectUtils.isEmpty(image) && ObjectUtils.isEmpty(request.getImageUrl()))){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        // TicketDate 형식 검사 (ex - 2023-11-01T00:00:00)
        request.setTicketLocalDateTime(convertStringToLocalDateTime(request.getTicketDate()));

        if (!ObjectUtils.isEmpty(image)) {
            request.setImageUrl(imageService.updateImage(image, "Ticket", "imageUrl"));
        }

        // 작성자 등록
        request.setMember(jwtService.getMemberFromAccessToken(accessToken));

        // Ticket 생성
        Ticket ticket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketMapper.ticketToTicketResponseDto(ticket));
    }

    @ApiOperation(
            value = "티켓 조회하기 - 내 티켓 혹은 전체 공개 티켓만 조회 가능",
            notes = " - ticketId는 필수\n" +
                    " - Authorization Header는 선택")
    @GetMapping("/{ticketId}")
    public ResponseEntity getTicket(
            @Positive @PathVariable(required = false) Long ticketId,
            @RequestHeader(value = "Authorization", required = false) String accessToken) {

        if (ObjectUtils.isEmpty(ticketId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        Ticket ticket = ticketService.getTicket(ticketId, accessToken);
        TicketResponseDto ticketResponseDto = ticketMapper.ticketToTicketResponseDto(ticket);
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDto);
    }

    /**
     * 회원 인증 받지 않아도 조회 가능한 api
     *
     * @return
     */
    @ApiOperation(
            value = "둘러보기 티켓 조회 - 전체 공개만 ",
            notes = " - ?categorys=영화,뮤지컬\n" +
                    " - &period=week    **[week, month, 6month, day로 조회 가능]**\n" +
                    " - &start=2023-11-03\n" +
                    " - &end=2023-11-05\n" +
                    " - &search=레미제라블\n" +
                    "- Header['Authorization'] : 토큰 값\n" +
                    "1. Authorization과 categorys를 입력할 경우, 유저의 온보딩 카테고리보다 categorys로 입력한 카테고리가 필터의 우선순위를 가집니다.\n" +
                    "2. start, end가 period보다 우선순위를 가집니다.\n" +
                    "3. start, end 두 값을 동시에 적지 않으면 filter 기능이 동작하지 않습니다.(에러는 발생하지 않습니다.)\n" +
                    "4. 전체 파라미터와 헤더는 필수 값이 아닙니다.")
    @GetMapping("/total")
    public ResponseEntity getTotalTicketList(
            @RequestParam(value = "categorys", required = false) List<String> categorys,
            @RequestParam(value = "period", required = false) String period, // 일주일(week), 한달(month), 6개월(6month), 하루(day)
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "search", required = false) String search,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {

        if (categorys == null && accessToken != null) {
            categorys = jwtService.getMemberFromAccessToken(accessToken).getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList());
        }

        List<Ticket> ticketList = ticketService.getTotalTicketList(categorys, period, start, end, search == null ? "" : search);
        List<TicketResponseDto> ticketResponseDtoList = ticketMapper.ticketsToTicketResponseDtos(ticketList);

        // 좋아요 여부 추가
        if (accessToken != null) {
            for (TicketResponseDto ticketResponseDto : ticketResponseDtoList) {
                ticketResponseDtoList.forEach(t -> t.setIsLike(memberTicketLikeService.getMemberTicketLike(t.getId(), accessToken)));
            }
        } else {
            ticketResponseDtoList.forEach(t -> t.setIsLike(false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoList);
    }

    @ApiOperation(
            value = "둘러보기 티켓 조회 - 전체 티켓(전체 공개) & 내 티켓(전체 공개, 비공개)만 ",
            notes = " - ?categorys=영화,뮤지컬\n" +
                    " - &period=week    **[week, month, 6month, day로 조회 가능]**\n" +
                    " - &start=2023-11-03\n" +
                    " - &end=2023-11-05\n" +
                    " - &search=레미제라블\n" +
                    "- Header['Authorization'] : 토큰 값\n" +
                    "1. Authorization과 categorys를 입력할 경우, 유저의 온보딩 카테고리보다 categorys로 입력한 카테고리가 필터의 우선순위를 가집니다.\n" +
                    "2. start, end가 period보다 우선순위를 가집니다.\n" +
                    "3. start, end 두 값을 동시에 적지 않으면 filter 기능이 동작하지 않습니다.(에러는 발생하지 않습니다.)\n" +
                    "4. Authorization은 필수 값이고, 나머지는 모두 필수 파라미터가 아닙니다.")
    @GetMapping("/my-total")
    public ResponseEntity getTotalAndMyTicketList(
            @RequestParam(value = "categorys", required = false) List<String> categorys,
            @RequestParam(value = "period", required = false) String period, // 일주일(week), 한달(month), 6개월(6month), 하루(day)
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "search", required = false) String search,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {
        if (ObjectUtils.isEmpty(accessToken)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        Member member = jwtService.getMemberFromAccessToken(accessToken);

        if (categorys == null) {
            categorys = member.getOnboardingMemberCategories().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList());
        }

        List<Ticket> ticketList = ticketService.getTotalAndMyTicketList(categorys, period, start, end, search, member);
        List<TicketResponseDto> ticketResponseDtoList = ticketMapper.ticketsToTicketResponseDtos(ticketList);

        // 좋아요 여부 추가
        ticketResponseDtoList.forEach(t -> t.setIsLike(memberTicketLikeService.getMemberTicketLike(t.getId(), accessToken)));
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoList);
    }

    /**
     * 회원 인증 받지 않아도 조회 가능한 api
     *
     * @return
     */
    @ApiOperation(
            value = "내 티켓 조회 - 내 티켓만 ",
            notes = " - ?categorys=영화,뮤지컬\n" +
                    " - &period=week    **[week, month, 6month, day로 조회 가능]**\n" +
                    " - &start=2023-11-03\n" +
                    " - &end=2023-11-05\n" +
                    " - &search=레미제라블\n" +
                    "- Header['Authorization'] : 토큰 값\n" +
                    "1. start, end가 period보다 우선순위를 가집니다.\n" +
                    "2. start, end 두 값을 동시에 적지 않으면 filter 기능이 동작하지 않습니다.(에러는 발생하지 않습니다.)\n" +
                    "4. 전체 파라미터는 필수 값이 아니고, 헤더는 필수 값입니다.")
    @GetMapping("/my")
    public ResponseEntity getMyTicketList(
            @RequestParam(value = "categorys", required = false) List<String> categorys,
            @RequestParam(value = "period", required = false) String period, // 일주일(week), 한달(month), 6개월(6month), 하루(day)
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "search", required = false) String search,
            @RequestHeader(value = "Authorization", required = false) String accessToken
    ) {
        if (ObjectUtils.isEmpty(accessToken)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        List<Ticket> ticketList = ticketService.getMyTicketList(categorys, period, start, end, search, jwtService.getMemberFromAccessToken(accessToken));
        List<TicketResponseDto> ticketResponseDtoList = ticketMapper.ticketsToTicketResponseDtos(ticketList);

        // 좋아요 여부 추가
        ticketResponseDtoList.forEach(t -> t.setIsLike(memberTicketLikeService.getMemberTicketLike(t.getId(), accessToken)));

        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoList);
    }

    @ApiOperation(
            value = "티켓 수정하기 - 내 티켓만 수정 가능",
            notes = " - Header['Authorization'] : AccessToken값 입력\n" +
                    " - image : MultipartFile 입력(사용자가 추가한 이미지)\n" +
                    " - request : {\n" +
                    "    \"title\" : \"레미제라블\",\n" +
                    "    \"ticketDate\" : \"2023-11-04T16:26:39.098\",\n" +
                    "    \"rating\" : 1,\n" +
                    "    \"memo\" : \"재미없는 공연이였다.(100자 이내 입력)\",\n" +
                    "    \"seat\" : \"E292\",\n" +
                    "    \"location\" : \"서울시 서울스퀘어\",\n" +
                    "    \"price\" : 15000,\n" +
                    "    \"friend\" : \"김가영\",\n" +
                    "    \"color\" : \"#102920\"\n" +
                    "    \"isPrivate\" : \"PUBLIC\",\n" +
                    "    \"categoryName\" : \"기타\"\n" +
                    "    \"ticketType\" : \"B유형\"\n" +
                    "    \"layoutType\" : \"A유형\"\n" +
                    "}\n" +
                    "1. ticketDate는 다음과 같은 형식으로 추가해주세요\n" +
                    "2. isPrivate는 PUBLIC, PRIVATE으로만 추가 가능합니다.\n" +
                    "3. rating은 별점으로, 0 ~ 5까지 소수점으로 입력할 수 있습니다.(n.5 권장)\n" +
                    "4. request는 application/json 형식입니다.\n" +
                    "5. 필수 : 제목, 날짜, 메모, 별점, 이미지, 카테고리, 레이아웃 타입")
    @PatchMapping("/{ticketId}")
    public ResponseEntity patchTicket(
            @Positive @PathVariable(required = false) Long ticketId,
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Valid @RequestPart(required = false) TicketPatchRequestDto request) {

        if (ObjectUtils.isEmpty(ticketId) || ObjectUtils.isEmpty(accessToken) || ObjectUtils.isEmpty(request)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        // TicketDate 형식 검사 (ex - 2023-11-01T00:00:00)
        request.setTicketLocalDateTime(convertStringToLocalDateTime(request.getTicketDate()));

        // image
        if (image != null) {
            request.setImageUrl(imageService.updateImage(image, "Ticket", "imageUrl"));
        }

        // Ticket 수정
        Ticket ticket = ticketService.patchTicket(ticketId, request, accessToken);

        // Ticket -> ResponseDto
        TicketResponseDto ticketResponseDto = ticketMapper.ticketToTicketResponseDto(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketResponseDto);
    }

    @ApiOperation(
            value = "티켓 삭제하기 - 내 티켓만 삭제 가능",
            notes = "Header의 Authorization 필수")
    @DeleteMapping("/{ticketId}")
    public ResponseEntity deleteTicket(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @PathVariable(required = false) Long ticketId) {
        if (ObjectUtils.isEmpty(accessToken) || ObjectUtils.isEmpty(ticketId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        ticketService.deleteTicket(ticketId, jwtService.getMemberFromAccessToken(accessToken));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ApiOperation(
            value = "티켓 나만보기 <-> 전체보기",
            notes = "Header의 Authorization 필수")
    @PostMapping("/{ticketId}/status")
    public ResponseEntity postTicketStatus(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestBody TicketStatusPostRequestDto ticketStatusPostRequestDto,
            @PathVariable(required = false) Long ticketId) {
        if (ObjectUtils.isEmpty(accessToken) || ObjectUtils.isEmpty(ticketId) || ObjectUtils.isEmpty(ticketStatusPostRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }

        Ticket ticket = ticketService.postTicketStatus(ticketId, jwtService.getMemberFromAccessToken(accessToken), ticketStatusPostRequestDto);
        TicketResponseDto ticketResponseDto = ticketMapper.ticketToTicketResponseDto(ticket);
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDto);
    }
}

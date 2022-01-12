package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;
import org.zerock.guestbook.repository.MemberRepository;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository guestbookRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO-----------------------");
        log.info(dto.toString());

        Member writer = this.memberRepository.findById(dto.getWriterEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found Writer"));

        Guestbook entity = dtoToEntity(dto, writer);
        log.info(entity.toString());

        guestbookRepository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = this.getSearchCondition(pageRequestDTO);
        Page<Guestbook> result = this.guestbookRepository.findAll(booleanBuilder, pageable);

        return new PageResultDTO<>(result, this::entityToDto);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        return this.guestbookRepository.findById(gno)
                .map(this::entityToDto)
                .orElse(null);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        this.guestbookRepository.findById(dto.getGno())
                .ifPresent(entity -> this.update(entity, dto));
    }

    @Override
    public void remove(Long gno) {
        this.guestbookRepository.deleteById(gno);
    }

    private void update(Guestbook entity, GuestbookDTO dto) {
        entity.changeTitle(dto.getTitle());
        entity.changeContent(dto.getContent());

        this.guestbookRepository.save(entity);
    }

    private BooleanBuilder getSearchCondition(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        QGuestbook qGuestbook = QGuestbook.guestbook;
        BooleanBuilder booleanBuilder = getBooleanBuilder(qGuestbook);

        if (isStringEmpty(type)) {
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = getConditionBuilder(type, qGuestbook, keyword);
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

    private BooleanBuilder getBooleanBuilder(QGuestbook qGuestbook) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression expression = qGuestbook.gno.gt(0L);

        booleanBuilder.and(expression);
        return booleanBuilder;
    }

    private BooleanBuilder getConditionBuilder(String type, QGuestbook qGuestbook, String keyword) {
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if (type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.name.contains(keyword));
        }
        return conditionBuilder;
    }

    private boolean isStringEmpty(String type) {
        return Objects.isNull(type) || type.trim().length() == 0;
    }
}

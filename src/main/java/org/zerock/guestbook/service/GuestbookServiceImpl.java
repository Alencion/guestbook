package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO-----------------------");
        log.info(dto.toString());

        Guestbook entity = dtoToEntity(dto);
        log.info(entity.toString());

        guestbookRepository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Function<Object[], GuestbookDTO> fn = (en -> entityToDto((Guestbook) en[0], (Member) en[1]));

        Page<Object[]> result = this.guestbookRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("gno").descending())
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Object result = this.guestbookRepository.getGuestbookByGno(gno);

        Object[] arr = (Object[]) result;

        return entityToDto((Guestbook) arr[0], (Member) arr[1]);
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
}

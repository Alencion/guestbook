package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

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
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());
        Page<Guestbook> result = this.guestbookRepository.findAll(pageable);

        return new PageResultDTO<>(result, this::entityToDto);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        return this.guestbookRepository.findById(gno)
                .map(this::entityToDto)
                .orElse(null);
    }
}

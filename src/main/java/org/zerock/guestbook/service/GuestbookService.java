package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
        return Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        return GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .registerDate(entity.getRegisterDate())
                .modifyDate(entity.getModifyDate())
                .build();
    }
}

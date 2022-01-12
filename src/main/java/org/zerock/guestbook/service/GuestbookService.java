package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.repository.MemberRepository;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO);

    GuestbookDTO read(Long gno);

    void modify(GuestbookDTO dto);

    void remove(Long gno);

    default Guestbook dtoToEntity(GuestbookDTO dto, Member writer) {
        return Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(writer)
                .build();
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        return GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writerEmail(entity.getWriter().getEmail())
                .writerName(entity.getWriter().getName())
                .registerDate(entity.getRegisterDate())
                .modifyDate(entity.getModifyDate())
                .build();
    }
}

package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.Member;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    GuestbookDTO read(Long gno);

    void modify(GuestbookDTO dto);

    void remove(Long gno);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Member writer = Member.builder().email(dto.getWriterEmail()).build();

        return Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(writer)
                .build();
    }

    default GuestbookDTO entityToDto(Guestbook guestbook, Member member) {
        return GuestbookDTO.builder()
                .gno(guestbook.getGno())
                .title(guestbook.getTitle())
                .content(guestbook.getContent())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .registerDate(guestbook.getRegisterDate())
                .modifyDate(guestbook.getModifyDate())
                .build();
    }
}

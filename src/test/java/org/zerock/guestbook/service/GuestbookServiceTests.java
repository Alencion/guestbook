package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    void testRegister() {
        // given
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user0")
                .build();

        // when & then
        System.out.println(this.service.register(guestbookDTO));
    }

    @Test
    void testGetList() {
        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        // when
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = this.service.getList(pageRequestDTO);

        // then
        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());
        System.out.println("-----------------------------------------");
        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("=========================================");
        resultDTO.getPageList().forEach(System.out::println);
    }

    @Test
    void testSearch() {
        //given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("w")
                .keyword("판다")
                .build();

        //when
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = this.service.getList(pageRequestDTO);

        //then
        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());
        System.out.println("-----------------------------------------");
        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("=========================================");
        resultDTO.getPageList().forEach(System.out::println);
    }
}

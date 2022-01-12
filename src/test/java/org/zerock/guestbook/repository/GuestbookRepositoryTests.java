package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    void insertDummies() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            Member member = Member.builder().email("user" + ((i - 1) % 100 + 1) + "@aaa.com").build();

            Guestbook guestbook = Guestbook.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();

            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    void updateTest() {
        // given
        Optional<Guestbook> result = this.guestbookRepository.findById(300L);

        // when & then
        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            this.guestbookRepository.save(guestbook);
        }
    }

    @Test
    void testQueryDSL1() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = qGuestbook.title.contains(keyword);
        builder.and(expression);

        //when
        Page<Guestbook> result = this.guestbookRepository.findAll(builder, pageable);

        //then
        result.stream().forEach(System.out::println);
    }

    @Test
    void testQueryDSL2() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);
        builder.and(qGuestbook.gno.gt(0L));

        //when
        Page<Guestbook> result = this.guestbookRepository.findAll(builder, pageable);

        //then
        result.stream().forEach(System.out::println);
    }

    @Test
    void testSearch1() {

        this.guestbookRepository.search1();
    }

    @Test
    void testSearchPage() {

        Pageable pageable = PageRequest.of(0, 10,
                Sort.by("gno").descending()
                        .and(Sort.by("title").ascending()));

        this.guestbookRepository.searchPage("t", "1", pageable);
    }
}

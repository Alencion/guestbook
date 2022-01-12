package org.zerock.guestbook.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.entity.QMember;

import java.util.List;

@Slf4j
public class SearchGuestbookRepositoryImpl extends QuerydslRepositorySupport implements SearchGuestbookRepository {

    public SearchGuestbookRepositoryImpl() {
        super(Guestbook.class);
    }

    @Override
    public Guestbook search1() {

        log.info("search1............");

        QGuestbook qGuestbook = QGuestbook.guestbook;
        QMember qMember = QMember.member;

        JPQLQuery<Guestbook> jpqlQuery = from(qGuestbook);
        jpqlQuery.leftJoin(qMember).on(qGuestbook.writer.eq(qMember));

        jpqlQuery.select(qGuestbook, qMember.email);

        log.info("-----------------------");
        log.info(jpqlQuery.toString());
        log.info("-----------------------");

        List<Guestbook> result = jpqlQuery.fetch();
        log.info(result.toString());

        return null;
    }
}

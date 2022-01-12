package org.zerock.guestbook.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.entity.QMember;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.....................");

        QGuestbook qGuestbook = QGuestbook.guestbook;
        QMember qMember = QMember.member;

        JPQLQuery<Guestbook> jpqlQuery = from(qGuestbook);
        jpqlQuery.join(qMember).on(qGuestbook.writer.eq(qMember));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(qGuestbook, qMember);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = qGuestbook.gno.gt(0L);

        booleanBuilder.and(expression);

        if (type != null) {
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(qGuestbook.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(qMember.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(qGuestbook.content.contains(keyword));
                        break;
                }
            }

            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Guestbook.class, "guestbook");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        tuple.groupBy(qGuestbook);

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result.toString());

        long count = tuple.fetchCount();
        log.info("COUNT:" + count);

        return new PageImpl<>(
                result.stream()
                        .map(Tuple::toArray)
                        .collect(Collectors.toList()),
                pageable,
                count);
    }
}

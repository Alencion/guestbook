package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.search.SearchGuestbookRepository;

@Repository
public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook>, SearchGuestbookRepository {

    @Query("SELECT g, w FROM Guestbook g LEFT JOIN g.writer w WHERE g.gno = :gno")
    Object getGuestbookByGno(@Param("gno") Long gno);
}

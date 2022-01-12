package org.zerock.guestbook.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.guestbook.entity.Guestbook;

public interface SearchGuestbookRepository {

    Guestbook search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}

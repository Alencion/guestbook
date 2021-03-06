package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;
    private int totalPage;
    private int page;
    private int size;
    private int start, end;
    private boolean prev, next;
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        this.dtoList = result.stream().map(fn).collect(Collectors.toList());
        this.totalPage = result.getTotalPages();

        this.makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        int lastUnitEnd = (int) (Math.ceil(page / 10.0)) * 10;

        this.start = lastUnitEnd - 9;
        this.end = Math.min(totalPage, lastUnitEnd);

        this.prev = start > 1;
        this.next = totalPage > lastUnitEnd;

        this.pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}

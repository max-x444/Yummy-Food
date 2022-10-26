package com.nix.ua.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Configuration
public class PageService<T> {
    public Page<T> findPaginated(Pageable pageable, Iterable<T> tIterable) {
        final int pageSize = pageable.getPageSize();
        final int currentPage = pageable.getPageNumber();
        final int startItem = currentPage * pageSize;
        final List<T> collect = StreamSupport.stream(tIterable.spliterator(), false).collect(Collectors.toList());
        List<T> list;

        if (collect.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, collect.size());
            list = collect.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, collect.size());
    }

    public ModelAndView getPaginated(Optional<Integer> page, Optional<Integer> size,
                                     Iterable<T> tIterable, String view, ModelAndView modelAndView) {
        final int currentPage = page.orElse(1);
        final int pageSize = size.orElse(5);
        final Page<T> tPage = findPaginated(PageRequest.of(currentPage - 1, pageSize), tIterable);
        final int totalPages = tPage.getTotalPages();

        if (totalPages > 0) {
            final List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
            modelAndView.addObject("currentPage", currentPage);
        }
        modelAndView.addObject("tPage", tPage);
        modelAndView.setViewName(view);
        return modelAndView;
    }
}
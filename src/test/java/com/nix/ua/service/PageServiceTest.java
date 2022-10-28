package com.nix.ua.service;

import com.nix.ua.model.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

class PageServiceTest {
    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_FILTER = "";
    private static final int INDEX_FIRST_PAGE = 0;
    private static final int DEFAULT_SIZE_PAGE = 5;
    private static final int NUMBER_PAGES = 1;
    private static final boolean DEFAULT_DIRECTION = true;
    private PageService<Booking> target;
    private BookingService bookingService;
    private ModelAndView modelAndView;
    private Page<Booking> targetPage;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        targetPage = Mockito.mock(Page.class);
        modelAndView = Mockito.mock(ModelAndView.class);
        bookingService = Mockito.mock(BookingService.class);
        target = new PageService<>(bookingService);
    }

    @Test
    void getPaginatedFounded() {
        Mockito.when(bookingService.findAllBySortedAndFiltered(DEFAULT_FILTER, PageRequest.of(
                INDEX_FIRST_PAGE, DEFAULT_SIZE_PAGE, Sort.by(DEFAULT_SORT).descending()))).thenReturn(targetPage);
        Assertions.assertEquals(getResult(targetPage), target.getPaginated(Optional.empty(), Optional.empty(),
                DEFAULT_SORT, DEFAULT_FILTER, DEFAULT_DIRECTION, modelAndView));
    }

    @Test
    void getPaginatedNotFounded() {
        Mockito.when(bookingService.findAllBySortedAndFiltered(DEFAULT_FILTER, PageRequest.of(
                INDEX_FIRST_PAGE, DEFAULT_SIZE_PAGE, Sort.by("wrong sort").descending()))).thenReturn(targetPage);
        Assertions.assertThrows(NullPointerException.class, () -> target.getPaginated(Optional.empty(),
                Optional.empty(), DEFAULT_SORT, DEFAULT_FILTER, DEFAULT_DIRECTION, modelAndView));
    }

    private ModelAndView getResult(Page<Booking> result) {
        modelAndView.addObject("currentPage", NUMBER_PAGES);
        modelAndView.addObject("pageNumbers", List.of(NUMBER_PAGES));
        modelAndView.addObject("tPage", result);
        return modelAndView;
    }
}
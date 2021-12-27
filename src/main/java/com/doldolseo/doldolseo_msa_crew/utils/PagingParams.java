package com.doldolseo.doldolseo_msa_crew.utils;

import org.springframework.data.domain.Page;

public class PagingParams {
    private int START_BLOCK_PAGE;
    private int END_BLOCK_PAGE;
    private int PAGE_NUMBER;
    private int TOTAL_PAGES;

    public PagingParams(int pageBlock, Page page) {
        this.PAGE_NUMBER = page.getPageable().getPageNumber();  //현재 페이지번호
        this.TOTAL_PAGES = page.getTotalPages();
        this.START_BLOCK_PAGE = ((PAGE_NUMBER) / pageBlock) * pageBlock + 1;
        this.END_BLOCK_PAGE = (PAGE_NUMBER / pageBlock == TOTAL_PAGES / pageBlock) ? TOTAL_PAGES : START_BLOCK_PAGE + pageBlock - 1; //현재페이지가 마지막 블록이면 마지막페이지 = 전체 페이지 수
    }

    public int getStartBlockPage() {
        return START_BLOCK_PAGE;
    }

    public int getEndBlockPage() {
        return END_BLOCK_PAGE;
    }

    public int getPageNumber() {
        return PAGE_NUMBER;
    }

    public int getTotalPages() {
        return TOTAL_PAGES;
    }
}

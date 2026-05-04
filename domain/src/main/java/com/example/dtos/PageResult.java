package com.example.dtos;

import java.util.List;

public class PageResult<T> {

    private List<T> content;
    private int page;
    private int totalPages;
    private long totalElements;

    public PageResult() {}

    public PageResult(List<T> content, int page, int totalPages, long totalElements) {
        this.content = content;
        this.page = page;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
}

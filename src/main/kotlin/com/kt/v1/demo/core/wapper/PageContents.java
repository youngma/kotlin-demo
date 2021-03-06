package com.kt.v1.demo.core.wapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageContents<T> {

    private int offset;// current page
    private int limit; // unit per page
    private int total; // total size
    private List<T> contents;

    public PageContents(List<T> contents, int offset, int limit, int total) {
        this.contents = contents;
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }

    public PageContents(List<T> contents, int total) {
        this.contents = contents;
        this.total = total;
    }
}

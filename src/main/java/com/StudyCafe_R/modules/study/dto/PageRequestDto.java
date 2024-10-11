package com.StudyCafe_R.modules.study.dto;

public class PageRequestDto {

    private int page = 1;
    private int size = 3;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

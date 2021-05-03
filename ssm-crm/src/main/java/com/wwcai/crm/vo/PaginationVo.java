package com.wwcai.crm.vo;

import java.util.List;

public class PaginationVo<T> {
    private int total;
    private List<T> datalist;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
    }


}

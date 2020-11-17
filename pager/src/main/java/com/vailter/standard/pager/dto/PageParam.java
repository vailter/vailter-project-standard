package com.vailter.standard.pager.dto;

//import com.github.pagehelper.IPage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageParam<T> {

    //  description = "页码", defaultValue =  1
    private Integer pageNum;

    // description = "页数", defaultValue = 20
    private Integer pageSize;

    // description = "排序", example = "id desc,name"
    private String orderBy;

    //  description = "参数"
    private T param;

    public PageParam<T> setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }
}

package com.vailter.standard.pager.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vailter.standard.pager.dto.PageParam;

import java.util.List;

/**
 * 统一分页服务
 *
 * @param <P> 泛型参数 Param
 * @param <R> 泛型返回值 R
 */
public interface IBaseService<P, R> {
    int DEFAULT_PAGE_SIZE = 100;

    /**
     * 分页查询
     *
     * @param param 请求参数DTO
     * @return 分页集合
     */
    default PageInfo<R> page(PageParam<P> param) {
        return PageHelper.startPage(param).doSelectPageInfo(() -> list(param.getParam()));
    }

    /**
     * 集合查询：数据量过大，使用分页组装
     *
     * @param param 查询参数
     * @return 查询响应
     */
    default List<R> listByPage(P param) {
        return listByPage(param, null);
    }

    default List<R> listByPage(P param, Integer pageSize) {
        if (null == pageSize) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int page = 1;
        PageParam<P> pageParam = new PageParam<P>()
                .setPageNum(1)
                .setPageSize(pageSize)
                .setParam(param);
        PageInfo<R> pageInfo = page(pageParam);
        int pages = pageInfo.getPages();
        List<R> list = pageInfo.getList();
        while (++page <= pages) {
            pageParam.setPageNum(page);
            PageInfo<R> tempPageInfo = page(pageParam);
            list.addAll(tempPageInfo.getList());
        }
        return list;
    }

    /**
     * 集合查询
     *
     * @param param 查询参数
     * @return 查询响应
     */
    List<R> list(P param);
}

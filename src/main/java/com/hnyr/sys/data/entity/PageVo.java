package com.hnyr.sys.data.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: PageVo
 * @Description: 分页数据对象
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class PageVo<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = -3720998571176536865L;
    private List<T> content = new ArrayList<>();
    private long totalElements;
    private int page;
    private int size;


    public PageVo() {
    }

    public PageVo(List<T> content, long totalElements, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }

    /**
     * Returns if there is a previous page.
     *
     * @return if there is a previous page.
     */
    public boolean hasPrevious() {
        return getPage() > 0;
    }

    /**
     * Returns if there is a next page.
     *
     * @return if there is a next page.
     */
    public boolean hasNext() {
        return getPage() + 1 < getTotalPage();
    }

    /**
     * Returns whether the current page is the first one.
     *
     * @return whether the current page is the first one.
     */
    public boolean isFirst() {
        return !hasPrevious();
    }

    /**
     * Returns whether the current  page is the last one.
     *
     * @return whether the current  page is the last one.
     */
    boolean isLast() {
        return !hasNext();
    }

    /**
     * Returns the total amount of elements of all pages.
     *
     * @return the total amount of elements of all pages.
     */
    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Returns the number of total pages.
     *
     * @return the number of total pages
     */
    public int getTotalPage() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) getSize());
    }

    /**
     * Returns the page content as unmodifiable {@link List}.
     *
     * @return Returns the page content as unmodifiable {@link List}
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * Returns whether the current page has content.
     *
     * @return whether the current page has content.
     */
    public boolean hasContent() {
        return getContentSize() > 0;
    }

    /**
     * Returns the number of elements on current page.
     *
     * @return the number of elements on current page.
     */
    public int getContentSize() {
        return content.size();
    }

    /**
     * Returns the number of items of each page.
     *
     * @return the number of items of each page
     */
    public int getSize() {
        return size;
    }

    public void setPageSize(int pageSize) {
        this.page = pageSize;
    }

    /**
     * Returns the number of current page. (Zero-based numbering.)
     *
     * @return the number of current page.
     */
    public int getPage() {
        return page;
    }

    /**
     * Set the number of current page. (Zero-based numbering.)
     */
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Iterator<T> iterator() {
        return getContent().iterator();
    }
}

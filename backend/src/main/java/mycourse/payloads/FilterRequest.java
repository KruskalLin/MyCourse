package mycourse.payloads;

import javax.validation.constraints.NotNull;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/19
 * @Todo:
 */
public class FilterRequest {
    @NotNull
    private int page;

    @NotNull
    private int limit;

    private String title;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

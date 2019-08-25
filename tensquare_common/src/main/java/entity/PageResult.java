package entity;

import java.util.List;

/**
 * 分页对象
 *
 * new Result(true,StatusCode.OK,"xxxx",new PageResult(100,list))
 *
 * @param <T>
 */
public class PageResult<T> {

    private Long total;//总记录数
    private List<T> rows;//当前页面需要显示的数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}

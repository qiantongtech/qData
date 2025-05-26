package tech.qiantong.qdata.es.model;

/**
 * 前端搜索对象
 *
 * @author qdata
 */
public class Search
{
    /** 搜索值 */
    private String searchValue;

    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    @Override
    public String toString()
    {
        return "Search [searchValue=" + searchValue + ", pageNum=" + pageNum + ", pageSize=" + pageSize + "]";
    }
}

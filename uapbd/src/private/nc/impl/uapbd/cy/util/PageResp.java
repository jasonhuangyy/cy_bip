package nc.impl.uapbd.cy.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageResp<T> implements Serializable {
    private static final long serialVersionUID = 8893436618811514667L;

    public List<T> content = new ArrayList<T>();
    public long totalElements = 0;
    public PageReq pageReq = null;
    public int totalPage = 0;

    public PageResp(List<T> content, PageReq pageReq, long totalElements) {
        this.content = content;
        this.pageReq = pageReq == null ? new PageReq() : pageReq;
        this.totalElements = totalElements;

        if (this.pageReq.size == 0) {
            totalPage = 0;
        } else {
            totalPage = new Double(Math.ceil(totalElements * 1.0 / this.pageReq.size)).intValue();
        }
    }

    public PageResp() {
        this.pageReq = new PageReq();
    }


}

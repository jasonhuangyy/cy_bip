package nc.impl.uapbd.cy.util;

import nc.bs.framework.common.NCLocator;
import nc.impl.uapbd.cy.sql.BeanListProcessor;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

import java.util.List;

@SuppressWarnings("unchecked")
public class PageUtil {

    public static <T> PageResp<T> pageQuery(String sql, Class<T> type) throws BusinessException {
        return pageQuery(sql, new PageReq(), type);
    }


    public static <T> PageResp<T> pageQuery(String sql, PageReq pageReq, Class<T> type) throws BusinessException {
        int start = pageReq.page * pageReq.size + 1;
        int end = (pageReq.page + 1) * pageReq.size;

        String pageSql = "SELECT /*+ FIRST_ROWS */ * FROM (SELECT A.*, ROWNUM RN FROM (" + sql + ") A WHERE ROWNUM <= " + end + ") WHERE RN >= " + start;
        String countSql = "select count(1) from (" + sql + ")";

        IUAPQueryBS service = NCLocator.getInstance().lookup(IUAPQueryBS.class);
        Object obj = service.executeQuery(countSql, new ColumnProcessor());

        long totalElement = 0;
        if (obj != null) {
            totalElement = Long.parseLong(obj.toString());
        }

        List<T> list = (List<T>) service.executeQuery(pageSql, new BeanListProcessor<T>(type));
        return new PageResp<T>(list, pageReq, totalElement);
    }

}

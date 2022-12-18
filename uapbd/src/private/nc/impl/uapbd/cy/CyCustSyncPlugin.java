package nc.impl.uapbd.cy;

import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.impl.uapbd.cy.util.PageReq;
import nc.impl.uapbd.cy.util.PageResp;
import nc.impl.uapbd.cy.util.PageUtil;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.pub.BusinessException;

import java.util.List;

public class CyCustSyncPlugin implements IBackgroundWorkPlugin {

    @Override
    public PreAlertObject executeTask(BgWorkingContext bgWorkingContext) throws BusinessException {
        String sql = "select pk_customer,code,name,taxpayerid,def1,def2 from bd_customer where dr=0 and (def1='~' or def1='N')";
        final int pageSize = 200;

        PageReq pageReq = new PageReq(0, pageSize);
        PageResp<CustomerVO> pageResp = PageUtil.pageQuery(sql, pageReq, CustomerVO.class);
        int currentPage = 0;

        Logger.info("ͬ���ͻ�������ʼ������" + currentPage + "ҳ����" + pageResp.totalElements + "��");
        while (currentPage < pageResp.totalPage) {
            Logger.info("���ڴ����" + currentPage + "ҳ");
            List<CustomerVO> list = pageResp.content;
            if (list != null && !list.isEmpty()) {
                for (CustomerVO vo : list) {
                    CySyncProcessor.getInstance().syncCustomerToERP(vo);
                }
            }

            pageReq = new PageReq(++currentPage, pageSize);
            pageResp = PageUtil.pageQuery(sql, pageReq, CustomerVO.class);
        }

        Logger.info("������ɣ���" + pageResp.totalElements + "�����ݣ���" + pageResp.totalPage + "ҳ");
        return null;
    }
}

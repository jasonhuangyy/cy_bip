package nc.impl.uapbd.cy;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.pub.BusinessException;

public class ExchangeToERP implements IBusinessListener {

    @Override
    public void doAction(IBusinessEvent event) throws BusinessException {
        if (event != null && event.getSourceID().equals("e4f48eaf-5567-4383-a370-a59cb3e8a451")
                && (event.getEventType().equals("1002") || event.getEventType().equals("1004"))) {
            CustomerVO customer = (CustomerVO) ((Object[]) ((BDCommonEvent.BDCommonUserObj) event.getUserObject()).getNewObjects())[0];
            if (customer != null) {
                CySyncProcessor.getInstance().syncCustomerToERP(customer);
            }
        }
    }


}

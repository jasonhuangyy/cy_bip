package nc.impl.uapbd.cy;

import nc.vo.bd.cust.CustomerVO;

public class CustDTO {
    private String customerName;
    private String customerNumber;
    private String sourceSystem;
    private String socialCreditCodes;

    private static final String SYS_CODE = "2f193758d9035a0981ebe96588e9202f";

    public static CustDTO of(CustomerVO cust) {
        CustDTO dto = new CustDTO();
        dto.customerName = cust.getName();
        dto.customerNumber = cust.getCode();
        dto.socialCreditCodes = cust.getTaxpayerid();
        dto.sourceSystem = SYS_CODE;
        return dto;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSocialCreditCodes() {
        return socialCreditCodes;
    }

    public void setSocialCreditCodes(String socialCreditCodes) {
        this.socialCreditCodes = socialCreditCodes;
    }
}

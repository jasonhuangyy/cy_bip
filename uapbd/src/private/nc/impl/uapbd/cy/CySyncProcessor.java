package nc.impl.uapbd.cy;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import nc.bs.dao.BaseDAO;
import nc.impl.uapbd.cy.util.RespMsg;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.pub.BusinessException;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import ssc.mq.util.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CySyncProcessor {
    private String erpHost = null;
    private static final CySyncProcessor instance = new CySyncProcessor();

    public static CySyncProcessor getInstance() {
        return instance;
    }

    private CySyncProcessor() {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("CyConfig.properties");
            Properties sp = new Properties();
            sp.load(in);
            erpHost = sp.getProperty("erpHost");
        } catch (Exception e) {
            nc.bs.logging.Logger.error(e.getMessage());
        }
    }

    public void syncCustomerToERP(CustomerVO customer) throws BusinessException {
        if ("Y".equals(customer.getDef1()) ||
                ("N".equals(customer.getDef1())
                        && customer.getDef2() != null
                        && customer.getDef2().contains("在ERP系统中已存在"))
        ) return;

        RespMsg respMsg = sendRequest(customer);
        customer.setDef1(respMsg.getSuccess() ? "Y" : "N");
        String message = respMsg.getMessage() + ":" + respMsg.getData();
        if (message.length() > 101) {
            message = message.substring(0, message.length() - 1);
        }
        customer.setDef2(message);
        new BaseDAO().updateVO(customer, new String[]{"def1", "def2"});
    }


    private RespMsg sendRequest(CustomerVO customer) throws BusinessException {
        CustDTO dto = CustDTO.of(customer);
        try {
            List<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()));

            HttpClient httpClient = HttpClients.custom()
                    .setDefaultHeaders(headers)   // 设置默认请求头
                    .build();

            HttpPost post = new HttpPost(erpHost);
            post.setEntity(new StringEntity(JSON.toJSONString(dto)));

            HttpResponse response = httpClient.execute(post);
            String respBody = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(respBody, RespMsg.class);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            throw new BusinessException("调用客户管理系统接口异常：" + ex.getMessage());
        }

    }


//    public static void main(String[] args) {
//        CustomerVO customer = new CustomerVO();
//        customer.setCode("0001");
//        customer.setName("佰里讯网络科技有限公司");
//        try {
//            RespMsg respMsg = CySyncProcessor.getInstance().sendRequest(customer);
//            String respBody = new ObjectMapper().writeValueAsString(respMsg);
//            System.out.println(respBody);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundCouponOrderAgreementPayModel;
import com.alipay.api.domain.AlipayFundCouponOrderDisburseModel;
import com.alipay.api.request.AlipayFundCouponOrderAgreementPayRequest;
import com.alipay.api.request.AlipayFundCouponOrderDisburseRequest;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayFundCouponOrderDisburseResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AlipayTest {

    public static  String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
    public static String appId = "2017011905224137";
    // 支付宝的密钥
    public static String private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJtZc6EE8XdRHOm1e9+c3af6LgGOcsykZkcbSum7XBsJ+Re/yNwMfZOcNGthj+x0qI8UtptVzSR5LfPtkFpxOgV0UQXB2khTI1s76UafkqOPaMhCLMiDmz7nqGT0CrcNno5ysue++i5wgp74QAC9E1yiIKjNPDzEfpIlAGA0rz/zFZ+kJi9f0RxU7C0n2t1e1M8cKaus9Fuxayj0IK/puU6WsFwRjsTaQxBkEaW15ObC6GA9/8Jq7aMWj3NTt4IsBl67TK/La6dkKyHVjeP8zoZwU012WUQMBqv8lwumC0juug0rscBXpwyKb2P3kTU3848LDe4BWTliQzTH6HcHq9AgMBAAECggEBAJZn1Y/yQUeYf+KzasW504mtyuGXMCnx2nNjZfsdGHaJITL2ZOe+bEbKD5ZWimTQbf+XDUiRd9lEjXvx/Rl97+CLsm/0gBYBS+NHWECKlMSrHQzCKsCgg4ZruOkVh39O8u38EfLjb1jlNO0wMBlotmOJicCcKfvCA40PjOQu6GDvAZeXwknT4kR6WepPz386+OJe8U1NKU+kngVGRLgzmmDn7oLXc/EMqavpdeRfhMEHtD4wlPgBG7lNOv1+eV9jHLmil4cOV/oNrPRJlO60bu4mWYIwoz/5kq3N16+zgT9qT+wdWd3IQ+sVIdD4RU4pYX4v5xPZYaMa/PbLxerWcQECgYEA79jwja+3bCxrpijeA7e2oabx6tl1J/ny3DH9yLSq5CNnJFaOWqfjhlB388j8dczv/ojXHscFzW+WMMT6eeXslsK+iejufLXV3Eacx5ner2fluHiWftABnyaE+cUZ0Cuy7Dw3DZpH1VziCax8w55jNk4hf2Zj7KsyLv7Tyus5HDECgYEA10si118Ugo+qFwxw2jBni7LzOt432hB4l9K/+3jbsnl6/K4JEj/4EY2PXHqveNY+SA4/k4C99MlgzA6kKamy7LK1DP5QMjiQWqzYgbep0YdZt/opnREVBMWUHHB0yS4WUTUOTxc9ROvfVqknsnfxJVFlEtDOMEKd6tgZwOGzAE0CgYEA2qhZLaKLQtgAT9wkAxmiKx9KnwMetpI9IReGOUrUHOZVqqAqaWVznMiRgrOxzSdHiDJyg8GHfbGEJ/P8MdM2bH8gPBQaD6GqVYYeei3CAQFkQVfWQ8gYImJ4ikhxbwHXvVxsCD+ly1NBUvZoS+KCqkhya3YKFQJB3uw8Y1sslpECgYBXoenIqJb8neWqBBJbfaBoKKsApzsss8+iXNJwVb13ZCM/fseKeidLXceg6P9LvEkVo1cVfy3S2bVg2gTQQJbn6cXwSAP0rTl07y4hftTMyL8oN9eAa1eW3aVv/gZLbUsAOwxb/SWfTDNYXvodlty9R+hQRFXpJvSIhvXUoQnvcQKBgGhvPsywCMElhB2fONHREVGZS3geH89quoiA92JLxVjMBxTzRkuwT6Gb5Isjpdv9VLTDkwewuU8pGmAghPWaArVKmM+wPcMGbm2zDOaUi5SofERHBXfPDsD6I3KqOFSqQj2yFn/EtcYKK5BrfhimYK2AoumWcEq1IlZ0/pLR4tF/";
    // 支付宝的公钥
    public static String ali_public_key  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw1AeGXQvAJYNwlxIuCRkD8nM+Y5NfDoU7lwjka69wHWOEyzALjxSPn69x3AlPMjkQS2UPpvBhKTD4s3ELpGA2mNT7DEqGGFeS6jHl80iGziQnb5dodFm98gfdRebyJMd36pSXDmht3GjBswMQpp2x06Wd1fwlBZ9lnuUBNtSN1pceawt+4F7OXuPVcNg/27MPrAZOMvaTC3UUid7kx4nwQJ6RQYSpg0m9nyVI2zPC1wj6XSmCWuhMJOAQhQxy1DikwHLtnnRr1Jur+mUYKIG+AY7yvZjs7J4DUxXZVyjB9Jfe0XkzvoXa/7TD9ZTEDcJyHyG1/vvByZ/OuqdckmcKQIDAQAB";
    public static String PID = "2088421446748174";


    public static void main(String[] args) {
//        AlipayFundCouponOrderAgreementPayResponse response = fundCoupon();
//        System.out.println(response.getAuthNo());
        //创建一个红包，可以分发给多个用户
        Disbursend("2019102510002001170598185870");
    }


    //创建红包接口
    public static AlipayFundCouponOrderAgreementPayResponse fundCoupon(){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appId,private_key,"json","GBK",ali_public_key,"RSA2");
        AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
        AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
            model.setOutOrderNo(orderNo);
            model.setOutRequestNo(UUID.randomUUID().toString());
            model.setOrderTitle("发送红包");
            model.setAmount("0.01");
            model.setPayerUserId(PID);
            model.setPayTimeout("1h");
            request.setBizModel(model);
        AlipayFundCouponOrderAgreementPayResponse response = null;
        try{
            response = alipayClient.execute(request);
            System.out.println(response.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response;
    }
    //给具体的用户发放具体的金额红包
    public static AlipayFundCouponOrderDisburseResponse Disbursend(String authNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appId,private_key,"json","GBK",ali_public_key,"RSA2");
        AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
        AlipayFundCouponOrderDisburseModel model = new AlipayFundCouponOrderDisburseModel();
            model.setOutOrderNo(orderNo);
            model.setDeductAuthNo(authNo);
            model.setOutRequestNo(UUID.randomUUID().toString());
            model.setOrderTitle("红包打款");
            model.setAmount("0.01");
            model.setPayeeUserId("2088212854989662");
            model.setPayTimeout("1h");
            request.setBizModel(model);
        AlipayFundCouponOrderDisburseResponse response = null;
        try{
            response = alipayClient.execute(request);
            System.out.println(response.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response;
    }


}

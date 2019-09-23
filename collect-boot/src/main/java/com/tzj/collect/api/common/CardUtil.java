package com.tzj.collect.api.common;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AntMerchantExpandTradeorderSyncModel;
import com.alipay.api.domain.ItemOrder;
import com.alipay.api.domain.OrderExtInfo;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.tzj.collect.common.constant.AlipayConst;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CardUtil {
    public static String url = "https://openapi.alipay.com/gateway.do";
    public static String appid="2016111702901743";
    public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJtZc6EE8XdRHOm1e9+c3af6LgGOcsykZkcbSum7XBsJ+Re/yNwMfZOcNGthj+x0qI8UtptVzSR5LfPtkFpxOgV0UQXB2khTI1s76UafkqOPaMhCLMiDmz7nqGT0CrcNno5ysue++i5wgp74QAC9E1yiIKjNPDzEfpIlAGA0rz/zFZ+kJi9f0RxU7C0n2t1e1M8cKaus9Fuxayj0IK/puU6WsFwRjsTaQxBkEaW15ObC6GA9/8Jq7aMWj3NTt4IsBl67TK/La6dkKyHVjeP8zoZwU012WUQMBqv8lwumC0juug0rscBXpwyKb2P3kTU3848LDe4BWTliQzTH6HcHq9AgMBAAECggEBAJZn1Y/yQUeYf+KzasW504mtyuGXMCnx2nNjZfsdGHaJITL2ZOe+bEbKD5ZWimTQbf+XDUiRd9lEjXvx/Rl97+CLsm/0gBYBS+NHWECKlMSrHQzCKsCgg4ZruOkVh39O8u38EfLjb1jlNO0wMBlotmOJicCcKfvCA40PjOQu6GDvAZeXwknT4kR6WepPz386+OJe8U1NKU+kngVGRLgzmmDn7oLXc/EMqavpdeRfhMEHtD4wlPgBG7lNOv1+eV9jHLmil4cOV/oNrPRJlO60bu4mWYIwoz/5kq3N16+zgT9qT+wdWd3IQ+sVIdD4RU4pYX4v5xPZYaMa/PbLxerWcQECgYEA79jwja+3bCxrpijeA7e2oabx6tl1J/ny3DH9yLSq5CNnJFaOWqfjhlB388j8dczv/ojXHscFzW+WMMT6eeXslsK+iejufLXV3Eacx5ner2fluHiWftABnyaE+cUZ0Cuy7Dw3DZpH1VziCax8w55jNk4hf2Zj7KsyLv7Tyus5HDECgYEA10si118Ugo+qFwxw2jBni7LzOt432hB4l9K/+3jbsnl6/K4JEj/4EY2PXHqveNY+SA4/k4C99MlgzA6kKamy7LK1DP5QMjiQWqzYgbep0YdZt/opnREVBMWUHHB0yS4WUTUOTxc9ROvfVqknsnfxJVFlEtDOMEKd6tgZwOGzAE0CgYEA2qhZLaKLQtgAT9wkAxmiKx9KnwMetpI9IReGOUrUHOZVqqAqaWVznMiRgrOxzSdHiDJyg8GHfbGEJ/P8MdM2bH8gPBQaD6GqVYYeei3CAQFkQVfWQ8gYImJ4ikhxbwHXvVxsCD+ly1NBUvZoS+KCqkhya3YKFQJB3uw8Y1sslpECgYBXoenIqJb8neWqBBJbfaBoKKsApzsss8+iXNJwVb13ZCM/fseKeidLXceg6P9LvEkVo1cVfy3S2bVg2gTQQJbn6cXwSAP0rTl07y4hftTMyL8oN9eAa1eW3aVv/gZLbUsAOwxb/SWfTDNYXvodlty9R+hQRFXpJvSIhvXUoQnvcQKBgGhvPsywCMElhB2fONHREVGZS3geH89quoiA92JLxVjMBxTzRkuwT6Gb5Isjpdv9VLTDkwewuU8pGmAghPWaArVKmM+wPcMGbm2zDOaUi5SofERHBXfPDsD6I3KqOFSqQj2yFn/EtcYKK5BrfhimYK2AoumWcEq1IlZ0/pLR4tF/";
    public static String APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh2gO8MAHqGw+j1MZONgMjfZAqYqDFV6EQqZQQOJb9kevDReujpEuMwPSkcd4aV3coWXi4fXqgFwZxeVQKqcNcStJ8X6OL6phZOHW1V0kjxMzlMP2OBcOvY8S4Anwut1UgD2n28MuZi43xORrVzm6ZP0OGth/520fqzgsTF5eJd/x98WwrLiM3ZjmUVvrmQ6PEcnHtWlNZwO9Gb/9Lr4MTUNVugKqITmTm7IWQl/k11KHSsthkUBYmt3WVnnWg0Pry4icrm+gGKVZZTUYS2wtZk/p8Vl2hC+NGSZPl2J0JOoSHPPfVRlm6hneaqzj7kbZeeS26cRx1ZeiquChNbU5jwIDAQAB";

    /*public static String url = "https://openapi.alipay.com/gateway.do";
    public static String appid="2017022705919589";
    public static String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDqHn0Mq7yKTslO5DTr/e/0XavB6MjrG8S6YOZLN9kGoTnEkXojX9a4ZjqGXocHaHlSAetgseTFOJ7NblQKxSTFZtOo56zQ0nxdiKU27BvGUgMymk0J161uezCHZEk0WOB8kVf9O7hVruw4fx5U+4+EwMdQaWGxAT0KTBPLBoXRb+nyB8X4ONkV9D9SfFMYstmWxQCZFQQWjMoExBCODXu3eEvq5Tw1DYLFYL4akbLI8UqbgamntbsZUbpzjV3s5DfMkYmX9Pg3ICTj8B3Nv47lVTsjz5UdZiZRai6qcaSzkRvJIbRzBGfWDJMQVfwSsgoOtIHxZN0ztxvxli167vbHAgMBAAECggEBAMe2znHapnFNmEimP8b4zpMkvPojfBGjlIRxWMLdl1Migcb/WrOGdALFojMxu/pD0wpz/Lh1yHO9Qyp3bVXqqt2CRBiX2UIh7FlV0aoya9cX6wZlvkYhKB9REyrMD+6JyqY4zliFc1AUcgoX1mYkWl+im+QqoV1/cvSvxUqx+8GWFsqxjWDUeof8i7qjm4BaXpmB3p0lVC+Dv1MU0ST9S0RWxgJFmN1Lj/TnhMCz7xuRRy5e4CRkEUxcFj0TGR9hqSGF3AKuweUj1z2GUZYIm6JxAFA19vhu2+AABCikbcbTTTzRjyj07r/Ei+kWjzqBdnuLRC9ueSL99un3zKzbJNECgYEA9Oqe9VxWWXE9uTw+FdYpfDzOhwCk/jkF7i+L3xennNhYu/SRUBjpKW5ZeMq4/UUI0184mjcFJ+5cDnCxlFz7oYVIgrO/rQt6G+p68mTSSViLnoQo+G2VbpDBR/myLdZ14vtWDuCdGscCWIgCyqjESY3DtclS0jgKME0ehOUCYJsCgYEA9LbHX47YFrAehEus3rztgWrGcIPXS07u9zP6o8RqNd4GLJTbzDneHjGn9BYn2vcuUkGUHmrRgAjIsW/KIR8tIk/AiOiJWGCMN10g9F4HFMJek3zBtwrW+LCNCoOvzUQO515Mr7LSvyhuBwA2lM2c5gnk2OR2HxZ+sK6ciSknF0UCgYEAma0GJOrVe7XkhvBX/Jeu3DfDV05+OM5K/FN1LnzHPVE7DGS/MUUQOGD3XyXEwZapnQeomotffti/hogLxQ47DfR1tR1Ybho/eni8wI+mq0MWCFbg5lDMbN4DiUgSF1T0D4KpUxh5O64XuLlPFx6iW4zKeqxifWmQaxJYzgZsYXUCgYEAnb34VIOajT1n3nV9cHKuUl8GnV6O+C9gYJ7z8H7ay/BbYH9wE3w1zpbJodBqTn5JMKKvHNBUTUjcPWxHCgSzrktkW5wxdHN/zuxMItk9jUchecbQg/IL0nDT6bQrLZCyXrftjNAjgy9HhWHAzTig67Py/VB/R9jTP4a+xrdtSFUCgYASP0ffR3EI3wuPxfWM5jUoi/Pr+WeWabXBpY7ipIYDgUMX/N+70kBr2vqdkaYx0lQIdBNc8jtdOxz9S6UiAlGM+ax5b7L9blCIlZ9lBmiSoNkQW4XX2mDzRNraxnQ8F+nEYof0kMhWot5uG2dY5aAz9sVAseA337+2n+2hMn20cQ==";
      public static String APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw1AeGXQvAJYNwlxIuCRkD8nM+Y5NfDoU7lwjka69wHWOEyzALjxSPn69x3AlPMjkQS2UPpvBhKTD4s3ELpGA2mNT7DEqGGFeS6jHl80iGziQnb5dodFm98gfdRebyJMd36pSXDmht3GjBswMQpp2x06Wd1fwlBZ9lnuUBNtSN1pceawt+4F7OXuPVcNg/27MPrAZOMvaTC3UUid7kx4nwQJ6RQYSpg0m9nyVI2zPC1wj6XSmCWuhMJOAQhQxy1DikwHLtnnRr1Jur+mUYKIG+AY7yvZjs7J4DUxXZVyjB9Jfe0XkzvoXa/7TD9ZTEDcJyHyG1/vvByZ/OuqdckmcKQIDAQAB";
      public static String AUTH_PATH = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016111702901743&scope=auth_user,auth_userinfo,auth_ecard&redirect_uri=http://139.219.192.145/ali/entrance.jhtml";
  */


    /*static String url = "https://openapi.alipaydev.com/gateway.do";

    static String appid = "2016080100140308";

    static String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDqHn0Mq7yKTslO5DTr/e/0XavB6MjrG8S6YOZLN9kGoTnEkXojX9a4ZjqGXocHaHlSAetgseTFOJ7NblQKxSTFZtOo56zQ0nxdiKU27BvGUgMymk0J161uezCHZEk0WOB8kVf9O7hVruw4fx5U+4+EwMdQaWGxAT0KTBPLBoXRb+nyB8X4ONkV9D9SfFMYstmWxQCZFQQWjMoExBCODXu3eEvq5Tw1DYLFYL4akbLI8UqbgamntbsZUbpzjV3s5DfMkYmX9Pg3ICTj8B3Nv47lVTsjz5UdZiZRai6qcaSzkRvJIbRzBGfWDJMQVfwSsgoOtIHxZN0ztxvxli167vbHAgMBAAECggEBAMe2znHapnFNmEimP8b4zpMkvPojfBGjlIRxWMLdl1Migcb/WrOGdALFojMxu/pD0wpz/Lh1yHO9Qyp3bVXqqt2CRBiX2UIh7FlV0aoya9cX6wZlvkYhKB9REyrMD+6JyqY4zliFc1AUcgoX1mYkWl+im+QqoV1/cvSvxUqx+8GWFsqxjWDUeof8i7qjm4BaXpmB3p0lVC+Dv1MU0ST9S0RWxgJFmN1Lj/TnhMCz7xuRRy5e4CRkEUxcFj0TGR9hqSGF3AKuweUj1z2GUZYIm6JxAFA19vhu2+AABCikbcbTTTzRjyj07r/Ei+kWjzqBdnuLRC9ueSL99un3zKzbJNECgYEA9Oqe9VxWWXE9uTw+FdYpfDzOhwCk/jkF7i+L3xennNhYu/SRUBjpKW5ZeMq4/UUI0184mjcFJ+5cDnCxlFz7oYVIgrO/rQt6G+p68mTSSViLnoQo+G2VbpDBR/myLdZ14vtWDuCdGscCWIgCyqjESY3DtclS0jgKME0ehOUCYJsCgYEA9LbHX47YFrAehEus3rztgWrGcIPXS07u9zP6o8RqNd4GLJTbzDneHjGn9BYn2vcuUkGUHmrRgAjIsW/KIR8tIk/AiOiJWGCMN10g9F4HFMJek3zBtwrW+LCNCoOvzUQO515Mr7LSvyhuBwA2lM2c5gnk2OR2HxZ+sK6ciSknF0UCgYEAma0GJOrVe7XkhvBX/Jeu3DfDV05+OM5K/FN1LnzHPVE7DGS/MUUQOGD3XyXEwZapnQeomotffti/hogLxQ47DfR1tR1Ybho/eni8wI+mq0MWCFbg5lDMbN4DiUgSF1T0D4KpUxh5O64XuLlPFx6iW4zKeqxifWmQaxJYzgZsYXUCgYEAnb34VIOajT1n3nV9cHKuUl8GnV6O+C9gYJ7z8H7ay/BbYH9wE3w1zpbJodBqTn5JMKKvHNBUTUjcPWxHCgSzrktkW5wxdHN/zuxMItk9jUchecbQg/IL0nDT6bQrLZCyXrftjNAjgy9HhWHAzTig67Py/VB/R9jTP4a+xrdtSFUCgYASP0ffR3EI3wuPxfWM5jUoi/Pr+WeWabXBpY7ipIYDgUMX/N+70kBr2vqdkaYx0lQIdBNc8jtdOxz9S6UiAlGM+ax5b7L9blCIlZ9lBmiSoNkQW4XX2mDzRNraxnQ8F+nEYof0kMhWot5uG2dY5aAz9sVAseA337+2n+2hMn20cQ==";

    static String APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxuihmj/drnCPO+nE91BRX4eMAP2ebLUDP0YMzCBoTu2+2tRMDi9R209xFjdk94xu5qbcv6gkZXclBHjpZtML4T9nbw6NbgwiIH1AY9YlVSHucMLedmbKKZ5GuriyUpKXqXLnxEfCpTcfp/QTXDCym9tAM5D7278kn1YaIsFD/PaLZ4B7YY8cOv5MkCdk0L/17igqcFBHmGoZNrMx528kwLw878MbGh00kUw/RoVkEDaQ3Ijw3XZgqkICQuLXjZ9qZdwHLXLHyd4laIDEtqwgP2CVQIqMWexsO7DoQoCOJXin3jNRYIWbzqDEFrjdqa+5PUCoAXycUUVwpOEbZGsdxQIDAQAB";
    */
    public static String update(AlipayClient alipayClient)
    {
        try
        {
            String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
            AlipayMarketingCardTemplateModifyRequest request = new AlipayMarketingCardTemplateModifyRequest();
            String biz = "{"
                    + "\"request_id\":\""+orderNo+"\","
                    + "\"template_id\":\"20180711000000001069918000300173\","
                    + "\"biz_no_prefix\":\"collect\","
                    + "\"write_off_type\":\"qrcode\","
                    + "\"template_style_info\":{"
                    + "\"card_show_name\":\"垃圾分类回收\","
                    + "\"logo_id\":\"Zt7Dv5U7Qga5DIy3BmUMRgAAACMAAQED\","
                    + "\"background_id\":\"AmPX76YsTWeOhHmj8fgCIQAAACMAAQED\","
                    + "\"bg_color\":\"rgb(0,204,50)\"},"
                    + "\"template_benefit_info\":[{\"title\":\"点滴付出，共筑绿色正义\","
                    + "\"benefit_desc\":[\"您可以领取本卡，参与垃圾分类回收行动，积攒环保能量。\",\"\",\"环保能量如何获取？点击首页上门回收按钮，进入生活垃圾回收页面，选择想要回收的物品类型，下单等待回收人员上门称重回收，订单完成后可以获取对应的环保能量。\",\"\",\"更多会员权益开放中，敬请期待...\"],"
                    + "\"start_date\":\"2016-08-18 15:17:23\","
                    + "\"end_date\":\"2096-09-14 12:12:12\"}],"
                    + "\"column_info_list\":["
                    //+ "\"title\":\"专属权益修改\",\"operate_type\":\"openWeb\",\"value\":\"会员权益\"},"
                    + "{\"code\":\"MEMBER\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    //+ "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2017022805948218&scope=auth_user,auth_base,auth_ecard&state=021-index&redirect_uri=http://alipay.mayishoubei.com/index.html"
                    + "https://qr.alipay.com/s6x04993ll3umdwbo3pmi11"
                    + "\",\"params\":\"{}\"},\"title\":\"扫一扫\",\"operate_type\":\"openWeb\",\"value\":\"\"},"
                    + "{\"code\":\"MEMBER\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    //+ "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2017022805948218&scope=auth_user,auth_base,auth_ecard&state=product&redirect_uri=http://alipay.mayishoubei.com/index.html"
                    + "https://qr.alipay.com/s6x03220ijk70tkkgffxh59"
                    + "\",\"params\":\"{}\"},\"title\":\"环保商城\",\"operate_type\":\"openWeb\",\"value\":\"\"},"
                    + "{\"code\":\"MEMBER\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    //+ "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2017022805948218&scope=auth_user,auth_base,auth_ecard&state=product&redirect_uri=http://alipay.mayishoubei.com/index.html"
                    + "https://qr.alipay.com/s6x01452mvefon1xfq0zt3f"
                    + "\",\"params\":\"{}\"},\"title\":\"垃圾分类回收\",\"operate_type\":\"openWeb\",\"value\":\"\"},"
                    +"{\"code\":\"POINT\",\"title\":\"积分\",\"value\":\"\"},"
                    +"{\"code\":\"OPENDATE\",\"title\":\"re\",\"value\":\"\"}"
                    + "],"
                    + "\"field_rule_list\":[{\"field_name\":\"Balance\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Balance\"},"
                    + "{\"field_name\":\"Point\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Point\"},"
                    + "{\"field_name\":\"OpenDate\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"OpenDate\"}],"
                    +   "\"card_action_list\":["
                    +   "{"
                    +    "        \"code\":\"TO_CLOCK_IN\","
                    +    "\"text\":\"预约回收\","
                    +    "\"url_type\":\"miniAppUrl\","
                    +    "\"url\":\"https://qr.alipay.com/s6x01452mvefon1xfq0zt3f\","
                    +    "\"mini_app_url\":{"
                    +    "\"mini_app_id\":\"2018060660292753\","
                    +    "\"display_on_list\":\"true\""
                    +    "        }"
                    +   "        },"
                    +   "{"
                    +    "        \"code\":\"TO_CLOCK_IN\","
                    +    "\"text\":\"环保商城\","
                    +    "\"url_type\":\"miniAppUrl\","
                    +    "\"url\":\"https://qr.alipay.com/s6x03220ijk70tkkgffxh59\","
                    +    "\"mini_app_url\":{"
                    +    "\"mini_app_id\":\"2018060660292753\","
                    +    "\"mini_page_param\":\"pages/view/storeMall/energy/energy\","
                +        "\"mini_query_param\":\"\","
                    +    "\"display_on_list\":\"true\""
                    +    "        }"
                    +   "        }" +
                    "]"
                    +"}";

            //+ "\"column_info_list\":["
            //+ "\"title\":\"专属权益修改\",\"operate_type\":\"openWeb\",\"value\":\"会员权益\"},"


                   /* + "\"column_info_list\":["

/*
+ "{\"code\":\"sb\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
+ "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016111702901743&scope=auth_user,auth_userinfo,auth_ecard&redirect_uri=http://106.15.157.147/ali/entrance.jhtml&state=sb"
+ "\",\"params\":\"{}\"},\"title\":\"蚂蚁收呗\",\"operate_type\":\"openWeb\",\"value\":\"上门回收\"},"
             */


            /*	    + "{\"code\":\"BENEFIT_INFO\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    + "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016111702901743&scope=auth_user,auth_userinfo,auth_ecard&redirect_uri=http://106.15.157.147/ali/entrance.jhtml&state=p"
                    + "\",\"params\":\"{}\"},\"title\":\"积分商城\",\"operate_type\":\"openWeb\",\"value\":\"\"},"

                    + "{\"code\":\"MEMBER\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    + "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016111702901743&scope=auth_user,auth_userinfo,auth_ecard&redirect_uri=http://106.15.157.147/ali/entrance.jhtml"
                    + "\",\"params\":\"{}\"},\"title\":\"个人中心\",\"operate_type\":\"openWeb\",\"value\":\"\"},"


                    + "{\"code\":\"AUTO\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    + "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2016111702901743&scope=auth_user,auth_userinfo,auth_ecard&redirect_uri=http://106.15.157.147/ali/entrance.jhtml&state=scan"
                    + "\",\"params\":\"{}\"},\"title\":\"自助积分\",\"operate_type\":\"openWeb\",\"value\":\"\"},"

                    + "{\"code\":\"HB\",\"more_info\":{\"title\":\"扩展信息\",\"url\":\""
                    + "https://qr.alipay.com/c1x065053wts8qtxmbhiscb"
                    + "\",\"params\":\"{}\"},\"title\":\"天天领红包\",\"operate_type\":\"openWeb\",\"value\":\"\"},"



                    +"{\"code\":\"POINT\",\"title\":\"积分\",\"value\":\"\"},"
                    +"{\"code\":\"OPENDATE\",\"title\":\"re\",\"value\":\"\"}"
                    + "],"
                    + "\"field_rule_list\":[{\"field_name\":\"Balance\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Balance\"},"
                    + "{\"field_name\":\"Point\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Point\"},"
                    + "{\"field_name\":\"OpenDate\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"OpenDate\"}]}";
           */
            //String bizz = "{\"request_id\":\"00041yanghuanyanghuanyanghuan\",\"template_id\":\"20170413000000000195176000300368\",\"biz_no_prefix\":\"green\",\"write_off_type\":\"barcode\",\"template_style_info\":{\"card_show_name\":\"绿色账户\",\"logo_id\":\"4UH7Y3ipSX67kVF2yCOkugAAACMAAQED\",\"color\":\"rgb(255,255,0)\",\"background_id\":\"_QL9V6WERFGsXsUIjR2xQwAAACMAAQED\",\"bg_color\":\"rgb(0,204,51)\"},\"template_benefit_info\":[{\"title\":\"积分说明\",\"start_date\":\"2016-08-18 15:17:23\",\"end_date\":\"2096-09-14 12:12:12\",\"benefit_desc\":[\"1.绿色账户为上海市推进垃圾分类的激励机制，居民在已推广区域正确投放湿垃圾可获得绿色账户积分，积分可在支付宝或绿色账户网站兑换，有效期2年\",\"2.支付宝虚拟卡中绿色积分与居民绿色账户积分同步更新。\",\"3.绿色账户具体信息可关注绿色账户网站（www.lajifenlei.sh.cn）及所在绿色账户小区公告。\"]}],\"column_info_list\":[{\"code\":\"POINT\",\"title\":\"积分\",\"value\":\"\"},{\"code\":\"OPENDATE\",\"title\":\"re\",\"value\":\"\"}],\"field_rule_list\":[{\"field_name\":\"Balance\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Balance\"},{\"field_name\":\"Point\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Point\"},{\"field_name\":\"OpenDate\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"OpenDate\"}]}";
            System.out.println(biz);
            request.setBizContent(biz);
            AlipayMarketingCardTemplateModifyResponse response = alipayClient.execute(request);
            System.out.println(response.getBody());
        }
        catch (AlipayApiException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }





        return null;
    }

    public static String create(AlipayClient alipayClient)
    {
        try
        {
            AlipayMarketingCardTemplateCreateRequest request = new AlipayMarketingCardTemplateCreateRequest();
            request.putOtherTextParam("app_auth_token", null);


            request.setBizContent("{"
                    + "\"request_id\":\"lajihuishou001\","
                    + "\"card_type\":\"OUT_MEMBER_CARD\","
                    + "\"biz_no_prefix\":\"collect\","
                    + "\"biz_no_suffix_len\":\"20\","
                    + "\"write_off_type\":\"barcode\","
                    + "\"template_style_info\":{"
                    + "\"card_show_name\":\"垃圾回收\","
                    + "\"logo_id\":\"Zt7Dv5U7Qga5DIy3BmUMRgAAACMAAQED\","
                    + "\"background_id\":\"AUHATjQWRG2n5SOOI_dtSgAAACMAAQED\","
                    + "\"bg_color\":\"rgb(0,204,51)\"},"
                    + "\"template_benefit_info\":[{\"title\":\"积分说明\","
                    + "\"benefit_desc\":[\"1. 积分获取方式：用户每次下单都会获取一定的积分。\"],"
                    + "\"start_date\":\"2016-08-18 15:17:23\","
                    + "\"end_date\":\"2096-09-14 12:12:12\"}],"
                    + "\"column_info_list\":["
                    //+ "\"title\":\"专属权益修改\",\"operate_type\":\"openWeb\",\"value\":\"会员权益\"},"
                    +"{\"code\":\"POINT\",\"title\":\"积分\",\"value\":\"\"},"
                    +"{\"code\":\"OPENDATE\",\"title\":\"re\",\"value\":\"\"}"
                    + "],"
                    + "\"field_rule_list\":[{\"field_name\":\"Balance\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Balance\"},"
                    + "{\"field_name\":\"Point\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"Point\"},"
                    + "{\"field_name\":\"OpenDate\",\"rule_name\":\"ASSIGN_FROM_REQUEST\",\"rule_value\":\"OpenDate\"}]}");
            AlipayMarketingCardTemplateCreateResponse response=alipayClient.execute(request);
            System.out.println(response.getBody());

        }
        catch (AlipayApiException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }


    public static String query(AlipayClient alipayClient)
    {
        try
        {
            AlipayMarketingCardTemplateQueryRequest request = new AlipayMarketingCardTemplateQueryRequest();
            request.setBizContent("{" +
                    "\"template_id\":\"20180711000000001069918000300173\"" +
                    "  }");
            AlipayMarketingCardTemplateQueryResponse response = alipayClient.execute(request);
            System.out.println(response.getBody());
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        }
        catch (AlipayApiException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }
    public static void main(String[] args) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConst.serverUrl, "2018060660292753", AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw1AeGXQvAJYNwlxIuCRkD8nM+Y5NfDoU7lwjka69wHWOEyzALjxSPn69x3AlPMjkQS2UPpvBhKTD4s3ELpGA2mNT7DEqGGFeS6jHl80iGziQnb5dodFm98gfdRebyJMd36pSXDmht3GjBswMQpp2x06Wd1fwlBZ9lnuUBNtSN1pceawt+4F7OXuPVcNg/27MPrAZOMvaTC3UUid7kx4nwQJ6RQYSpg0m9nyVI2zPC1wj6XSmCWuhMJOAQhQxy1DikwHLtnnRr1Jur+mUYKIG+AY7yvZjs7J4DUxXZVyjB9Jfe0XkzvoXa/7TD9ZTEDcJyHyG1/vvByZ/OuqdckmcKQIDAQAB", AlipayConst.sign_type);
        update(alipayClient);
//        AlipayMarketingCardQueryRequest request = new AlipayMarketingCardQueryRequest();
//        request.setBizContent("{" +
//                "\"target_card_no_type\":\"BIZ_CARD\"," +
//                "\"target_card_no\":\"collect00000000000000771750\"," +
//                "\"card_user_info\":{" +
//                "\"user_uni_id\":\"2088212869139500\"," +
//                "\"user_uni_id_type\":\"UID\"" +
//                "    }," +
//                "\"ext_info\":\"\"" +
//                "  }");
//        System.out.println(request.getBizContent());
//        AlipayMarketingCardQueryResponse response = alipayClient.execute(request);
//        if(response.isSuccess()){
//            System.out.println("调用成功" + response.getSchemaUrl());
//        } else {
//            System.out.println("调用失败");
//        }
       // mysl();
    }

    public static void mysl(){

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
        AntMerchantExpandTradeorderSyncRequest request = new AntMerchantExpandTradeorderSyncRequest();
        AntMerchantExpandTradeorderSyncModel model = new AntMerchantExpandTradeorderSyncModel();
        model.setBuyerId("2088212854989662");
        model.setSellerId(AlipayConst.SellerId);
        model.setOutBizType("RECYCLING");
        model.setOutBizNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
        List<ItemOrder> orderItemList = new ArrayList<ItemOrder>();
        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setItemName("废金属");
        itemOrder.setQuantity((long)10);
        List<OrderExtInfo> extInfo = new ArrayList<>();
        OrderExtInfo orderExtInfo = new OrderExtInfo();
        orderExtInfo.setExtKey("ITEM_TYPE");
        orderExtInfo.setExtValue("fabric");
        extInfo.add(orderExtInfo);
        itemOrder.setExtInfo(extInfo);
        orderItemList.add(itemOrder);
        model.setItemOrderList(orderItemList);
        request.setBizModel(model);
        AntMerchantExpandTradeorderSyncResponse response = null;
        try {
            response = alipayClient.execute(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调⽤成功"+ response.getParams());

        } else {
            System.out.println("调⽤失败");
        }
        System.out.println(response.getBody()); ;

    }

}

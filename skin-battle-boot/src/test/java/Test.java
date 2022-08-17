import com.skin.Application;
import com.skin.config.EmailUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 13:43
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class Test {

    @Autowired
    EmailUtils emailUtils;

    @org.junit.Test
    public void sendStringEmail() {
        // 测试文本邮件发送（无附件）
        String to = "1747090372@qq.com"; // 这是个假邮箱，写成你自己的邮箱地址就可以
        String title = "文本邮件发送测试";
        String content = "<html><h1>验证码：<b>123456</b>,五分钟内有效</h1>  </html>";
        emailUtils.sendMessage(to, title, content);



    }

}

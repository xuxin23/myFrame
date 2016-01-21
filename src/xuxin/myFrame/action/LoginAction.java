/**
 * 
 */
package xuxin.myFrame.action;

import java.util.Map;

import xuxin.halo.support.RequestReady;
import xuxin.halo.support.SessionReady;
import xuxin.myFrame.domain.User;

/**
 * 测试框架
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-23
 */
public class LoginAction implements RequestReady,SessionReady{

    private Map<String, Object> request;
    private Map<String, Object> session;
    private User user;
    
    /**
     * 登录
     */
    public String login() {
        String result = null;
        // 假设数据库中有xuxin-123456这个账户
        if ("xuxin".equals(user.getId()) && "123456".equals(user.getPassword())) {
            // 假设从持久层取出这个账户的信息是userInfo
            User userInfo = new User();
            userInfo.setId("xuxin");
            userInfo.setName("科比");
            userInfo.setPassword("123456");
            userInfo.setAddress("广州");
            
            session.put("user", userInfo);
            result = "success";
        } else {
            request.put("msg", "用户名或密码错误！");
            result = "failed";
        }
        return result;
    }
    
    /**
     * 注销
     */
    public String exit() {
        session.clear();
        return "success";
    }
    
    public Map<String, Object> getRequest() {
        return request;
    }
    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }
    public Map<String, Object> getSession() {
        return session;
    }
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    
    
}

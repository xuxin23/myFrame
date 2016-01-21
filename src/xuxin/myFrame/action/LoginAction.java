/**
 * 
 */
package xuxin.myFrame.action;

import java.util.Map;

import xuxin.halo.support.RequestReady;
import xuxin.halo.support.SessionReady;
import xuxin.myFrame.domain.User;

/**
 * ���Կ��
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-23
 */
public class LoginAction implements RequestReady,SessionReady{

    private Map<String, Object> request;
    private Map<String, Object> session;
    private User user;
    
    /**
     * ��¼
     */
    public String login() {
        String result = null;
        // �������ݿ�����xuxin-123456����˻�
        if ("xuxin".equals(user.getId()) && "123456".equals(user.getPassword())) {
            // ����ӳ־ò�ȡ������˻�����Ϣ��userInfo
            User userInfo = new User();
            userInfo.setId("xuxin");
            userInfo.setName("�Ʊ�");
            userInfo.setPassword("123456");
            userInfo.setAddress("����");
            
            session.put("user", userInfo);
            result = "success";
        } else {
            request.put("msg", "�û������������");
            result = "failed";
        }
        return result;
    }
    
    /**
     * ע��
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

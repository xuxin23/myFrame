/**
 * 
 */
package xuxin.halo.support;

import java.util.Map;


/**
 * Action实现该接口，框架会自动注入Request对象
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-22
 */
public interface SessionReady {

    public void setSession(Map<String, Object> session);
    
    public Map<String, Object> getSession();
}

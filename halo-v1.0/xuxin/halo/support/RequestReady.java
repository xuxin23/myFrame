/**
 * 
 */
package xuxin.halo.support;

import java.util.Map;

/**
 * Actionʵ�ָýӿڣ���ܻ��Զ�ע��Request����
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-22
 */
public interface RequestReady {

    public void setRequest(Map<String, Object> reqeust);
    
    public Map<String, Object> getRequest();
}

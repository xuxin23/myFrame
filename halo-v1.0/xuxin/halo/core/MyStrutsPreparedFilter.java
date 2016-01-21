/**
 * 
 */
package xuxin.halo.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ���Filter,���������������Զ����ܲ���ʼ��
 * @version 1.1
 * @author xuxin 
 * @data 2013-1-22	
 */
public class MyStrutsPreparedFilter implements Filter{

    private static Log logger = LogFactory.getLog(MyStrutsPreparedFilter.class);
    
    /* 
     * ��������
     */
    public void doFilter(ServletRequest request0, ServletResponse response0, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) request0;
        HttpServletResponse response = (HttpServletResponse) response0;
        
        // �����󽻸�������������
        ActionController.getInstance().invoke(request, response);
    }

    /* 
     * ���������ļ�
     */
    public void init(FilterConfig config) throws ServletException {
        logger.debug("MyStrutsPreparedFilter.init() is executing..");
        
        String configLocation = config.getInitParameter("MyStrutsConfigLocation");
        configLocation = config.getServletContext().getRealPath("WEB-INF/classes/" + configLocation);
        logger.info("load " + configLocation);
        
        // ���ز����������ļ�
        ActionMapping.parse(configLocation);
    }

    public void destroy() {
        
    }
}

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
 * 框架Filter,将所有请求引入自定义框架并初始化
 * @version 1.1
 * @author xuxin 
 * @data 2013-1-22	
 */
public class MyStrutsPreparedFilter implements Filter{

    private static Log logger = LogFactory.getLog(MyStrutsPreparedFilter.class);
    
    /* 
     * 处理请求
     */
    public void doFilter(ServletRequest request0, ServletResponse response0, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) request0;
        HttpServletResponse response = (HttpServletResponse) response0;
        
        // 将请求交给控制器来分配
        ActionController.getInstance().invoke(request, response);
    }

    /* 
     * 加载配置文件
     */
    public void init(FilterConfig config) throws ServletException {
        logger.debug("MyStrutsPreparedFilter.init() is executing..");
        
        String configLocation = config.getInitParameter("MyStrutsConfigLocation");
        configLocation = config.getServletContext().getRealPath("WEB-INF/classes/" + configLocation);
        logger.info("load " + configLocation);
        
        // 加载并解析配置文件
        ActionMapping.parse(configLocation);
    }

    public void destroy() {
        
    }
}

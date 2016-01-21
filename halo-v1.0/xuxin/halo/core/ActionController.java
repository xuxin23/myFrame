package xuxin.halo.core;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import xuxin.halo.support.ActionMaker;

/**
 * action处理器
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-22
 */
public class ActionController{

    private static Log logger = LogFactory.getLog(ActionController.class);
    
    private static ActionController actionController;
    
    // action映射关系
    private Map<String, Object> actionMap;
    
    private ActionController() {
    	
    }
    
    public static ActionController getInstance() {
    	if (actionController == null) {
    		actionController = new ActionController();
    	}
    	return actionController;
    }
    
    /* 
     * 处理具体的action请求
     */
    public void invoke(HttpServletRequest request, HttpServletResponse response) {
        // 请求路径
        String requestURL = request.getServletPath().substring(1);
        String jsp = null;
        try {
            if (!requestURL.endsWith(".jsp")) {
                jsp = executeAction(requestURL, request, response);
                logger.info("forward jsp: " + jsp);
            } else {
                jsp = requestURL;
            }
            request.getRequestDispatcher(jsp).forward(request, response);
        }
        catch (ServletException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String executeAction(String requestURL, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 返回的结果Jsp
        String result = null;
        
        if (actionMap.containsKey(requestURL)) {
            Element elem = (Element) actionMap.get(requestURL);
            // 反射调用的类名称
            String className = elem.attributeValue("class");
            // 调用的方法名称
            String methodName = elem.attributeValue("method");
            logger.info("execute: " + className + "." + methodName);
            try {
                // 调用的类
                Class actionClass = Class.forName(className);
                Method[] methods = actionClass.getMethods();
                // 调用的方法
                Method method = null;
                for (Method m:methods) {
                    if (methodName.equals(m.getName())) {
                        method = m;
                    }
                }
                if (method != null) {
                    Object action;
                    try {
                        action = actionClass.newInstance();
                        
                        // Action增强处理(框架自动组装请求为对象和注入request/session)
                        ActionMaker.make(action, request);
                        
                        // 方法执行的结果
                        String retVal = (String) method.invoke(action, null);
                        
                        // Action增强处理(将Map型的request、session存入实际的Request、Session)
                        ActionMaker.reBack(action, request);
                        
                        // 结果对应的action&jsp
                        String nextActionName = null;
                        // 获取<action>结果集合
                        Iterator<Element> iterator = elem.elementIterator("result");
                        while (iterator.hasNext()) {
                            Element resultElement = iterator.next();
                            String resultName = resultElement.attributeValue("name");
                            if (resultName.equals(retVal)) {
                                nextActionName = resultElement.getText();
                                logger.debug("next actionName:" + nextActionName);
                            }
                        }
                        
                        // 如果下一个执行的为action,则继续循环
                        if (!nextActionName.endsWith(".jsp")) {
                            nextActionName = executeAction(nextActionName, request, response);
                        } 
                        result = nextActionName;
                        
                    }
                    catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    
                } else {
                    logger.warn("can't find the method named " + methodName);
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("the requestURL[" + requestURL +  "] can't mapped to an action...");
            throw new Exception("the requestURL[" + requestURL +  "] can't mapped to an action...");
        }
        return result;
    }

	public Map<String, Object> getActionMap() {
		return actionMap;
	}

	public void setActionMap(Map<String, Object> actionMap) {
		this.actionMap = actionMap;
	}

    
    
}

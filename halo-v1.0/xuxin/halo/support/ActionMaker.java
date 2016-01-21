/**
 * 
 */
package xuxin.halo.support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Action对象增强处理
 * 1.自动封装请求对象
 * 2.注入request/session
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-22
 */
public class ActionMaker {

    private static Log logger = LogFactory.getLog(ActionMaker.class);
    
    /**
     * Action增强
     */
    public static void make(Object obj, HttpServletRequest request) {
        // 获取所有的请求参数的名字集合
        Enumeration attributes = request.getParameterNames();
        // 获取Action中需要被组装的属性对象(必须实现set/get方法)
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        // 筛选字段，如果有set/get才可能被组装
        for (Field field:fields) {
            String fieldName = field.getName();
            String setName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String getName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String[] methodNames = {setName, getName};
            // 符合组装要求的方法个数(有set/get方法就符合)
            int count = 0;
            // 逐个比较方法
            for (String methodName:methodNames) {
                for (Method m:methods) {
                    if (methodName.equals(m.getName())) {
                        count++;
                    }
                }
            }
            
            // 满足组装条件(有set/get,且不是java常用类型，因为需要组装的是自定义的Domain)，新建属性对象并从Request尝试组装对象
            if (count == 2 && !Pattern.matches("java.*", field.getType().getName())) {
                logger.debug("try to make the object-field named [" + field.getName() + "]");
                // 新建属性对象
                Object newObj = null;
                try {
                    newObj = field.getType().newInstance();
                }
                catch (InstantiationException e1) {
                    e1.printStackTrace();
                }
                catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                // 找到与字段名相匹配的传入参数
                while (attributes.hasMoreElements()) {
                    String attr = (String) attributes.nextElement();
                    //参数满足a.b的形式
                    if (attr.indexOf(".") > 0 && attr.indexOf(".") == attr.lastIndexOf(".")) {
                        String objName = attr.split("\\.")[0];
                        String fName = attr.split("\\.")[1];
                        String methodName = "set" + fName.substring(0, 1).toUpperCase() + fName.substring(1);
                        
                        // 参数前缀等于属性字段名
                        if (objName.equals(fieldName)) {
                            // 查找是否有与参数后缀相匹配的set方法
                            for (Method m:field.getType().getMethods()) {
                                // 找到匹配的方法
                                if (methodName.equals(m.getName())) {
                                    try {
                                        // 将参数装配到对象方法
                                        m.invoke(newObj, request.getParameter(attr));
                                        logger.debug("[making..]" + field.getName() + "." + fName + "=" + request.getParameter(attr));
                                    }
                                    catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                    catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    
                }
                
                try {
                    // 将对象装载到Action
                    clazz.getMethod(setName, field.getType()).invoke(obj, newObj);
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                
            }
            
        }
        
        
        // 获取Action实现的接口
        Class[] interfaces = obj.getClass().getInterfaces();
        // 循环查找接口
        for (Class c:interfaces) {
            
            // 注入Request对象的attribute
            if (c.getName().equals("xuxin.myStruts.support.RequestReady")) {
                Map<String, Object> map = new HashMap<String, Object>();
                Enumeration requestAttrs = request.getAttributeNames();
                while (requestAttrs.hasMoreElements()) {
                    String attr = (String) requestAttrs.nextElement();
                    map.put(attr, request.getAttribute(attr));
                }
                logger.debug("Request mapped to Map<String, Object>:" + map);
                
                try {
                    c.getMethod("setRequest", java.util.Map.class).invoke(obj, map);
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            
            // 注入Session对象的attribute
            if (c.getName().equals("xuxin.myStruts.support.SessionReady")) {
                Map<String, Object> map = new HashMap<String, Object>();
                HttpSession session = request.getSession();
                Enumeration sessionAttributes = session.getAttributeNames();
                while (sessionAttributes.hasMoreElements()) {
                    String attr = (String) sessionAttributes.nextElement();
                    map.put(attr, session.getAttribute(attr));
                }
                logger.debug("Session mapped to Map<String, Object>:" + map);
                
                try {
                    c.getMethod("setSession", java.util.Map.class).invoke(obj, map);
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    
    /**
     * 将增强处理封装的reqeust、session重新写入到实际的request、session中
     */
    public static void reBack(Object obj, HttpServletRequest request) {
        Class[] interfaces = obj.getClass().getInterfaces();
        for (Class c:interfaces) {
            if (c.getName().equals("xuxin.myStruts.support.RequestReady")) {
                try {
                    // 清空原有request的attribute
                    Enumeration requestEnum = request.getAttributeNames();
                    while (requestEnum.hasMoreElements()) {
                        request.removeAttribute((String)requestEnum.nextElement());
                    }
                    // 重新写入request的attribute
                    Map<String, Object> req = (Map<String, Object>) obj.getClass().getMethod("getRequest", null).invoke(obj, null);
                    Set<String> reqSet = req.keySet();
                    for (String attr:reqSet) {
                        request.setAttribute(attr, req.get(attr));
                    }
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if (c.getName().equals("xuxin.myStruts.support.SessionReady")) {
                try {
                    // 清空原有session的attribute
                    Enumeration sessionEnum = request.getSession().getAttributeNames();
                    while (sessionEnum.hasMoreElements()) {
                        request.getSession().removeAttribute((String)sessionEnum.nextElement());
                    }
                    // 重新写入session的attribute
                    Map<String, Object> sess = (Map<String, Object>) obj.getClass().getMethod("getSession", null).invoke(obj, null);
                    Set<String> resSet = sess.keySet();
                    for (String attr:resSet) {
                        request.getSession().setAttribute(attr, sess.get(attr));
                    }
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

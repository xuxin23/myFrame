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
 * Action������ǿ����
 * 1.�Զ���װ�������
 * 2.ע��request/session
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-22
 */
public class ActionMaker {

    private static Log logger = LogFactory.getLog(ActionMaker.class);
    
    /**
     * Action��ǿ
     */
    public static void make(Object obj, HttpServletRequest request) {
        // ��ȡ���е�������������ּ���
        Enumeration attributes = request.getParameterNames();
        // ��ȡAction����Ҫ����װ�����Զ���(����ʵ��set/get����)
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        // ɸѡ�ֶΣ������set/get�ſ��ܱ���װ
        for (Field field:fields) {
            String fieldName = field.getName();
            String setName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String getName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String[] methodNames = {setName, getName};
            // ������װҪ��ķ�������(��set/get�����ͷ���)
            int count = 0;
            // ����ȽϷ���
            for (String methodName:methodNames) {
                for (Method m:methods) {
                    if (methodName.equals(m.getName())) {
                        count++;
                    }
                }
            }
            
            // ������װ����(��set/get,�Ҳ���java�������ͣ���Ϊ��Ҫ��װ�����Զ����Domain)���½����Զ��󲢴�Request������װ����
            if (count == 2 && !Pattern.matches("java.*", field.getType().getName())) {
                logger.debug("try to make the object-field named [" + field.getName() + "]");
                // �½����Զ���
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
                // �ҵ����ֶ�����ƥ��Ĵ������
                while (attributes.hasMoreElements()) {
                    String attr = (String) attributes.nextElement();
                    //��������a.b����ʽ
                    if (attr.indexOf(".") > 0 && attr.indexOf(".") == attr.lastIndexOf(".")) {
                        String objName = attr.split("\\.")[0];
                        String fName = attr.split("\\.")[1];
                        String methodName = "set" + fName.substring(0, 1).toUpperCase() + fName.substring(1);
                        
                        // ����ǰ׺���������ֶ���
                        if (objName.equals(fieldName)) {
                            // �����Ƿ����������׺��ƥ���set����
                            for (Method m:field.getType().getMethods()) {
                                // �ҵ�ƥ��ķ���
                                if (methodName.equals(m.getName())) {
                                    try {
                                        // ������װ�䵽���󷽷�
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
                    // ������װ�ص�Action
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
        
        
        // ��ȡActionʵ�ֵĽӿ�
        Class[] interfaces = obj.getClass().getInterfaces();
        // ѭ�����ҽӿ�
        for (Class c:interfaces) {
            
            // ע��Request�����attribute
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
            
            // ע��Session�����attribute
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
     * ����ǿ�����װ��reqeust��session����д�뵽ʵ�ʵ�request��session��
     */
    public static void reBack(Object obj, HttpServletRequest request) {
        Class[] interfaces = obj.getClass().getInterfaces();
        for (Class c:interfaces) {
            if (c.getName().equals("xuxin.myStruts.support.RequestReady")) {
                try {
                    // ���ԭ��request��attribute
                    Enumeration requestEnum = request.getAttributeNames();
                    while (requestEnum.hasMoreElements()) {
                        request.removeAttribute((String)requestEnum.nextElement());
                    }
                    // ����д��request��attribute
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
                    // ���ԭ��session��attribute
                    Enumeration sessionEnum = request.getSession().getAttributeNames();
                    while (sessionEnum.hasMoreElements()) {
                        request.getSession().removeAttribute((String)sessionEnum.nextElement());
                    }
                    // ����д��session��attribute
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

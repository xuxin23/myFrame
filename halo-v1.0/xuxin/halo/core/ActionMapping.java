package xuxin.halo.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析myStruts.xml
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-22
 */
public class ActionMapping {

    private static Log logger = LogFactory.getLog(ActionMapping.class);
    
    /**
     * 解析配置文件
     */
    public static void parse(String configLocation) {
        SAXReader saxReader = new SAXReader();
        try {
            Map<String, Object> actionMap = new HashMap<String, Object>();
            
            Document document = saxReader.read(
                    new File(configLocation));
			List<?> list = document.selectNodes("/struts/action");
            for (Object obj:list) {
                Element elem = (Element) obj;
                actionMap.put(elem.attribute("name").getValue(), elem);
                logger.debug("action mapped : " + 
                        elem.attribute("name").getValue() + "\t-" + 
                        elem.attribute("class").getValue() + "." + 
                        elem.attribute("method").getValue());
            }
            ActionController.getInstance().setActionMap(actionMap);
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
}

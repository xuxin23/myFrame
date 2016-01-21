/**
 * 
 */
package xuxin.myFrame.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xuxin.halo.support.RequestReady;
import xuxin.myFrame.domain.News;

/**
 * @version 1.0
 * @author xuxin 
 * @data 2013-1-23
 */
public class NewsAction implements RequestReady{

    private Map<String, Object> request;
    
    public String findAllNews() {
        // ����newsList�Ǵӳ־ò��ȡ�Ľ������
        List<News> newsList = new ArrayList<News>();
        
        News news = new News();
        news.setTitle("�й��ͺ�ը�������ձ���ʾ���Ե�Ǹ��");
        news.setDate("2013-10-1");
        news.setContent("�ٹ������ʾ�˸߲��ң��׷ױ�ʾ����dangzhongying!");
        
        News news2 = new News();
        news2.setTitle("�ձ��Ͷ�����ը��һ���������ɵ�����֧Ԯ");
        news2.setDate("2013-10-2");
        news2.setContent("�����ɵ���ʾ�����ƻ����й��Ĺ�ϵ���ձ�����ж�������!");
        
        newsList.add(news);
        newsList.add(news2);
        
        request.put("newsList",newsList);
        
        return "success";
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }
    
    
}

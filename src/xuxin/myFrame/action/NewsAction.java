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
        // 假设newsList是从持久层获取的结果集合
        List<News> newsList = new ArrayList<News>();
        
        News news = new News();
        news.setTitle("中国就轰炸东京向日本表示由衷的歉意");
        news.setDate("2013-10-1");
        news.setContent("举国人民表示兴高采烈！纷纷表示永恒dangzhongying!");
        
        News news2 = new News();
        news2.setTitle("日本就东京被炸沉一事向美国干爹请求支援");
        news2.setDate("2013-10-2");
        news2.setContent("美国干爹表示不能破坏与中国的关系，日本人民感动很受伤!");
        
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

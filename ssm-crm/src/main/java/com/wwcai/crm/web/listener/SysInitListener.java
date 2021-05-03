package com.wwcai.crm.web.listener;

import com.wwcai.crm.settings.domain.DicValue;
import com.wwcai.crm.settings.service.DicService;
import com.wwcai.crm.settings.service.impl.DicServiceImpl;
import com.wwcai.crm.utils.ServiceFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    private static WebApplicationContext context;

    private DicService ds;

    /**
     * 该方法用来监听上下文域对象的方法，服务器启动，上下文域对象创建
     * 对象创建完毕，马上执行该方法
     * @param event 该参数能够取得坚挺的对象
     *              监听的是什么对象，就可以通过该参数取得什么对象
     */
    public void contextInitialized(ServletContextEvent event){

        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();
        context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        ds = (DicService) context.getBean("dicService");

        /**
         * 向业务层要7个list
         *
         * 打包为一个map
         *
         * */

        // DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();
        // 将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key, map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");

        // 数据字典处理完毕后，处理stage2Possibility.properties文件
        /*
               解析该文件，将该属性文件中的健值对关系处理成为Java中的健值对关系(map)

         */

        Map<String, String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("stage2Possibility");
        Enumeration<String> e = rb.getKeys();

        while(e.hasMoreElements()) {
            // 阶段
            String key = e.nextElement();
            // 可能性
            String value = rb.getString(key);

            pMap.put(key, value);
        }

        // 将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

package com.project.reggie.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.project.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /*
    * 本来可以在resources文件夹下创建static文件夹，springboot项目默认将static文件夹下的文件做映射
    * 但是这里直接放在resources文件夹下，所以需要添加静态资源映射方法
    * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*静态资源映射*/
        log.info("开始映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        log.info("映射完成");
    }

    /*
    * 用于拦截所有请求，以确保用户成功登录
    * 已登录不拦截
    * 未登录转发至登陆页面
    * */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                System.out.println(request.getRequestURI());
                Object employee = request.getSession().getAttribute("employee");
//
                if (employee!=null) {
                    return true;
                }
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
                response.sendRedirect("http://localhost:8080/backend/page/login/login.html");
                return false;
            }
        }).addPathPatterns("/**").excludePathPatterns("/backend/page/login/login.html","/employee/login","/employee/logout");
    }

    /*
    * 配置用于解决java获取到Long数据精度于js能处理的Long数据精度不一致的问题
    * bug具体表现为js获取到的id最后两位永远为0
    * 将返回的数据序列化为String类型，以方便js处理
    * */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fjc = new FastJsonConfig();
        // 配置序列化策略
        fjc.setSerializerFeatures(SerializerFeature.BrowserCompatible);
        fastJsonConverter.setFastJsonConfig(fjc);
        converters.add(fastJsonConverter);
    }
}

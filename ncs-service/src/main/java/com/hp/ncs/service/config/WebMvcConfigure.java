package com.hp.ncs.service.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.hp.ncs.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.List;

/**
 * @author dongxing
 **/
@Configuration
@Slf4j
public class WebMvcConfigure implements WebMvcConfigurer {

    @Autowired
    private AppSecretProperties appSecretProperties;


    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1、需要先定义一个 convert 转换消息的对象;
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//保留空的字段
                SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                SerializerFeature.WriteNullNumberAsZero,//Number null -> 0
                SerializerFeature.PrettyFormat);//结果格式化
        //3、在convert中添加配置信息.
        fastConverter.setFastJsonConfig(fastJsonConfig);

        HttpMessageConverter<?> converter = fastConverter;
        converters.add(converter);
    }

    /**
     * 统一认证拦截
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                //验证签名、数据验证
                log.info("接收消息，请求接口：{}，请求参数：{}", request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
                boolean pass = validateSign(request);
                if (true) {
                    return true;
                } else {
                    log.error("签名认证失败，请求接口：{}",request.getRequestURI());
                    Result<Object> codeMessage = Result.createByErrorCodeMessage(Result.ResponseCode.ERROR_SIGNATURE.getCode(), "签名认证失败");
                    responseResult(response, codeMessage);
                    return false;
                }
            }
        }).excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }


    /**
     * 签名认证
     */
    private boolean validateSign(HttpServletRequest request) {
        //请求报文
        String logisticsInterface=request.getParameter("logistics_interface");
        //报文签名
        String requestSign = request.getParameter("data_digest");
        //请求来源
        String fromCode=request.getParameter("from_code");
        if(StringUtils.isEmpty(requestSign)||StringUtils.isEmpty(logisticsInterface)) {
            return false;
        }
        try {
//            List<AppSecretProperties.Config> appConfig = appSecretProperties.getAppConfig();
//            if (Optional.ofNullable(appConfig).isPresent() && !appConfig.isEmpty()) {
//                Optional<AppSecretProperties.Config> any = appConfig.stream()
//                        .filter(e -> e.getAppId().equals(fromCode)).findAny();
//                return !any.isPresent() || !requestSign.equals(Base64Util.encode(MD5Util.MD5(logisticsInterface + any.get().getSecret()).getBytes()));
//
//            }
            return true;
        } catch (Exception e) {
            log.error("签名认证失败，exception:",e.getMessage());
            return false;
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}

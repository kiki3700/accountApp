package com.example.demo.aspect;



import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	@Around("within(com.example.demo.controller.*)")
	public Object looging(ProceedingJoinPoint pjp) throws Throwable {
	
		String params = getRequestParams();
		logger.debug(params);
		long startAt = System.currentTimeMillis();
		logger.info("---------------------> REQUEST : {}({})={}", pjp.getSignature().getDeclaringTypeName(),
				pjp.getSignature().getName(),params);
		Object result = pjp.proceed();
		long endAt = System.currentTimeMillis();
        logger.info("----------> RESPONSE : {}({}) = {} ({}ms)", pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(), result, endAt-startAt);
		return result;
	}
	
	 // get requset value
    private String getRequestParams() {

        String params = "";

        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();

        if(requestAttribute != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            Map<String, String[]> paramMap = request.getParameterMap();

            if(!paramMap.isEmpty()) {
                params = " [" + paramMapToString(paramMap) + "]";
            }
        }
        return params;
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
    	StringBuilder sb = new StringBuilder();
    	for(String key : paramMap.keySet()) {
    		sb.append(key +" : ");
    		String[] arr = paramMap.get(key);
    		if(arr.length== 0 ) continue;
    		for(int i = 0 ; i < arr.length; i++) {
    			sb.append(arr[i]);
    			if(i != arr.length-1) sb.append(" ,");
    		}
    		sb.append("\n");
    	}
    	return sb.toString();
    }
}

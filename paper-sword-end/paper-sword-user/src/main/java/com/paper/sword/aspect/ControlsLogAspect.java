package com.paper.sword.aspect;

import com.paper.sword.annotation.ControlsLog;
import com.paper.sword.common.async.AsyncLogQueue;
import com.paper.sword.user.entity.Log;
import com.paper.sword.user.entity.Video;
import com.paper.sword.mapper.LogMapper;
import com.paper.sword.mapper.VideoMapper;
import com.paper.sword.common.vo.UserHolder;
import com.paper.sword.common.vo.UserVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Aspect
@Component
public class ControlsLogAspect {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private LogMapper logMapper;

    @Pointcut("@annotation(com.paper.sword.annotation.ControlsLog)")
    private void pointcut() {}

    @After("pointcut()")
    public void handleSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        AsyncLogQueue.submit((Runnable) ()->{
            saveSysLog(joinPoint);
        });
    }
    public void saveSysLog(ProceedingJoinPoint joinPoint){
        UserVO user = UserHolder.getUser();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        String videoId = (String) args[0];
        Video video = videoMapper.selectById(videoId);
        String videoType = video.getVideoType().toString();

        Log log = new Log();
        ControlsLog controlsLog = method.getAnnotation(ControlsLog.class);
        if( controlsLog!=null){
            log.setControls(controlsLog.operateType());
        }
        log.setVideoType(videoType);
        log.setUserId(user.getId());
        log.setUserName(user.getUsername());
        logMapper.insert(log);
    }
}

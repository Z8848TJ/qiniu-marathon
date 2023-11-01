package com.paper.sword.common.aspect;

import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.async.AsyncLogQueue;
import com.paper.sword.common.entity.Log;
import com.paper.sword.common.entity.Video;
import com.paper.sword.common.mapper.LogMapper;
import com.paper.sword.common.mapper.MarkMapper;
import com.paper.sword.common.mapper.VideoMapper;
import com.paper.sword.common.vo.*;
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

    @Autowired
    private MarkMapper markMapper;

    @Pointcut("@annotation(com.paper.sword.common.annotation.ControlsLog)")
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
            int operateType = controlsLog.operateType();
            log.setControls(String.valueOf(operateType));
            String[] split = videoType.split(",");
            for(int i = 0; i<split.length; i++){
                MarkVo markVo = new MarkVo();
                int markType = Integer.parseInt(split[i]);
                markVo.setUserId(user.getId());
                markVo.setType(markType);
                //根据不同的操作设置不同的偏好值
                switch (controlsLog.operateType()){
                    case 0: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case 1: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case 2: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 2);
                        }else{
                            markVo.setMark(2);
                        }
                    };break;
                    case 3: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case 4: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case 5:{
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) - 3);
                        }else{
                            markVo.setMark(-3);
                        }
                    };break;
                    case 6:{
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) - 5);
                        }else{
                            markVo.setMark(-5);
                        }
                    };break;
                }
                if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                   markMapper.updateById(markVo);
                }else{
                   markMapper.insert(markVo);
                }

            }
        }


        log.setVideoType(videoType);
        log.setUserId(user.getId());
        log.setUserName(user.getUsername());
        logMapper.insert(log);
    }
}

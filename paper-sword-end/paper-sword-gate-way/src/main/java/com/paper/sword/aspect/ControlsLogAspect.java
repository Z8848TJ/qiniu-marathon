package com.paper.sword.aspect;

import com.paper.sword.common.annotation.ControlsLog;
import com.paper.sword.common.async.AsyncLogQueue;
import com.paper.sword.common.entity.Log;
import com.paper.sword.common.entity.Video;
import com.paper.sword.common.mapper.LogMapper;
import com.paper.sword.common.mapper.MarkMapper;
import com.paper.sword.common.mapper.VideoMapper;
import com.paper.sword.common.vo.*;
import com.paper.sword.common.enumType.OperateType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author wwh
 * @date 2023/10/25
 */
@Component
@Aspect
@Slf4j
public class ControlsLogAspect {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private MarkMapper markMapper;

    @Pointcut("@annotation(com.paper.sword.common.annotation.ControlsLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("保存 log 日志");
        Object result = point.proceed();
        AsyncLogQueue.submit(()->{
            saveSysLog(point);
        });
        return result;
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
            OperateType operateType = controlsLog.operateType();
            log.setControls(String.valueOf(operateType.getValue()));
            String[] split = videoType.split(",");
            for(int i = 0; i<split.length; i++){
                MarkBo markVo = new MarkBo();
                int markType = Integer.parseInt(split[i]);
                markVo.setUserId(user.getId());
                markVo.setVideoType(markType);
                //根据不同的操作设置不同的偏好值
                switch (operateType){
                    case like: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case collect: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case follow: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 2);
                        }else{
                            markVo.setMark(2);
                        }
                    };break;
                    case retransmission: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case comment: {
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) + 1);
                        }else{
                            markVo.setMark(1);
                        }
                    };break;
                    case NotInterested:{
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) - 3);
                        }else{
                            markVo.setMark(-3);
                        }
                    };break;
                    case report:{
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) - 5);
                        }else{
                            markVo.setMark(-5);
                        }
                    };break;
                    case CancelLike:{
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) - 1);
                        }else{
                            markVo.setMark(-1);
                        }
                    };break;
                    case CancelCollect:{
                        if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                            markVo.setMark(markMapper.selectByUserIdAndType(markVo.userId, markType) - 1);
                        }else{
                            markVo.setMark(-1);
                        }
                    };break;
                }
                if (markMapper.selectByUserIdAndType(markVo.userId, markType)!=null){
                   markMapper.updateByUserAndType(markVo);
                }else{
                   markMapper.insert(markVo);
                }
            }
        }
        log.setVideoType(videoType);
        log.setUserId(user.getId());
        log.setUserName(user.getUsername());
        log.setCreateTime(new Date());
        logMapper.insert(log);
    }
}

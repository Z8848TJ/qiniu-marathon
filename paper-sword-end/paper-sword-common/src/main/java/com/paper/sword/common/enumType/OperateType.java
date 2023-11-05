package com.paper.sword.common.enumType;

/**
 * @author wwh
 * @date 2023/11/4
 */
public enum OperateType {
    like("点赞",0),
    collect("收藏",1),
    follow("关注",2),
    retransmission("转发",3),
    comment("评论",4),
    NotInterested("不感兴趣",5),
    report("举报",6),
    CancelLike("取消点赞",7),
    CancelCollect("取消收藏",8);




    private final String name;
    private final int value;

    OperateType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}

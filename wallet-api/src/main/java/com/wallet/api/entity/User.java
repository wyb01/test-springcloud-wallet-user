package com.wallet.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 16:39
 **/
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class User implements Serializable {

    private long userId;
    private String loginName;
    private String uid;
    private Integer sourceId;  //用户来源 1 web 2 app 3 其它
    private Integer vipLevel;   //VIP等级 0非会员  ,1 vip1 , 2 vip2 ,3 vip3 , 4 vip4 ,5 vip5
    private String nickName; //昵称
    private String pwd; //密码
    private String safetyCode; //安全码
    private String email; //邮箱
    private String telephone; //手机号
    private Integer isDel; //是否删除
    private String isEffect; //是否生效
    private String isBlack; //是否黑名单
    private Date createTime; //创建时间
    private Date modifyTime; //修改时间
    private Integer active; //是否激活
    private Date loginTime ;
    private String db_source;


}
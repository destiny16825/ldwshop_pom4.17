package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  implements Serializable {

    @TableId(type = IdType.AUTO )//主键回填
    private int id;
    private String  username;
    private String  password;
    private String  nickname;
    private String email;
    private String phone;
    private int status=0;//默认未激活状态
}

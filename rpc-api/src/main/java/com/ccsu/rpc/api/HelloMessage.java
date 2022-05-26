package com.ccsu.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
*  测试 api 的实体类
*
*  @author J
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloMessage implements Serializable {
    private Integer id;
    private String message;
}

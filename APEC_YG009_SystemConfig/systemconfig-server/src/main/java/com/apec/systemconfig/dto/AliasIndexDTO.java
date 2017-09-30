package com.apec.systemconfig.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重新索引结构
 * Created by wubi on 2017/9/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliasIndexDTO {
    //索引名称
    private String index;
    //索引别名
    private String alias;
}

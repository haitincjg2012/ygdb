package com.apec.systemconfig.vo;

import com.apec.framework.common.enumtype.EnableFlag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by wubi on 2017/9/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsConfigVO {

    //id
    private Long id;

    //配置项名称
    private String configName;

    //别名
    private String indexAlias;

    //索引
    private String indexName;

    //重新后的索引名称
    private String newIndexName;

    //映射
    private String mappingStr;

    //类型
    private String indexType;

    //启用/禁用
    private EnableFlag enableFlag;

    //状态  未使用，使用中
    private String status;

    //版本
    private Integer version;

    //服务名
    private String serverName;
    //方法名
    private String methodName;


    //创建日期
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private String createBy;

    //修改日期
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;
}

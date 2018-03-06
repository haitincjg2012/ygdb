package com.apec.systemconfig.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author xxx
 */
@Table(name = "ES_CONFIG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class EsConfig extends BaseModel<Long> {
    /**
     * 配置项名称
     */
    private String configName;
    /**
     * 别名
     */
    private String indexAlias;

    /**
     * 索引
     */
    private String indexName;

    /**
     * 映射
     */
    @Lob
    @Column
    private String mappingStr;

    /**
     * 类型
     */
    private String indexType;

    /**
     * 状态  未使用，使用中
     */
    private String status;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 服务名
     */
    private String serverName;
    /**
     * 方法名
     */
    private String methodName;


}

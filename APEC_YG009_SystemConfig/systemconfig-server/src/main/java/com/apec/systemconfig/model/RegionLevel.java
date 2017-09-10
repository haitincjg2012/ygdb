package com.apec.systemconfig.model;

import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by hmy on 2017/7/28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class RegionLevel extends BaseModel<Long>{

    private static final long serialVersionUID = -6704902903928294908L;

    @Column(name = "code")
    private String code;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private String level;

}

package com.apec.systemuser.model;


import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class SysRoleResource extends BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6118757044420992900L;

	@Column(name = "rid")
    private String rid;
	
	@Column(name = "reid")
    private String reid;

}

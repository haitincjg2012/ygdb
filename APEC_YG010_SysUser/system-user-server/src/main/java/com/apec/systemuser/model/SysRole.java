package com.apec.systemuser.model;


import com.apec.framework.common.Constants;
import com.apec.framework.jpa.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class SysRole extends BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1336007226243410346L;

	@Column(name = "name")
    private String name;
	
	@Column(name = "type")
	private String type;
	
    @Column(name = "remake")
    private String remake;


}

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
 *@author xxx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@GenericGenerator(name = Constants.SYSTEM_GENERATOR, strategy = Constants.ASSIGNED)
public class SysResource extends BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7952251956276850819L;

	@Column(name = "name")
    private String name;
	
	@Column(name = "type")
    private String type;
	
	@Column(name = "resource")
    private String resource;
	
	@Column(name = "sorts")
    private int sorts;
	
	@Column(name = "parent")
    private Long parent;
    
    @Column(name = "remake")
    private String remake;
}

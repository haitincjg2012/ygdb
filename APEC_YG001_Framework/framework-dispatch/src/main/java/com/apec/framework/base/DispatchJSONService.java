package com.apec.framework.base;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("prototype")
@Service("dispatchJSONService")
public class DispatchJSONService  extends BaseJSONService
{

}

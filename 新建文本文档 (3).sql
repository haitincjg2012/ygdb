-- 修改等级属性值为中文等级
delete FROM  attribute_value where attribute_id = 197714467807296;
insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,0,'二级','Y','hmy',200155881730137,now());							insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,1,'二三级','Y','hmy',200155881730138,now());
insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,2,'一二级','Y','hmy',197714467819584,now());						insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,3,'一二三级','Y','hmy',197714467831872,now());
insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,4,'三级','Y','hmy',197714467848256,now());							
insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,5,'一级','Y','hmy',197714467864640,now());
insert into  attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,6,'次果','Y','hmy',197714467852352,now());


-- 删除规格和品种
delete from  word_book where code = 1002 and sort != 0 and id > 0;
delete from  word_book where code = 1001 and sort != 0 and id > 0;

-- 新插入规格
insert into  word_book(id,sort,code,keyword,value) values (19,1,'1001','65#',0);								
insert into  word_book(id,sort,code,keyword,value) values (20,2,'1001','70#',0);								
insert into  word_book(id,sort,code,keyword,value) values (21,3,'1001','75#',0);								
insert into  word_book(id,sort,code,keyword,value) values (22,4,'1001','80#',0);								
insert into  word_book(id,sort,code,keyword,value) values (23,5,'1001','85#',0);								
insert into  word_book(id,sort,code,keyword,value) values (24,6,'1001','90#',0);								
insert into  word_book(id,sort,code,keyword,value) values (25,7,'1001','70#以上',0);								
insert into  word_book(id,sort,code,keyword,value) values (26,8,'1001','75#以上',0);								
insert into  word_book(id,sort,code,keyword,value) values (27,9,'1001','80#以上',0);								
insert into  word_book(id,sort,code,keyword,value) values (28,10,'1001','60#-70#混',0);								
insert into  word_book(id,sort,code,keyword,value) values (29,11,'1001','70#-75#混',0);		

-- 新插入品种
insert into  word_book(id,sort,code,keyword,value) values (30,1,'1002','红富士',0);							
insert into  word_book(id,sort,code,keyword,value) values (31,2,'1002','2001',0);							
insert into  word_book(id,sort,code,keyword,value) values (32,3,'1002','嘎啦果',0);							
insert into  word_book(id,sort,code,keyword,value) values (33,4,'1002','金帅',0);							
insert into  word_book(id,sort,code,keyword,value) values (34,5,'1002','红将军',0);							
insert into  word_book(id,sort,code,keyword,value) values (35,6,'1002','乔纳金',0);							
insert into  word_book(id,sort,code,keyword,value) values (36,7,'1002','金富士',0);							
insert into  word_book(id,sort,code,keyword,value) values (37,8,'1002','其他',0);	



-- 修改字典表区域相关数据
update ygdb.word_book set value = '3710' where id = 12;					
update ygdb.word_book set value = '3706' where id = 13;						
						
							




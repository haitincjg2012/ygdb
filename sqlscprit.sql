-- --增加商品属性值
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,create_date,id) value (197714353979456,6,'金富士','Y','hmy',now(),200155881730118);
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,create_date,id) value (197714353979456,7,'其他','Y','hmy',now(),200155881730119);

-- --删除规格属性
delete FROM ygdb.attribute_value  where attribute_id = 197714453815360;
-- --增加规格属性
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,0,'65#','Y','hmy',200155881730120,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,1,'70#','Y','hmy',200155881730121,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,2,'75#','Y','hmy',200155881730122,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,3,'80#','Y','hmy',200155881730123,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,4,'85#','Y','hmy',200155881730124,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,5,'90#','Y','hmy',200155881730125,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,6,'70#以上','Y','hmy',200155881730126,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,7,'75#以上','Y','hmy',200155881730127,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,8,'80#以上','Y','hmy',200155881730128,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,9,'60#-70#混','Y','hmy',200155881730129,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,10,'70#-75#混','Y','hmy',200155881730130,now());													
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714453815360,11,'100#','Y','hmy',200155881730131,now());

-- --
-- --增加颜色属性
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714489888832,3,'条拉片','Y','hmy',200155881730132,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714489888832,4,'条片不分','Y','hmy',200155881730133,now())	;													

-- --增加等级
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,5,'2级','Y','hmy',200155881730137,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (197714467807296,6,'23级','Y','hmy',200155881730138,now());

-- --删除摘果时间属性值
delete from ygdb.attribute_value where attribute_id = '200155442298944';
-- --增加摘果时间属性值
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (200155442298944,0,'前期','Y','hmy',200155881730139,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (200155442298944,1,'中前期','Y','hmy',200155881730140,now());													
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (200155442298944,2,'中期','Y','hmy',200155881730141,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (200155442298944,3,'中后期','Y','hmy',200155881730142,now());														
insert into ygdb.attribute_value (attribute_id,sort,attr_value,enable_flag,create_by,id,create_date) value (200155442298944,4,'后期','Y','hmy',200155881730143,now());	

-- --是否贴字
delete FROM ygdb.attribute_value  where attribute_id = 200155599806528;
delete FROM ygdb.attribute_name  where id = 200155599806528;
-- --是否套袋
delete FROM ygdb.attribute_value  where attribute_id = 197714653675584;
delete FROM ygdb.attribute_name  where id = 197714653675584;


-- --select id from ygdb.attribute_name where attr_name = '商品' and enable_flag = 'Y';
-- --select id from ygdb.attribute_name where attr_name = '等级' and enable_flag = 'Y';
-- --select id from ygdb.attribute_name where attr_name = '规格' and enable_flag = 'Y';
-- --select id from ygdb.attribute_name where attr_name = '颜色' and enable_flag = 'Y';
-- --select id from ygdb.attribute_name where attr_name = '摘果时间' and enable_flag = 'Y';
-- --select id from ygdb.attribute_name where attr_name = '是否贴字' and enable_flag = 'Y';
-- --select id from ygdb.attribute_name where attr_name = '是否套袋' and enable_flag = 'Y';
-- 删除商品中配置的是否套袋和是否贴字属性
delete FROM ygdb.goods_attr  where attr_id = 200155599806528 and id > 0;
delete FROM ygdb.goods_attr  where attr_id = 197714653675584 and id > 0;


-- 修改梨这个大类的是否为主类信息为否
update  ygdb.goods set main_goods = 0 where id = '201975988715584';
-- 删除梨这个大类
delete from ygdb.goods where id = '201975988715584';

delete from ygdb.goods_attr where goods_id = '201975988715584';
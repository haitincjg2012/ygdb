   我们使用 config module 来组织 node 配置，默认配置写在 config/default.js，然后把不同环境的值写在 config/ 目录下相应文件。无论是开发还是生产，
不要直接修改 config/default.js 等配置文件，而是把需要设置的项拷贝到 config/local.js 让 config 模块自动加载覆盖。
config 模块加载覆盖规则

config 模块加载时会按顺序先加载 default.js，然后加载适合当前环境的配置文件，加载顺序是
1、default
2、[env]（默认为 "development"）
3、[hostname]
4、local.js
5、runtime.js
最后为最优先，后面的会覆盖掉前面的配置（注意是 deepMerge，后面的例子中 a.c 一直有保留），
后面没有的会保留前面的

#【开发说明】

> ##开发时请完善代码注释

###使用工具 【JDK1.8+ 、Mysql5.7 、IDEA 、Maven3.6.0 、SVN】

###项目使用框架 【spring boot 、mybatis plus 、spring security 、swagger2】

> ###项目开发配置:
  >> 1. 为了避免提交无用的文件，打开IDEA点击左上角File->Settings->搜索File Types,在窗口最下面的Ignore files and folders输入框中最后添加 .idea;*.iml;
  >> 2. [配置maven,使用阿里云仓库](https://www.cnblogs.com/beanbag/p/9760724.html)
  >> 3. 根据自己数据库服务器的实际参数配置application.properties中的相应参数
  >> 4. 安装以下插件>>>>>[IDEA插件安装教程](https://blog.csdn.net/xingbaozhen1210/article/details/81076597)
   >>> + Lombok
   >>> + Free MyBatis plugin
   
> ###使用代码生成器在
  >> 1. 进入【src\test\java\com\unis\crk\Generator\CodeGenerator.java】类中
  >> 2. 配置相应的包路径及数据库参数
  >> 3. 运行main方法根据提示完成操作
  
> ###解决Intellij IDEA中文输入法不跟随光标问题
  >> 1. 打开IDEA，Ctrl+Shift+A，在出现的搜索框中输入 Switch Boot JDK
  >> 2. JDK 版本，选择【...】选项，配置自己本地安装的那个路径下的JDK
  >> 3. 然后保存重启即可
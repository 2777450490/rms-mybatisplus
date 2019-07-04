package com.unis.crk.Generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.util.*;

/**
 * 代码生成器
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/05/24 13:23
 */
public class CodeGenerator {

    /**数据库配置**/
    private final static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private final static String URL = "jdbc:mysql://192.168.1.235:3306/crk_new?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "123456";

    /**作者**/
    private final static String AUTHOR = "pengxl";

    /**生成代码包路径**/
    private final static String PACKAGE_PATH = "com.unis.crk.common";

    /**MapperXML文件生成路径**/
    private final static String MAPPER_XML_FILE_PATH = "/src/main/resources/mapper/common/";



    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + ":");
    }





    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        /***************全局配置************/
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(AUTHOR);
        gc.setFileOverride(true);
        gc.setEnableCache(false);
        gc.setOpen(false);
        gc.setSwagger2(true);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setBaseResultMap(true);
        gc.setIdType(IdType.UUID);
        mpg.setGlobalConfig(gc);

        /***************数据源配置************/
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName(DRIVER_CLASS_NAME);
        dsc.setUrl(URL);
        dsc.setUsername(USER_NAME);
        dsc.setPassword(PASSWORD);
        // dsc.setSchemaName("public");
        mpg.setDataSource(dsc);
        /***************包配置************/
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_PATH);
        mpg.setPackageInfo(pc);

        /***************自定义配置，自定义MapperXML输出配置************/
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                //自定义模板参数类型为Map,模板中取值为${cfg.参数名}
            }
        };
        String templatePath = "/templates/mapper.xml.vm";
        String mapperXmlFilePath = projectPath + MAPPER_XML_FILE_PATH;
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return mapperXmlFilePath + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        /***************配置自定义Controller生成模板,关闭默认MapperXml输出************/
        TemplateConfig template = new TemplateConfig();
        template.setXml(null);
        template.setController("/templates/my-controller.java");
        mpg.setTemplate(template);
        /***************************数据库表配置***************************************/
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名,多个表','隔开").split(","));
        strategy.setControllerMappingHyphenStyle(false);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        // strategy.setSuperEntityColumns("id");
        mpg.setStrategy(strategy);
        // 执行生成代码
        mpg.execute();
    }
}

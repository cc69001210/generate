package com.zskj.code.generate.test;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: cc
 * @Date: 2019/6/17 09:28
 * @Description:
 */
public class MainClass {
    public static void main(String[] args) throws Exception {
        //用来获取Mybatis-Plus.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("Mybatis-Plus");

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(rb.getString("OutputDir"));
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
//        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor(rb.getString("author"));
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        /*dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });*/
//        dsc.setDbType(DbType.MYSQL);
//        dsc.setDriverName(rb.getString("driver"));
//        dsc.setUrl(rb.getString("url"));
//        dsc.setUsername(rb.getString("userName"));
//        dsc.setPassword(rb.getString("password"));
//        mpg.setDataSource(dsc);

        dsc.setDbType(DbType.ORACLE);
        dsc.setDriverName(rb.getString("driver"));
        dsc.setUrl(rb.getString("url"));
        dsc.setUsername(rb.getString("userName"));
        dsc.setPassword(rb.getString("password"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix("t");
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        String[] tableNames = new String(rb.getString("tableName").getBytes("ISO-8859-1"), "UTF8").split(",");
        strategy.setInclude(tableNames); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表

        // 自定义实体父类
        //  mybatis的model父类
        strategy.setSuperEntityClass("com.baomidou.mybatisplus.extension.activerecord.Model");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });

        // 自定义 mapper 父类
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");

        // 自定义 service 父类
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");

        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");

        // 自定义 controller 父类
         strategy.setSuperControllerClass("com.zky10.wlyj.config.base.BaseApiController");

        // 生成@RestController
        strategy.setRestControllerStyle(true);

        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(rb.getString("parent.package"));
        // pc.setModuleName("tbldept");//模块名称，单独生成模块时使用！！！！！！！！！！！
        String packageName = StringUtils.isEmpty(rb.getString("className")) ? "" : "." + rb.getString("className");
        pc.setService("service" + packageName);
        pc.setServiceImpl("service" + packageName + ".impl");
        pc.setEntity("pojo.entity" + packageName);
        pc.setMapper("mapper" + rb.getString("className"));
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 自定义 xxList.jsp 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        /*focList.add(new FileOutConfig("/template/list.jsp.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return "E:/workspace/ssmp-crud2/src/main/webapp/WEB-INF/" + tableInfo.getEntityName() + ".jsp";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);*/

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                System.out.println("生成mapper.xml成功！！");
                return rb.getString("OutputDirXml") + "/mapper/" + rb.getString("className") + "/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);


        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
    }
}

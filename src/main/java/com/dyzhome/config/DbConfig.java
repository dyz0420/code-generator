package com.dyzhome.config;

import com.dyzhome.dao.GeneratorDao;
import com.dyzhome.dao.MysqlGeneratorDao;
import com.dyzhome.utils.Constant;
import com.dyzhome.utils.RRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据库配置
 *
 * @author Dyz
 */
@Configuration
public class DbConfig {
    @Value("${database.type: mysql}")
    private String database;
    @Autowired
    private MysqlGeneratorDao mySQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao() {
        if (Constant.MYSQL.equalsIgnoreCase(database)) {
            return mySQLGeneratorDao;
        } else {
            throw new RRException("不支持当前数据库：" + database);
        }
    }

}

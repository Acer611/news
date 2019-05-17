package td.news.configuration.mapper;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author sanlion do
 */
@Configuration
@MapperScan(basePackages = NewsMapperConfig.PACKAGE, sqlSessionFactoryRef = "newsSqlSessionFactory")

public class NewsMapperConfig {

    static final String DB = "TD_CP_News";
    static final String PACKAGE = "td.news.mapper." + DB;
    static final String MAPPER_LOCATION = "classpath:config/mapper/" + DB + "/*.xml";

    @Value("${config.db.news.url}") private String url;
    @Value("${config.db.news.username}") private String user;
    @Value("${config.db.news.password}") private String password;
    @Value("${config.db.driver}") private String driverClass;

    @Bean(name = "newsDataSource")
    @Primary
    public DataSource newsDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url); //数据源
        config.setUsername(user); //用户名
        config.setPassword(password); //密码
        config.setDriverClassName(driverClass);
        config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    @Bean(name = "newsTransactionManager")
    @Primary
    public DataSourceTransactionManager newsTransactionManager() {
        return new DataSourceTransactionManager(newsDataSource());
    }

    @Bean(name = "newsSqlSessionFactory")
    @Primary
    public SqlSessionFactory newsSqlSessionFactory(@Qualifier("newsDataSource") DataSource newsDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(newsDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(NewsMapperConfig.MAPPER_LOCATION));
        // 分页插件
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
//        properties.setProperty("returnPageInfo", "check");
//        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        //添加插件
        sessionFactory.setPlugins(new Interceptor[]{(Interceptor) pageHelper});

        return sessionFactory.getObject();
    }
}

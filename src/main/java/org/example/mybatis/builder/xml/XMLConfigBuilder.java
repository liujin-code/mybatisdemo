package org.example.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.datasource.DataSourceFactory;
import org.example.mybatis.io.Resources;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder extends BaseBuilder {
    private final Element root;

    public XMLConfigBuilder(Reader reader) {
        super(new Configuration());
        SAXReader saxReader = new SAXReader();
        try {
            Document read = saxReader.read(new InputSource(reader));
            root = read.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration parse()  {
        try {
            mapperElement(root.element("mappers"));
            environmentsElement(root.element("environments"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    /**
     * <environments default="development">
     *         <environment id="development">
     *             <transactionManager type="JDBC"/>
     *             <dataSource type="DRUID">
     *                 <property name="driver" value="com.mysql.jdbc.Driver"/>
     *                 <property name="url" value="jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true"/>
     *                 <property name="username" value="root"/>
     *                 <property name="password" value="Ab123456"/>
     *             </dataSource>
     *         </environment>
     *     </environments>
     */
    private void environmentsElement(Element environments) throws InstantiationException, IllegalAccessException {
        String environment = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element element : environmentList) {
            String id = element.attributeValue("id");
            if (id.equals(environment)) {
                JdbcTransactionFactory jdbcTransactionFactory =(JdbcTransactionFactory) typeAliasRegistry.resolveAlias(element.element("transactionManager").attributeValue("type")).newInstance();
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(element.element("dataSource").attributeValue("type")).newInstance();
                Properties properties = new Properties();
                element.element("dataSource").elements("property").forEach(property -> {
                    properties.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                });
                dataSourceFactory.setProperties(properties);
                Environment environment1 = Environment.builder().transactionFactory(jdbcTransactionFactory).dataSource(dataSourceFactory.getDataSource()).build();
                configuration.setEnvironment(environment1);
            }
        }
    }


    /*
     * <mappers>
     *	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     *
     *   <mapper class="cn.bugstack.mybatis.test.dao.IUserDao"/>
     * </mappers>
     */
    private void mapperElement(Element mappers) throws Exception{
        List<Element> mapper = mappers.elements("mapper");
        for (Element element : mapper) {
            String resource = element.attributeValue("resource");
            String mapperClass = element.attributeValue("class");

            // xml解析
            if (resource != null&& mapperClass == null){
                InputStream inputStream = Resources.getResourceAsStream(resource);
                // 在for循环里每个mapper都重新new一个XMLMapperBuilder，来解析
                XMLMapperBuilder mapperParser  = new XMLMapperBuilder(inputStream, configuration, resource);
                mapperParser.parse();
            }
            // annotation解析
            else if (resource == null && mapperClass != null){
                Class<?> mapperInterface = Resources.classForName(mapperClass);
                configuration.addMapper(mapperInterface);
            }
        }
    }
}

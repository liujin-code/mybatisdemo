package org.example.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.datasource.DataSourceFactory;
import org.example.mybatis.io.Resources;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLConfigBuilder extends BaseBuilder {
    private Element root;

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

    private void mapperElement(Element mappers) throws Exception{
        List<Element> mapper = mappers.elements("mapper");
        for (Element element : mapper) {
            String resource = element.attributeValue("resource");
            SAXReader saxReader = new SAXReader();
            Reader resourceAsReader = Resources.getResourceAsReader(resource);
            Document read = saxReader.read(new InputSource(resourceAsReader));
            Element rootElement = read.getRootElement();
            String namespace = rootElement.attributeValue("namespace");
            List<Element> select = rootElement.elements("select");
            for (Element element1 : select) {
                String id = element1.attributeValue("id");
                String resultType = element1.attributeValue("resultType");
                String parameterType = element1.attributeValue("parameterType");
                String sql = element1.getText();

                Map<Integer, String> parameterMap = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    sql = sql.replace(g1, "?");
                    parameterMap.put(i, g2);
                }
                String msId = namespace + "." + id;
                String nodeName = element1.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase());
                BoundSql boundSql = new BoundSql(parameterType, resultType, sql, parameterMap);
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
                configuration.addMappedStatement(mappedStatement);
            }
            configuration.addMapper(Resources.classForName(namespace));
        }
    }
}

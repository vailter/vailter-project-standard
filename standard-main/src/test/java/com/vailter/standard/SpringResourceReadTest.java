package com.vailter.standard;

//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
import com.vailter.standard.utils.CustomResourceReader;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 获取资源测试：
 *
 * @author Vailter67
 */
@SpringBootTest
public class SpringResourceReadTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * {@link org.springframework.core.io.ClassPathResource}加载classpath下的资源
     */
    @Test
    public void classPathResourceTest() throws IOException {
        String path = "application.properties";
        // 原始用法
        //InputStream in = SpringResourceReadTest.class.getClassLoader().getResourceAsStream(path);
        Resource resource = new ClassPathResource(path);
        InputStream in = resource.getInputStream();
        System.out.println(resource.getFile().getAbsolutePath());
        Properties properties = initProperties(in);
        properties.forEach((key, value) -> {
            Assertions.assertThat(key).isEqualTo("spring.profiles.active");
            Assertions.assertThat(value).isEqualTo("test");
        });
    }

    /**
     * {@link org.springframework.core.io.FileSystemResource}加载系统文件
     * 通常通过文件的绝对或者相对路径来读取。
     *
     * @throws IOException
     */
    @Test
    public void fileResourceTest() throws IOException {
        String path = "D:\\Repository\\vailter-project-standard\\standard-main\\src\\main\\resources\\application.properties";
        // 原始用法
        //InputStream in = new FileInputStream(path);
        FileSystemResource fileSystemResource = new FileSystemResource(path);
        InputStream in = fileSystemResource.getInputStream();
        Properties properties = initProperties(in);
        properties.forEach((key, value) -> {
            System.out.println(key + "=" + value);
            //Assertions.assertThat(key).isEqualTo("spring.profiles.active");
            //Assertions.assertThat(value).isEqualTo("test");
        });
    }

    /**
     * 如果你业务中需要使用延迟加载，我们可以使用类 ResourceLoader
     * {@link org.springframework.core.io.ResourceLoader}
     *
     * @throws IOException
     */
    @Test
    public void resourceLazyLoader() throws IOException {
        String path = "application.properties";
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(path);
        InputStream in = resource.getInputStream();
        Properties properties = initProperties(in);
        properties.forEach((key, value) -> System.out.println(key + "=" + value));
    }

    /**
     * 我们也可以使用 @Autowired 将 ResourceLoader 注入我们的bean
     * ApplicationContext 继承了ResourceLoader 接口。因此我们可以通过Spring bean 注入的方式来读取资源。
     *
     * @throws IOException
     */
    @Test
    public void resourceByApplicationContext() throws IOException {
        String path = "classpath:application.properties";
        Resource resource = applicationContext.getResource(path);
        //Resource resource = resourceLoader.getResource(path);
        InputStream in = resource.getInputStream();
        Properties properties = initProperties(in);
        properties.forEach((key, value) -> System.out.println(key + "=" + value));
    }

    @Value("classpath:application.properties")
    private Resource resource;

    @Value("classpath:1.jpg")
    private Resource imageResource;

    /**
     * 直接使用@Value注解将资源直接注入Spring bean。
     * 还可以读取图片
     *
     * @throws IOException
     */
    @Test
    public void resourceByValueAnno() throws IOException {
        InputStream in = resource.getInputStream();
        Properties properties = initProperties(in);
        properties.forEach((key, value) -> System.out.println(key + "=" + value));

        // 读取图片
        File file = imageResource.getFile();
        System.out.println(file.getName());
    }

    //@Value("#{new com.vailter.standard.utils.CustomResourceReader().readResource('application.properties')}") // 普通方法
    @Value("#{T(com.vailter.standard.utils.CustomResourceReader).staticReadResource('application.properties')}") // 静态方法
    private Properties properties;

    @Value("#{T(com.vailter.standard.utils.CustomResourceReader).customConfig('application.properties')}") // 静态方法
    private CustomResourceReader.CustomConfig customConfig;

    /**
     * 利用 SpEL(Spring Expression Language) 直接解析出来并注入对象
     */
    @Test
    public void resourceBySpEL() {
        properties.forEach((key, value) -> System.out.println(key + "=" + value));
        System.out.println(customConfig);
    }

    private Properties initProperties(InputStream in) throws IOException {
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }

    /**
     * test.list[0]=aaa
     * test.list[1]=bbb
     * test.list[2]=ccc
     * 这种会报错：Could not resolve placeholder 'test.list' in value "${test.list}"
     * 解决：新建一个类，定义List，注入这个类
     */
    //@Value("${test.list}")
    //private List<String> stringList;

    /**
     * test.list1=ds,22das2,333adsa
     * 下面这几种都是正确的
     */
    //@Value("${test.list1}")
    //@Value("#{'${test.list1}'}") // EL 表达式
    @Value("#{'${test.list1}'.split(',')}") // EL 表达式
    //private List<String> stringList;
    private String[] strings;

    @Value("#{'${test.list1:}'.empty ? null : '${test.list:}'.split(',')}")
    private List<String> testList;
    @Value("#{'${test.set:}'.empty ? null : '${test.set:}'.split(',')}")
    private Set<Integer> testSet;

    /**
     * map：
     * test:
     * map1: '{"name": "zhangsan", "sex": "male"}'
     * map2: '{"math": "90", "english": "85"}'
     */
    //@Value("#{${test.map1}}")
    //private Map<String, String> map1;
    //
    //@Value("#{${test.map2}}")
    //private Map<String, Integer> map2;

    @Value("#{T(com.vailter.standard.utils.MapUtils).decodeMap(${test.map1:}?:null)}")
    private Map<String, String> map1;
    @Value("#{T(com.vailter.standard.utils.MapUtils).decodeMap(${test.map2:}?:null)}")
    private Map<String, String> map2;

    @Test
    void listOrMap() {
        //System.out.println(stringList);
        System.out.println(ArrayUtils.toString(strings));
        System.out.println(map1);
        System.out.println(map2);
    }

    @Test
    void testGetProjectPath() throws FileNotFoundException {
        //获取跟目录
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) path = new File("");
        System.out.println("path:"+path.getAbsolutePath());

//如果上传目录为/static/images/upload/，则可以如下获取：
        File upload = new File(path.getAbsolutePath(),"static/images/upload/");
        if(!upload.exists()) upload.mkdirs();
        System.out.println("upload url:"+upload.getAbsolutePath());
//在开发测试模式时，得到的地址为：{项目跟目录}/target/static/images/upload/
//在打包成jar正式发布时，得到的地址为：{发布jar包目录}/static/images/upload/
    }
}

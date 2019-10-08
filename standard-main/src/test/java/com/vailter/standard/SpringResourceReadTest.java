package com.vailter.standard;

import com.vailter.standard.utils.CustomResourceReader;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
}

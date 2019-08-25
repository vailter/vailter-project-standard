package com.vailter.standard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 静态资源的解释：
 * <p>
 * static-path-pattern：访问模式，默认为/**，多个可以逗号分隔
 * <p>
 * static-locations：资源目录，多个目录逗号分隔，
 * 默认资源目录为classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
 * <p>
 * Spring boot默认对/**的访问可以直接访问四个目录下的文件：
 * <p>
 * classpath:/public/
 * classpath:/resources/
 * classpath:/static/
 * classpath:/META-INFO/resources/
 *
 * @author Vailter67
 */
@RestController
public class StaticResourceController {
    @RequestMapping("/static/**")
    public void getHtml(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        String[] arr = uri.split("static/");

        String resourceName = "index.html";

        if (arr.length > 1) {
            resourceName = arr[1];
        }

        String url = StaticResourceController.class.getResource("/").getPath() + "html/" + resourceName;
        try {
            FileReader reader = new FileReader(new File(url));
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            response.getOutputStream().write(sb.toString().getBytes());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

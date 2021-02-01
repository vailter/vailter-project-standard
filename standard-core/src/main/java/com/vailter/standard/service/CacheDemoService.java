package com.vailter.standard.service;

import com.vailter.standard.domain.Customer;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "my-redis-cache1", cacheManager = "redisCacheManager")
public class CacheDemoService {

    @Cacheable(key = "#id", sync = true)
    public Customer getCustomer(Integer id) {
        System.out.println("操作数据库，返回customer");
        return new Customer(id, "卡卡罗特", 100);
    }


    /**
     * 使用@CachePut注解的方法，一定要有返回值，该注解声明的方法缓存的是方法的返回结果。
     * it always causes the
     * method to be invoked and its result to be stored in the associated cache
     **/
    @CachePut(key = "#customer.getId()")
    public Customer setCustomer(Customer customer) {
        System.out.println("存入数据库");
        return customer;
    }

    @CacheEvict(key = "#id")
    public void deleteCustomer(Integer id) {
        System.out.println("删除数据库中customer");
    }

    @CachePut(key = "#customer.getId()")
    public Customer updateCustomer(Customer customer) {
        System.out.println("修改customer,并存入数据库");
        return customer;
    }
}

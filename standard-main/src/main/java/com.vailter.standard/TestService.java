package com.vailter.standard;

import com.vailter.standard.datasource.dynamic.annotation.DataSource;
import com.vailter.standard.datasource.dynamic.annotation.Product;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    //@Product
    @DataSource("1231231")
    public void test() {

    }
}

package com.vailter.standard.rule.urule.converters;

import com.vailter.standard.rule.urule.domain.TestConfig;
import com.vailter.standard.rule.urule.dto.TestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestConverter {
    @Mapping(target = "age", source = "type")
    TestDTO fromTestConfig(TestConfig config);
}

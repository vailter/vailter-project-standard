package com.vailter.standard.utils.desensitization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveTypeEnum type;

    public SensitiveInfoSerialize(SensitiveTypeEnum type) {
        this.type = type;
    }

    public SensitiveInfoSerialize() {
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (StringUtils.isNotBlank(value)) {
            switch (type) {
                case CHINESE_NAME:
                    value = DesensitizedUtils.chineseName(value);
                    break;
                case ID_CARD:
                    value = DesensitizedUtils.idCardEncrypt(value);
                    break;
                case FIXED_PHONE:
                    value = DesensitizedUtils.fixedPhone(value);
                    break;
                case MOBILE_PHONE:
                    value = DesensitizedUtils.mobileEncrypt(value);
                    break;
                case ADDRESS:
                    value = DesensitizedUtils.address(value, 8);
                    break;
                case EMAIL:
                    value = DesensitizedUtils.email(value);
                    break;
                case BANK_CARD:
                    value = DesensitizedUtils.bankCard(value);
                    break;
                case PASSWORD:
                    value = DesensitizedUtils.password(value);
                    break;
                case CAR_NUMBER:
                    value = DesensitizedUtils.carNumber(value);
                    break;
                case WORK_NO:
                    value = DesensitizedUtils.idPassport(value, 4);
                    break;
                default:
            }
        }
        gen.writeString(value);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) { // 为空直接跳过
            if (Objects.equals(property.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
                Desensitized desensitized = property.getAnnotation(Desensitized.class);
                if (desensitized == null) {
                    desensitized = property.getContextAnnotation(Desensitized.class);
                }
                if (desensitized != null) { // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize

                    return new SensitiveInfoSerialize(desensitized.type());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }
}

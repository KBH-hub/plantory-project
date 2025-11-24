package com.zero.plantory.global.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

@Component
public class XmlToJsonConverter {

    private final XmlMapper xmlMapper = new XmlMapper();

    public JsonNode convert(String xml) {
        try {
            return xmlMapper.readTree(xml);
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }
}

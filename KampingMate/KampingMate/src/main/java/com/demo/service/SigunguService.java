package com.demo.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SigunguService {

    public Map<String, List<String>> getSigunguData() {
        Map<String, List<String>> sigunguMap = new HashMap<>();

        try {
            File file = new File("C:/Users/tiger/Kamp/sigungu_data.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String siDo = element.getElementsByTagName("si_do").item(0).getTextContent();
                String gunGu = element.getElementsByTagName("gun_gu").item(0).getTextContent();

                sigunguMap.computeIfAbsent(siDo, k -> new ArrayList<>()).add(gunGu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sigunguMap;
    }
}

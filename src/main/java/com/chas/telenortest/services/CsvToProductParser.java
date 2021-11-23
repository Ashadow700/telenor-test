package com.chas.telenortest.services;

import com.chas.telenortest.dataObjects.Product;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CsvToProductParser {

    private final static Logger LOG = LoggerFactory.getLogger(CsvToProductParser.class);

    private final static int CSV_ORDER_TYPE = 0;
    private final static int CSV_ORDER_PROPERTIES = 1;
    private final static int CSV_ORDER_PRICE = 2;
    private final static int CSV_ORDER_LOCATION_OF_SALE = 3;

    private final static String ADDRESS_CITY_SEPARATOR = ", ";

    public static List<Product> parseCsvToProduct(List<String[]> csvList) {
        csvList.remove(0); //First line only contains info, not actual product data

        return csvList.stream()
                .map(CsvToProductParser::createProduct)
                .collect(Collectors.toList());
    }

    private static Product createProduct(String[] productData) {
        String type = productData[CSV_ORDER_TYPE];
        String properties = productData[CSV_ORDER_PROPERTIES];
        double price = Double.parseDouble(productData[CSV_ORDER_PRICE]);
        String addressOfSale = StringUtils.substringBefore(productData[CSV_ORDER_LOCATION_OF_SALE], ADDRESS_CITY_SEPARATOR);
        String cityOfSale = StringUtils.substringAfter(productData[CSV_ORDER_LOCATION_OF_SALE], ADDRESS_CITY_SEPARATOR);

        LOG.info("Creating Product from csv data with type " + type + ", properties " + properties + ", price " + price +
                ", addressOfSale " + addressOfSale + ", and cityOfSale " + cityOfSale);

        return new Product(type, properties, price, addressOfSale, cityOfSale);
    }

}

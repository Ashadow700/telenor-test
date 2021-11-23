package com.chas.telenortest.services;


import com.chas.telenortest.api.ProductController;
import com.chas.telenortest.constants.ProductTypes;
import com.chas.telenortest.constants.PropertyTypes;
import com.chas.telenortest.dataObjects.Product;
import com.chas.telenortest.repositories.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final static Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductRepository productRepository;

    public List<Product> fetchFilteredProducts(
            Optional<String> type,
            Optional<Double> minPrice,
            Optional<Double> maxPrice,
            Optional<String> property,
            Optional<String> propertyColor,
            Optional<Double> gbLimitMin,
            Optional<Double> gbLimitMax,
            Optional<String> address,
            Optional<String> city) {

        LOG.info("Filtering products");

        return productRepository.findAll().stream()
                .filter(product -> filterType(product, type))
                .filter(product -> filterMinPrice(product, minPrice))
                .filter(product -> filterMaxPrice(product, maxPrice))
                .filter(product -> filterAddress(product, address))
                .filter(product -> filterCity(product, city))
                .filter(product -> filterProperty(product, property, propertyColor, gbLimitMin, gbLimitMax))
                .collect(Collectors.toList());
    }

//    Developer's note for the test: This method could be shortened into one line:
//        return type.isEmpty() || product.getType().equalsIgnoreCase(type.get());
//    The same pattern works for the other filters too. I like to separate the two requirements though, as I think
//    it increases code readability. Just a personal preference.
    private boolean filterType(Product product, Optional<String> type) {
        if(type.isEmpty()) {
            return true;
        }
        return product.getType().equalsIgnoreCase(type.get());
    }

    private boolean filterMinPrice(Product product, Optional<Double> minPrice) {
        if(minPrice.isEmpty()) {
            return true;
        }
        return minPrice.get() <= product.getPrice();
    }

    private boolean filterMaxPrice(Product product, Optional<Double> maxPrice) {
        if(maxPrice.isEmpty()) {
            return true;
        }
        return product.getPrice() <= maxPrice.get();
    }

    private boolean filterAddress(Product product, Optional<String> address) {
        if(address.isEmpty()) {
            return true;
        }
        return product.getAddressOfSale().toLowerCase().contains(address.get().toLowerCase());
    }


    private boolean filterCity(Product product, Optional<String> city) {
        if(city.isEmpty()) {
            return true;
        }
        return product.getCityOfSale().toLowerCase().contains(city.get().toLowerCase());
    }

    //Developer's notes for test: This tree is extremely awkward. I've written more about it under known issues
    private boolean filterProperty(Product product, Optional<String> property, Optional<String> propertyColor,
                                   Optional<Double> gbLimitMin, Optional<Double> gbLimitMax) {

        if (property.isEmpty() && (propertyColor.isPresent() || gbLimitMin.isPresent() || gbLimitMax.isPresent())) {
            throw new RuntimeException("The parameter property must be set if any of the property:color, property:gb_limit_min" +
                    "or property:gb_limit_Max is used");
        } else if (property.isEmpty()) {
            return true;

        } else if (property.get().equals(PropertyTypes.COLOR)) {
            return filterPropertyColor(product, propertyColor);

        } else if (property.get().equals(PropertyTypes.GB_LIMIT)) {
            return filterPropertyGbLimit(product, gbLimitMin, gbLimitMax);
        } else {
            throw new RuntimeException("If parameter property is used, the value must be either " + PropertyTypes.COLOR +
                    "or " + PropertyTypes.GB_LIMIT);
        }
    }

    private boolean filterPropertyColor(Product product, Optional<String> propertyColor) {

        if(!product.getType().equals(ProductTypes.PHONE)) {
            return true; // Color filter is only applicable to Phone products
        } else if (propertyColor.isEmpty()) {
            return true;
        }

        String productColor = StringUtils.substringAfter(product.getProperty(), ":");
        return propertyColor.get().equalsIgnoreCase(productColor);
    }

    private boolean filterPropertyGbLimit(Product product, Optional<Double> gbLimitMin, Optional<Double> gbLimitMax) {

        if(!product.getType().equals(ProductTypes.SUBSCRIPTION)) {
            return true; // Gb limit filter is only applicable to subscriptions
        } else if(gbLimitMin.isEmpty() && gbLimitMax.isEmpty()) {
            return true;
        }

        double productGbLimit = Double.parseDouble(StringUtils.substringAfter(product.getProperty(), ":"));
        if(gbLimitMin.isPresent() && gbLimitMax.isEmpty()) {
            return gbLimitMin.get() <= productGbLimit;

        } else if(gbLimitMin.isEmpty() && gbLimitMax.isPresent()) {
            return productGbLimit <= gbLimitMax.get();

        } else {
            return gbLimitMin.get() <= productGbLimit && productGbLimit <= gbLimitMax.get();
        }
    }

}

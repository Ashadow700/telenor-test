package com.chas.telenortest.api;

import com.chas.telenortest.dataObjects.Product;
import com.chas.telenortest.repositories.ProductRepository;
import com.chas.telenortest.services.ProductService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final static Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getFilteredProducts(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, name = "min_price") Double minPrice, //Primate types cannot be optional, which is why Double is used instead of double
            @RequestParam(required = false, name = "max_price") Double maxPrice,
            @RequestParam(required = false) String property,
            @RequestParam(required = false, name = "property:color") String propertyColor,
            @RequestParam(required = false, name = "property:gb_limit_min") Double gbLimitMin,
            @RequestParam(required = false, name = "property:gb_limit_max") Double gbLimitMax,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city) {

        LOG.info("Received GET filtered Products request");

        List<Product> products = productService.fetchFilteredProducts(
                Optional.ofNullable(type),
                Optional.ofNullable(minPrice),
                Optional.ofNullable(maxPrice),
                Optional.ofNullable(property),
                Optional.ofNullable(propertyColor),
                Optional.ofNullable(gbLimitMin),
                Optional.ofNullable(gbLimitMax),
                Optional.ofNullable(address),
                Optional.ofNullable(city)
        );

        //Developer's note for the test: Tbh, I don't really see much point in wrapping the list in a data tag, we could
        // have just returned List<Product> in a ResponseEntity directly, but the test said I should, so I did that
        Map<String, Object> result = new JSONObject()
                .put("data", products)
                .toMap();

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }




}

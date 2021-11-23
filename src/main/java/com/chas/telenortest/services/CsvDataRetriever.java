package com.chas.telenortest.services;

import com.chas.telenortest.dataObjects.Product;
import com.chas.telenortest.repositories.ProductRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CsvDataRetriever {

    private final static Logger LOG = LoggerFactory.getLogger(CsvDataRetriever.class);

    @Autowired
    ProductRepository productRepository;

    @Value("${com.chas.csv-data-path}")
    private String csvDataPath;

    @PostConstruct
    public void RetrieveProductData() {

        LOG.info("Starting read products from " + csvDataPath);

        readCsvData().ifPresentOrElse(
                this::parseAndPostCsvData,
                () -> LOG.warn("Failed to read csv data. No products from csv file will be posted to database")
        );
    }

    private Optional<List<String[]>> readCsvData() {
        try (CSVReader reader = new CSVReader(new FileReader(csvDataPath))) {
            return Optional.of(reader.readAll());
        } catch (IOException | CsvException e) {
            LOG.error("Failed to read csv data from " + csvDataPath, e);
            return Optional.empty();
        }

    }

    private void parseAndPostCsvData(List<String[]> csvList) {
        List<Product> productList = CsvToProductParser.parseCsvToProduct(csvList);
        productRepository.saveAll(productList);
        LOG.info("Finished saving all Products from csv data file");
    }
}


package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements IElasticsearchService {

    String INDEX = "bookstore";
    String TYPE = "book";

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Gson gson;

    @Override
    public String createBook(Book book) throws IOException {

        Map<String, Object> documentMapper = objectMapper.convertValue(book, Map.class);
        String data = objectMapper.writeValueAsString(book);
        System.out.println(data);
        IndexRequest index = new IndexRequest(INDEX);
        System.out.println(index);
        index.id(Integer.toString(book.getId()));
        index.source(data, XContentType.JSON);
        IndexResponse indexResponse = client.index(index, RequestOptions.DEFAULT);
        return indexResponse.getResult().name();
    }
}

package com.awsdemo.imagetagger.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;

@Service
public class ElasticsearchServiceImpl {
	
//	@Autowired
//	private AWSElasticsearch searchClient;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	// Can't inject into static field 
	@Value("${elasticsearch.host}")
	private String host;
	
	@Value("${elasticsearch.index}")
	private String index;
	
	private final String serviceName = "es";

	private RestHighLevelClient esClient;
	
	@PostConstruct
    private void esClient() {
    	final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
        this.esClient = new RestHighLevelClient(RestClient.builder(HttpHost.create(host)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }

	
	public String getAll() {
		SearchRequest req = new SearchRequest(index);
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.matchAllQuery());
		req.source(builder);
		try {
			return esClient.search(req, RequestOptions.DEFAULT).toString();
		} catch (IOException e) {
			return "Host: " + host + "\nE: " + e.getMessage();
		}
	}
	
	public String searchStr(String term) {
		SearchRequest req = new SearchRequest(index);
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.matchQuery("labels", term));
		req.source(builder);
		try {
			return esClient.search(req, RequestOptions.DEFAULT).toString();
		} catch (IOException e) {
			return e.getMessage();
		}
	}
	
	public List<String> search(String term) throws IOException {
		SearchRequest req = new SearchRequest(index);
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.matchQuery("labels", term));
		req.source(builder);
		List<String> keys = new ArrayList<String>();
		SearchResponse res = esClient.search(req, RequestOptions.DEFAULT);
		for (SearchHit hit : res.getHits().getHits()) {
			keys.add(hit.field("s3key").toString());
		}
		return keys;
	}
}

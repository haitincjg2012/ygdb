package com.apec.framework.elasticsearch.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.util.Collections;

/**
 * Title:
 *
 * @author yirde  2017/7/7.
 */
public class RemotingClient   {

     public static void main(String [] args) throws  Exception{

         RestClient restClient = RestClient.builder(
                 new HttpHost("192.168.7.22", 9200),
                 new HttpHost("192.168.7.21", 9200)).build();

         Response response = restClient.performRequest("GET", "/",
                 Collections.singletonMap("pretty", "true"));
         System.out.println(EntityUtils.toString(response.getEntity()));
//
         HttpEntity entity = new NStringEntity(
                 "{\n" +
                         "    \"user\" : \"kimchy\",\n" +
                         "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                         "    \"message\" : \"trying out Elasticsearch\"\n" +
                         "}", ContentType.APPLICATION_JSON);
             Response indexResponse = restClient.performRequest(
                 "PUT",
                 "/blog/post/190345074876480",
                 Collections.<String, String> emptyMap(),
                 entity);

         System.out.println(Runtime.getRuntime().availableProcessors());
//
//         int numRequests = 25;
//         final CountDownLatch latch = new CountDownLatch(numRequests);
//
//         for (int i = 0; i < numRequests; i++) {
//             HttpEntity entitys = new NStringEntity(
//                     "{\n" +
//                             "    \"user\" : \"kim\",\n" +
//                             "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
//                             "    \"message\" : \"trying out Elasticsearch \"\n" +
//                             "}", ContentType.APPLICATION_JSON);
//             restClient.performRequestAsync(
//                     "PUT",
//                     "/blog/post/" + i,
//                     Collections.<String, String>emptyMap(),
//                     //assume that the documents are stored in an entities array
//                     entitys,
//                     new ResponseListener() {
//                         @Override
//                         public void onSuccess(eslistener response) {
//                             System.out.println("===========================");
//                             System.out.println(response);
//                             latch.countDown();
//                         }
//
//                         @Override
//                         public void onFailure(Exception exception) {
//                             System.out.println("=========*********======");
//                             System.out.println(response);
//                             latch.countDown();
//                         }
//                     }
//             );
//         }
//
//         latch.await();
         restClient.close();
         System.out.println("END");
     }

}

package index;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;


/**
 * ElasticSearch Java Api(二) -检索索引库
 */
public class ElasticSearchGet {

	/**
	 * 一次查询可分为下面四个步骤：
	 */
	public static void main(String[] args) {
		// 1.创建连接ElasticSearch服务的client.
		try {
			//索引在ElasticSearch服务器上，进行索引的查询首先要和服务器创建连接，这是第一步。
			Client client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

			//2.创建QueryBuilder.QueryBuilder可以设置单个字段的查询,也可以设置多个字段的查询.
			//e.g.1: 查询title字段中包含hibernate关键字的文档:
			QueryBuilder qb1 = termQuery("title", "hibernate");
			//e.g.2: 查询title字段或content字段中包含Git关键字的文档:
			QueryBuilder qb2 = QueryBuilders.multiMatchQuery("git", "title", "content");

			// 3.执行查询
			// 通过client设置查询的index、type、query.返回一个SearchResponse对象：
			SearchResponse response = client.prepareSearch("blog").setTypes("article").setQuery(qb2)
					.execute().actionGet();

			//4.处理查询结果
			//SearchResponse对象的getHits()方法获取查询结果,返回一个SearchHits的集合，遍历集合获取查询的文档信息：
			SearchHits hits = response.getHits();
			if (hits.totalHits() > 0) {
				for (SearchHit hit : hits) {
					System.out.println("score:" + hit.getScore() + ":\t" + hit.getSource());// .get("title")
				}
			} else {
				System.out.println("搜到0条结果");
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
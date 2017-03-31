package index;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 创建索引、添加数据
 */
public class ElasticSearchHandler {
	public static void main(String[] args) {
		    /* 创建客户端 */
		// client startup
		try {
			Client client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

			List<String> jsonData = DataFactory.getInitJsonData();

			for (int i = 0; i < jsonData.size(); i++) {
				IndexResponse response = client.prepareIndex("blog", "article").setSource(jsonData.get(i)).get();
				if (response.isCreated()) {
					System.out.println("创建成功!");
				}
			}
			client.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
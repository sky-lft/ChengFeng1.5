package com.beautifulsoup.chengfeng.elasticsearch;

import com.beautifulsoup.chengfeng.pojo.PurchaseProduct;
import com.beautifulsoup.chengfeng.repository.PurchaseInfoRepository;
import com.beautifulsoup.chengfeng.service.dto.PurchaseInfoDto;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ElasticSearchTest {

    @Autowired
    private PurchaseInfoRepository purchaseInfoRepository;

    @Test
    public void demo(){
        NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();
        // 不查询任何结果
        builder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));


        builder.addAggregation(
                AggregationBuilders.terms("品牌").field("subtitle"));
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<PurchaseInfoDto> aggPage = (AggregatedPage<PurchaseInfoDto>) this.purchaseInfoRepository.search(builder.build());

        StringTerms agg = (StringTerms) aggPage.getAggregation("subtitle");
        // 3.2、获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        // 3.3、遍历
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key，即品牌名称
            System.out.println(bucket.getKeyAsString());
            // 3.5、获取桶中的文档数量
            System.out.println(bucket.getDocCount());
        }


    }
}

package com.mobeom.local_currency.recommend;

import static com.mobeom.local_currency.store.QStore.store;
import static com.mobeom.local_currency.industry.QIndustry.industry;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.mobeom.local_currency.consume.QGenderAge.genderAge;


import com.mobeom.local_currency.consume.GenderAge;
import com.mobeom.local_currency.industry.Industry;
import com.mobeom.local_currency.join.IndustryStore;
import com.mobeom.local_currency.store.QStore;
import com.mobeom.local_currency.store.Store;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.sql.DataSource;
import java.util.List;

interface CustomRecommendRepository {
    Store recommendStores(String searchWord);
    List<Store> fetchByBestStore(String searchLocalWord);
    List<StoreVo> testRecommend(String storeName, String storeType);
    List<IndustryStore> fetchByIndustry(String searchIndustry);
    List<GenderAge> industryByGenderAndAge(String searchWord, int age);

    List<GenderAge> industryByAge(int age);
}

public class RecommendRepositoryImpl extends QuerydslRepositorySupport implements CustomRecommendRepository {
    private final JPAQueryFactory queryFactory;
    private final DataSource dataSource;


    RecommendRepositoryImpl(JPAQueryFactory queryFactory, DataSource dataSource) {
        super(Recommend.class);
        this.queryFactory = queryFactory;
        this.dataSource = dataSource;
    }




    @Override //mahout을 통한 가맹점 추천
    public Store recommendStores(String searchWord) {
        QStore store = QStore.store;
        JPAQueryFactory query = new JPAQueryFactory(getEntityManager());
        Store recommendStore = new Store();
        recommendStore = query.select(Projections.fields
                (Store.class, store.storeName, store.storeType, store.starRanking, store.id)).from(store)
                .where(store.id.like(searchWord)).fetchOne();
        return recommendStore;
    }

    @Override //단순 가맹점 추천(서치 순)
    public List<Store> fetchByBestStore(String searchLocalWord){
        return queryFactory.select(Projections.fields(Store.class, store.storeName, store.storeType, store.starRanking))
                .from(store).where(store.address.contains(searchLocalWord))
                .orderBy(store.searchResultCount.desc()).limit(10).fetch();
    }


    @Override //서브쿼리 예제
    public List<StoreVo> testRecommend(String storeName, String storeType){
        return queryFactory
                .select(Projections.fields(StoreVo.class,
                        store.storeName.as("storeName"),
                        store.storeType.as("storeType"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(store.id))
                                        .from(store)
                                        .where(store.storeName.startsWith(storeName), store.storeType.contains(storeType)),
                                "passengerCounter")
                ))
                .from(store)
                .fetch();
    }

    @Override //업종명으로 가맹점 추천(img 연결된 ver)
    public List<IndustryStore> fetchByIndustry(String searchIndustry) {
        return queryFactory.select(Projections.fields(IndustryStore.class,
                store.storeName.as("storeName"),
                store.mainCode.as("mainCode"),
                store.starRanking.as("starRanking"),
                industry.industryImageUrl.as("imgUrl"),
                store.storeType.as("storeType"))
                )
                .from(store).innerJoin(industry)
                .on(store.storeTypeCode.eq(industry.industryCode))
                .fetchJoin().where(industry.mainCode.contains(searchIndustry))
                .orderBy(store.searchResultCount.desc()).limit(10).fetch();
    }
    //고양시도 검색되는데 상관 없나... 헷갈린다.

    @Override //성별 및 연령 입력시 대분류 안내
    public List<GenderAge> industryByGenderAndAge(String searchWord, int age){
        return queryFactory.selectFrom(genderAge).where(genderAge.genderCode.eq(searchWord),genderAge.ageGroup.eq(age))
                .orderBy(genderAge.amount.desc()).distinct().limit(7).fetch();
    }


    @Override // 연령으로 대분류 추천
    public List<GenderAge> industryByAge(int age) {
        return queryFactory.selectFrom(genderAge).where(genderAge.ageGroup.eq(age)).orderBy(genderAge.amount.desc()).limit(5).fetch();
    }



}

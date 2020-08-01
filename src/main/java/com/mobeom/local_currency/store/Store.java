package com.mobeom.local_currency.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity @Getter @Setter @ToString @NoArgsConstructor
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") private Long id;

    @Column(name = "store_name",nullable = false) private String storeName;

    @Column(name = "store_type_code",nullable = false) private int storeTypeCode;

    @Column(name = "store_type", nullable = false) private String storeType;

    @Column(name = "local_name", nullable = false) private String localName;

    @Column(name = "road_address", nullable = false) private String address;

    @Column(name = "store_phone", nullable = false) private String storePhone;

    @Column(name = "latitude",nullable = false) private int latitude;

    @Column(name = "logitude", nullable = false) private int longitude;

    @Column(name = "star_ranking",nullable = false) private int starRanking;

    @Column(name = "search_result_count",nullable = false) private int searchResultCount;

}

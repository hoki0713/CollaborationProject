package com.mobeom.local_currency.store;

import com.mobeom.local_currency.proxy.Box;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.tools.jconsole.JConsole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/stores")
@AllArgsConstructor
public class StoreController {
    private final StoreService storeService;
    static Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired Box box;



    @GetMapping("/fromAddr/{lat}/{lng}")
    public Map<?,?> getStoreByAddr(@PathVariable String lat, @PathVariable String lng){
        logger.info("getStoreByAddr()");
        box.clear();
        box.put("list", storeService.getStores(lat,lng));
        return box.get();
    }// 좌표근처 가맹점 찾기


    @GetMapping("/realTimeSearch/{searchWD}")
    public Map<?,?>  realTimeSearch(@PathVariable String searchWD){
        logger.info("realTimeSearch()  "+"searchWD:"+searchWD);
        Object results = storeService.getSeveral(searchWD);
        box.clear();
        if (results != null) {
            box.put("msg", "success");
            box.put("list", results);
        } else {
            box.put("msg", "fail");
        }
        return box.get();
    }// eunsong findBestRoute addStore search

    @GetMapping("/getSome/{stateName}/{category}/{pageNow}")
    public Map<?,?> getStoreList(@PathVariable(name = "stateName") String stateName,
                                 @PathVariable(name = "category") String category,
                                 @PathVariable(name = "pageNow") int pageNow){
        logger.info("getStoreList()");
        box.clear();
        Object results = storeService.getSome(stateName, category, pageNow);
        System.out.println(results);
        if (results != null) {
            box.put("msg", "success");
            box.put("list", results);
        } else {
            box.put("msg", "fail");
        }

        return box.get();
    }// eunsong MerchanList //have to add pagenation


    @GetMapping("/findStore/{storeName}")
    public ResponseEntity<Map<Long, SearchStoreVO>> findStoreByName(@PathVariable String storeName) {
        Map<Long, SearchStoreVO> resultStores = new HashMap<>();
        Optional<List<Store>> storeList = storeService.findAllStoreByName(storeName);
        if(storeList.isPresent()) {
            storeList.get().forEach(store -> {
                SearchStoreVO storeInfo = new SearchStoreVO();
                storeInfo.setStoreName(store.getStoreName());
                storeInfo.setStoreLocal(store.getLocalName());
                storeInfo.setStoreAddr(store.getAddress());
                resultStores.put(store.getId(), storeInfo);
            });
            return ResponseEntity.ok(resultStores);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getStores/{localName}")
    public ResponseEntity<Map<Long,StoresVO>> getAllStoresByLocalName (@PathVariable String localName) {
        Map<Long,StoresVO> storesMap = storeService.getAllStoresByLocalName(localName.trim());
        return ResponseEntity.ok(storesMap);
    }

    @GetMapping("/searchStore/{searchWord}")
    public ResponseEntity<List<Store>> searchStore(@PathVariable String searchWord) {
        List<Store> storeList = storeService.findStoreBySearchWord(searchWord);
        return ResponseEntity.ok(storeList);
    }


}

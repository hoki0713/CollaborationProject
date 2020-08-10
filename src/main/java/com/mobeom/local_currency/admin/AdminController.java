package com.mobeom.local_currency.admin;

import com.mobeom.local_currency.join.SalesVoucher;
import com.mobeom.local_currency.proxy.Box;
import com.mobeom.local_currency.sales.Sales;
import com.mobeom.local_currency.user.User;
import com.querydsl.core.Tuple;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admins")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final Box box;

    public AdminController(AdminRepository adminRepository, AdminService adminService, Box box) {
        this.adminRepository = adminRepository;
        this.adminService = adminService;
        this.box = box;
    }


    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUserList(){
        List<User> userList = adminService.getAllUserList();
        return ResponseEntity.ok(userList);
    }





    @GetMapping("/list/{userId}")
    public User getOneUser(@PathVariable String userId) {
        Optional<User> findOne = adminService.findOneUser(userId);
        return findOne.orElse(null);
       // return findOne.isPresent()? ResponseEntity.ok(findOne) : ResponseEntity.notFound().build();

    }
    


    @GetMapping("/userTotal-chart/{localSelect}")
    public Box<Map<?,?>> userLocalGenderChart(@PathVariable String localSelect){
        Map<String,Long> testChart1 = adminService.userLocalGenderChart(localSelect);
        Map<String,Integer> testChart2 = adminService.userAgeTotal(localSelect);
       box.put("test1",testChart1);
        box.put("test2",testChart2);

        //return adminService.userLocalGenderChart(localSelect);
        return box;
    }

    @GetMapping("/chart/ratio-of-user-region")
    public Map<String,Long> ratioOfUserRegion(){

        return adminService.localTotalChart();
    }

    @GetMapping("/userAge-chart/{localSelect}")
    public Map<String,Integer> userAgeChart(@PathVariable String localSelect){
        Map<String,Integer> userAge = adminService.userAgeTotal(localSelect);

        return userAge;
    }

    @GetMapping("/joinDate-chart/{startDate}/{endDate}") //* 아직 구현 xx
    public void joinChart(@PathVariable String startDate, @PathVariable String endDate){
        LocalDate start_date = LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
        LocalDate end_date = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
        adminService.joinChart(start_date,end_date);
    }

    @GetMapping("/localChart/{localSelect}")
    public Long storeLocalChart(@PathVariable String localSelect){
        return adminService.storeLocalsChart(localSelect);

    }


    @GetMapping("/storeTypeChart")
    public Map<String,Long> storeTypeChart(){
        return adminService.storeTypeChart();
    }


    @GetMapping("/currency/month/total")
    public ResponseEntity<List<Sales>> currencySalesMonthTotalChart(){
        List<Sales> monthList = adminService.salesMonthChart();
       return ResponseEntity.ok(monthList);
    }

    @GetMapping("/voucher/name-list/{voucherName}")
    public SalesVoucher voucherNameChart(@PathVariable String voucherName){ //SalesVoucher voucherNameChart(String voucherName)
        System.out.println(adminService.voucherNameChart(voucherName).toString());
        return adminRepository.voucherNameChart(voucherName);
    }

    @GetMapping("/useChart/test/{useSelect}/{localName}")
    public Integer test3(@PathVariable String useSelect, @PathVariable String localName){
        System.out.println(adminService.useChartTest(useSelect,localName));
        return adminService.useChartTest(useSelect,localName);
    }
}

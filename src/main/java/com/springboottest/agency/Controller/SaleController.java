package com.springboottest.agency.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboottest.agency.Dto.SaleRequest;
import com.springboottest.agency.Entity.ProductUser;
import com.springboottest.agency.Entity.SaleUser;
import com.springboottest.agency.Entity.StockManagement;
import com.springboottest.agency.Service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/{id}")
        public String sale(@PathVariable Long id, 
                      @RequestBody SaleRequest saleRequest ) {

            inventoryService.saleProduct(id, saleRequest.getQuantity(), saleRequest.getSellingPrice());
            return "✅Sale recorded successfully!";
   
        }

    @GetMapping("/all")
         public ResponseEntity<List<SaleUser>> getAllSale() {
            return ResponseEntity.ok(inventoryService.getAllSale());
    }
        

}

package com.springboottest.agency.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboottest.agency.Entity.StockManagement;
import com.springboottest.agency.Service.InventoryService;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private InventoryService inventoryService;


    @PostMapping("/stock/add/{vendorId}")
    public ResponseEntity<?> addStock(@PathVariable Long vendorId,
                                      @RequestBody StockManagement stock) {

        return ResponseEntity.ok(inventoryService.addStock(vendorId, stock));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockManagement>> getAllStock() {
        return ResponseEntity.ok(inventoryService.getAllStock());
    }

    @PutMapping("/stock/update/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id,
                                         @RequestBody StockManagement updated) {

        return ResponseEntity.ok(inventoryService.updateStock(id, updated));
    }

    @DeleteMapping("/stock/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable Long id) {
        boolean deleted = inventoryService.deleteStock(id);

        return deleted ? ResponseEntity.ok("Stock Deleted Successfully")
                       : ResponseEntity.status(404).body("Stock Not Found");
    }
}

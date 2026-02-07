package com.springboottest.agency.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboottest.agency.Entity.ItemsUser;
import com.springboottest.agency.Entity.PurchaseUser;
import com.springboottest.agency.Service.InventoryService;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/item/add")
    public ResponseEntity<ItemsUser> addItem(@RequestBody ItemsUser item) {
        return ResponseEntity.ok(inventoryService.addItem(item));
    }

    @GetMapping("/all")
  public ResponseEntity<List<PurchaseUser>> getAllPurchases() {
    return ResponseEntity.ok(inventoryService.getAllPurchases());
  }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        boolean deleted = inventoryService.deleteItem(id);
        return deleted ? ResponseEntity.ok("Item Deleted Successfully")
                : ResponseEntity.status(404).body("Item Not Found");
    }
}

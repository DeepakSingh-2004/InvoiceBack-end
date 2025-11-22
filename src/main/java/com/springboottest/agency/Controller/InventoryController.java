package com.springboottest.agency.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboottest.agency.Dto.PurchaseRequest;
import com.springboottest.agency.Dto.SaleRequest;
import com.springboottest.agency.Entity.ProductUser;
import com.springboottest.agency.Service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {


    @Autowired
    private InventoryService inventoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product")
    public List<ProductUser> getAllProduct() {
    return inventoryService.getAll();
}


    // ✅ Get all products
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }



    // ✅ Add product
   @PostMapping("/add")
    public ResponseEntity<ProductUser> addProduct(@RequestBody ProductUser productUser) {
    ProductUser savedProduct = inventoryService.addProduct(productUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
 }

    @PostMapping("/purchase/{productId}")
     public String purchase(@RequestBody Long productId, @RequestParam int quantity, @RequestParam String vendor){
       inventoryService.purchaseProduct(productId, quantity, vendor);
       return "Purchase recorded successfully!";
  }

@PostMapping("/purchase")
    public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequest request) {
        inventoryService.purchaseProduct(
                request.getProductId(),
                request.getQuantity(),
                request.getVendor()
        );
        return ResponseEntity.ok("✅ Product purchased successfully!");
    }

@PostMapping("/sale/{id}")
   public String sale(@PathVariable Long id, 
                      @RequestBody SaleRequest saleRequest ) {

            inventoryService.saleProduct(id, saleRequest.getQuantity(), saleRequest.getProductPrice());
            return "✅Sale recorded successfully!";
   }
    @PutMapping("/update-price/{id}")
    public ResponseEntity<String> updateProductPrice(
            @PathVariable Long id,
            @RequestParam double newPrice) {

        inventoryService.updateAllProductPrice(id, newPrice);
        return ResponseEntity.ok("Product price updated successfully!");
    }

}


    


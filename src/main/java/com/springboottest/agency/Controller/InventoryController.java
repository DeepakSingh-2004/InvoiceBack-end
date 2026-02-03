package com.springboottest.agency.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
                                   
import com.springboottest.agency.Dto.ProductUpdateRequest;
import com.springboottest.agency.Dto.PurchaseRequest;
import com.springboottest.agency.Dto.SaleRequest;
import com.springboottest.agency.Entity.ProductUser;
import com.springboottest.agency.Entity.StockManagement;
import com.springboottest.agency.Entity.VendorUser;
import com.springboottest.agency.Entity.ItemsUser;
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
    // @GetMapping("/products")
    // public ResponseEntity<?> getAllProducts(){
    //     return ResponseEntity.ok(inventoryService.getAllProducts());
    // }

    @GetMapping("/products")
public ResponseEntity<List<ProductUser>> getAllProducts() {
    return ResponseEntity.ok(inventoryService.getAllProducts());
}




    // ✅ Add product
   @PostMapping("/add")
    public ResponseEntity<ProductUser> addProduct(@RequestBody ProductUser productUser) {
    ProductUser savedProduct = inventoryService.addProduct(productUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
 }

 // ====================== PURCHASE API ======================

//     @PostMapping("/purchase/{productId}")
//      public String purchase(@RequestBody Long productId, @RequestParam int quantity, @RequestParam String vendor){
//        inventoryService.purchaseProduct(productId, quantity, vendor);
//        return "Purchase recorded successfully!";
//   }


//     @PostMapping("/purchase")
//     public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequest request) {
//         inventoryService.purchaseProduct(
//                 request.getProductId(),
//                 request.getQuantity(),
//                 request.getVendor()
//         );
//         return ResponseEntity.ok("✅ Product purchased successfully!");
//     }

    // ====================== SALE API ======================

//     @PostMapping("/sale/{id}")
//    public String sale(@PathVariable Long id, 
//                       @RequestBody SaleRequest saleRequest ) {

//             inventoryService.saleProduct(id, saleRequest.getQuantity(), saleRequest.getProductPrice());
//             return "✅Sale recorded successfully!";
//    }

   // ====================== UPDATE API ======================

    @PutMapping("/product/{id}/update")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
    ) {
        inventoryService.updateProduct(id, request);
        return ResponseEntity.ok("Product updated successfully!");
 
  }
  // ====================== DELETE API ======================

    @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteById(@PathVariable Long id){
    boolean deleted = inventoryService.deleteById(id);

    if (deleted) {
        return ResponseEntity.status(201).body("PRODUCT DELETED SUCCESSFULLY");
    }
    else{
        return ResponseEntity.status(404).body("PRODUCT NOT FOUND");
    }
  }


  // ====================== ITEM API ======================

    // @PostMapping("/item/add")
    // public ResponseEntity<ItemsUser> addItem(@RequestBody ItemsUser item) {
    //     return ResponseEntity.ok(inventoryService.addItem(item));
    // }

    // @GetMapping("/item/all")
    // public ResponseEntity<List<ItemsUser>> getAllItem() {
    //     return ResponseEntity.ok(inventoryService.getAllItem());
    // }

    // @DeleteMapping("/item/{id}")
    // public ResponseEntity<String> deleteItem(@PathVariable Long id) {
    //     boolean deleted = inventoryService.deleteItem(id);
    //     return deleted ? ResponseEntity.ok("Item Deleted Successfully")
    //             : ResponseEntity.status(404).body("Item Not Found");
    // }

    // // ====================== STOCK MANAGEMENT API ======================

    // @PostMapping("/stock/add/{itemId}")
    // public ResponseEntity<?> addStock(@PathVariable Long vendorId,
    //                                   @RequestBody StockManagement stock) {

    //     return ResponseEntity.ok(inventoryService.addStock(vendorId, stock));
    // }

    // @GetMapping("/stock/all")
    // public ResponseEntity<List<StockManagement>> getAllStock() {
    //     return ResponseEntity.ok(inventoryService.getAllStock());
    // }

    // @PutMapping("/stock/update/{id}")
    // public ResponseEntity<?> updateStock(@PathVariable Long id,
    //                                      @RequestBody StockManagement updated) {

    //     return ResponseEntity.ok(inventoryService.updateStock(id, updated));
    // }

    // @DeleteMapping("/stock/{id}")
    // public ResponseEntity<String> deleteStock(@PathVariable Long id) {
    //     boolean deleted = inventoryService.deleteStock(id);

    //     return deleted ? ResponseEntity.ok("Stock Deleted Successfully")
    //                    : ResponseEntity.status(404).body("Stock Not Found");
    // }



    // // ====================== VENDOR API ======================

    // @PostMapping("/vendor/add")
    // public ResponseEntity<VendorUser> addVendor(@RequestBody VendorUser vendor) {
    //     return ResponseEntity.ok(inventoryService.addVendor(vendor));
    // }

    // @GetMapping("/vendor/all")
    // public ResponseEntity<List<VendorUser>> getAllVendors() {
    //     return ResponseEntity.ok(inventoryService.getAllVendors());
    // }

    // @GetMapping("/vendor/{id}")
    // public ResponseEntity<?> getVendorById(@PathVariable Long id) {
    //     VendorUser vendor = inventoryService.getVendorById(id);
    //     return vendor != null ? ResponseEntity.ok(vendor)
    //             : ResponseEntity.status(404).body("Vendor Not Found");
    // }

    // @PutMapping("/vendor/update/{id}")
    // public ResponseEntity<?> updateVendor(
    //         @PathVariable Long id,
    //         @RequestBody VendorUser vendor
    // ) {
    //     VendorUser updated = inventoryService.updateVendor(id, vendor);

    //     return updated != null ?
    //             ResponseEntity.ok(updated) :
    //             ResponseEntity.status(404).body("Vendor Not Found");
    // }

    // @DeleteMapping("/vendor/delete/{id}")
    // public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
    //     boolean deleted = inventoryService.deleteVendor(id);

    //     return deleted
    //             ? ResponseEntity.ok("Vendor Deleted Successfully")
    //             : ResponseEntity.status(404).body("Vendor Not Found");
    // }
}







    


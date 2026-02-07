package com.springboottest.agency.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.springboottest.agency.Repository.PurchaseRepo;
import com.springboottest.agency.Repository.SaleRepo;
import com.springboottest.agency.Repository.StockRepo;
import com.springboottest.agency.Repository.VendorRepo;
import com.springboottest.agency.Repository.ItemsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springboottest.agency.Dto.ProductUpdateRequest;
import com.springboottest.agency.Entity.ProductUser;
import com.springboottest.agency.Entity.PurchaseUser;
import com.springboottest.agency.Entity.SaleUser;
import com.springboottest.agency.Entity.StockManagement;
import com.springboottest.agency.Entity.VendorUser;
import com.springboottest.agency.Entity.ItemsUser;
import com.springboottest.agency.Repository.ProductRepo;

@Service
public class InventoryService {


    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private PurchaseRepo purchaseRepo;

    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private ItemsRepo itemsRepo;

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private VendorRepo vendorRepo;



    
    public List<ProductUser> getAll() {
    return productRepo.findAll();
}


    // ✅ Get all products
    // public ResponseEntity<List<ProductUser>> getAllProducts() {
    //     List<ProductUser> products = productRepo.findAll();
    //     return ResponseEntity.ok(products);
    // }

    public List<ProductUser> getAllProducts() {
    return productRepo.findAll();
}


      // ✅ Add a product
    public ProductUser addProduct(ProductUser productUser) {

        // Recalculate total price
        double total = productUser.getProductPrice() * productUser.getProductStock();
        productUser.setTotalPrice(total);
        return productRepo.save(productUser);
    }

    // ✅ Purchase product
    public void purchaseProduct(Long productId, int quantity, String vendor){
        ProductUser product = productRepo.findById(productId).orElseThrow();

         // update stock
        product.setProductStock(product.getProductStock() + quantity);
        productRepo.save(product);      

       // create purchase entry 
    PurchaseUser purchaseUser = new PurchaseUser();
    purchaseUser.setVendor(vendor);
    purchaseUser.setQuantity(quantity);
    purchaseUser.setDate(LocalDate.now());
    purchaseUser.setProduct(product);


    purchaseRepo.save(purchaseUser);
    }

    // ✅ Sell Product
    public void saleProduct(Long productId, int quantity, double sellingPrice){
        ProductUser product = productRepo.findById(productId).orElseThrow();
        if (product.getProductStock() >= quantity) {
            product.setProductStock(product.getProductStock() - quantity);
            productRepo.save(product);

            SaleUser sale = new SaleUser (quantity, LocalDate.now(), sellingPrice, product);
            saleRepo.save(sale);
        }
    }   
    // // ✅ Update a product-Price
    // public void updateAllProductPrice(Long id, double newPrice, Integer newQuantity) {
    //     ProductUser product = productRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Product not found"));

    //     // update price
    //     if(newPrice!=0.00)
    //     {
    //         product.setProductPrice(newPrice);
    //     }

    //     if(newQuantity!=0.00)
    //     {
    //         product.setQuantity(newQuantity);
    //     }



    //     // Recalculate total price
    //     double total = product.getProductPrice() * product.getProductStock();
    //     product.setTotalPrice(total);


    //     //save update product
    //     productRepo.save(product);
    // }


    // ✅ Update a product-Price
   public void updateProduct(Long id, ProductUpdateRequest request) {

    ProductUser product = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // Update only non-null fields
    if (request.getProductName() != null) {
        product.setProductName(request.getProductName());
    }

    if (request.getProductCode() != null) {
        product.setProductCode(request.getProductCode());
    }

    if (request.getProductPrice() != null) {
        product.setProductPrice(request.getProductPrice());
    }

    if (request.getProductStock() != null) {
        product.setProductStock(request.getProductStock());
    }
    if (request.getQuantity() != null) {
        product.setQuantity(request.getQuantity());
    }

    if (request.getUnit() != null) {
        product.setUnit(request.getUnit());
    }

    // Recalculate total price on every update
    double total = product.getProductPrice() * product.getProductStock();
    product.setTotalPrice(total);

    productRepo.save(product);
}

// ✅ DELETE BY ID
public boolean deleteById(Long id){
    if (productRepo.existsById(id)) {
        productRepo.deleteById(id);
        return true;
    }else{
        return false;
    }
}

// ====================== ITEM SERVICE ======================

    public ItemsUser addItem(ItemsUser vendor) {

        // Auto-calc total price
        double total = vendor.getQuantity() * vendor.getUnitPrice();
        total = total + (total * vendor.getTax() / 100);
        vendor.setTotal_price( "₹" +total);

        return itemsRepo.save(vendor);
    }


    public List<ItemsUser> getAllItem() {
        return itemsRepo.findAll();
    }


    public boolean deleteItem(Long id) {
        Optional<ItemsUser> vendor = itemsRepo.findById(id);

        if (vendor.isPresent()) {
            vendorRepo.deleteById(id);
            return true;
        }
        return false;
    }


    // ====================== ITEM SERVICE ======================


        public List<PurchaseUser> getAllPurchases() {
  return purchaseRepo.findAll();
}

    // ====================== SALE SERVICE ======================

    public List<SaleUser> getAllSale() {
        return  saleRepo.findAll();

    }



    // ====================== STOCK SERVICE ======================

    public StockManagement addStock(Long vendorId, StockManagement stock) {

        ItemsUser vendor = itemsRepo.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // openingStock will be the previous closing stock OR user-defined
        int closing = stock.getOpeningStock()
                     + stock.getPurchaseQuantity()
                     - stock.getSoldQuantity();

        stock.setClosingStock(closing);

        return stockRepo.save(stock);
    }


    public List<StockManagement> getAllStock() {
        return stockRepo.findAll();
    }


    public StockManagement updateStock(Long id, StockManagement updated) {

        StockManagement existing = stockRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        existing.setOpeningStock(updated.getOpeningStock());
        existing.setPurchaseQuantity(updated.getPurchaseQuantity());
        existing.setSoldQuantity(updated.getSoldQuantity());

        int newClosing = updated.getOpeningStock()
                        + updated.getPurchaseQuantity()
                        - updated.getSoldQuantity();

        existing.setClosingStock(newClosing);

        return stockRepo.save(existing);
    }


    public boolean deleteStock(Long id) {
        Optional<StockManagement> stock = stockRepo.findById(id);

        if (stock.isPresent()) {
            stockRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // ====================== VENDOR SERVICE ======================


    public VendorUser addVendor(VendorUser vendor) {
        return vendorRepo.save(vendor);
    }

    public List<VendorUser> getAllVendors() {
        return vendorRepo.findAll();
    }

    public VendorUser updateVendor(Long id, VendorUser updatedVendor) {

    return vendorRepo.findById(id)
            .map(vendor -> {

                if (updatedVendor.getVendorName() != null) {
                    vendor.setVendorName(updatedVendor.getVendorName());
                }

                if (updatedVendor.getAddress() != null) {
                    vendor.setAddress(updatedVendor.getAddress());
                }

                if (updatedVendor.getGstin_pan() != null) {
                    vendor.setGstin_pan(updatedVendor.getGstin_pan());
                }

                if (updatedVendor.getContactNumber_email() != null) {
                    vendor.setContactNumber_email(updatedVendor.getContactNumber_email());
                }

                if (updatedVendor.getBankDetails() != null) {
                    vendor.setBankDetails(updatedVendor.getBankDetails());
                }

                if (updatedVendor.getDefaultPaymentTerms() != null) {
                    vendor.setDefaultPaymentTerms(updatedVendor.getDefaultPaymentTerms());
                }

                return vendorRepo.save(vendor);
            })
            .orElse(null);
}


    public boolean deleteVendor(Long id) {
        if (vendorRepo.existsById(id)) {
            vendorRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public VendorUser getVendorById(Long id) {
        return vendorRepo.findById(id).orElse(null);
    }
}

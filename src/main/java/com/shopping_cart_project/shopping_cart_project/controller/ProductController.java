package com.shopping_cart_project.shopping_cart_project.controller;

import com.shopping_cart_project.shopping_cart_project.entity.Product;
import com.shopping_cart_project.shopping_cart_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product createdProduct = productService.addProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws Exception {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }


    //根據條件篩選並分頁商品
    @GetMapping("/")
    //value用來定位URL中的參數，required = false代表可以不填，required = true是強制要填資料
    public ResponseEntity<Page<Product>> findProductByFilter(@RequestParam(value = "category", required = false) String category,
                                                             @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                                             @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                                             @RequestParam(value = "sort", required = false) String sort,
                                                             @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Page<Product> filteredProductsPage = productService.getProductsByFilter(category, minPrice, maxPrice, sort, pageNumber, pageSize);
        return new ResponseEntity<>(filteredProductsPage, HttpStatus.OK);
    }

    @PostMapping("/many")
    public ResponseEntity<List<Product>> addProducts(@RequestBody Product[] products) {
        List<Product> createdProducts = new ArrayList<>();
        for(Product product : products) {
            Product p = productService.addProduct(product);
            createdProducts.add(p);
        }
        return new ResponseEntity<>(createdProducts, HttpStatus.CREATED);
    }

}

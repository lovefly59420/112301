package com.shopping_cart_project.shopping_cart_project.service;

import com.shopping_cart_project.shopping_cart_project.entity.Product;
import com.shopping_cart_project.shopping_cart_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public String deleteProduct(Long id){
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

    public Product getProductById(Long id) throws Exception{
        Optional<Product> opt = productRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new Exception("Product not found");
    }

    public Page<Product> getProductsByFilter(String category, Integer minPrice, Integer maxPrice,
                                             String sort, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.findProductsByFilter(category, minPrice, maxPrice, sort);
        // 數值等於pageNumber*pageSize
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min((startIndex + pageable.getPageSize()), products.size());
        List<Product> pageContent = products.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, products.size());
    }
}

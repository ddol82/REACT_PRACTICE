package com.ddol.jwtd9.product.controller;

import com.ddol.jwtd9.common.ResponseDTO;
import com.ddol.jwtd9.product.model.dto.ProductDTO;
import com.ddol.jwtd9.product.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ResponseDTO> selectAllProduct(@RequestParam(name = "offset", defaultValue = "1") int currPage) {
        int productCount = productService.countAllProduct();
        int itemsPerPage = 10;
        int pageAmount = (int) Math.ceil(productCount/itemsPerPage);
        int minIndex = itemsPerPage * (currPage-1); //0, 10, 20...
        int maxIndex = Math.min(productCount, itemsPerPage * currPage - 1); //9, 19, 29...
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", null));
    }

    @PostMapping("/products")
    public ResponseEntity<ResponseDTO> insertProduct(@ModelAttribute ProductDTO product) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "등록 성공", productService.insertProduct(product)));
    }
}

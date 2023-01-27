package com.ddol.jwtd9.product.controller;

import com.ddol.jwtd9.common.ResponseDTO;
import com.ddol.jwtd9.product.model.dto.ProductDTO;
import com.ddol.jwtd9.product.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ResponseDTO> selectAllProduct(@RequestParam(name = "offset", defaultValue = "1") int currPage) {
        int itemsPerPage = 10;

        int productCount = productService.countAllProduct();
        int minIndex = itemsPerPage * (currPage-1) + 1; //1, 11, 21...
        int maxIndex = Math.min(productCount, itemsPerPage * currPage); //10, 20, 30...
        int pageAmount = (int) Math.ceil((double)productCount/itemsPerPage);
        if(currPage < 1 || currPage > pageAmount) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.NO_CONTENT, "존재하지 않는 페이지", null));
        }

        Map<String, String> idxMap = new HashMap<>();
        idxMap.put("minIndex", minIndex+"");
        idxMap.put("maxIndex", maxIndex+"");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", productService.selectAllRangedProductList(idxMap)));
    }

    @PostMapping("/management")
    public ResponseEntity<ResponseDTO> insertProduct(@ModelAttribute ProductDTO product) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "등록 성공", productService.insertProduct(product)));
    }

    @PutMapping("/management")
    public ResponseEntity<ResponseDTO> updateProduct(@ModelAttribute ProductDTO product) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "수정 성공", productService.updateProduct(product)));
    }
}

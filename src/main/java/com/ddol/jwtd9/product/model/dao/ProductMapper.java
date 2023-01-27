package com.ddol.jwtd9.product.model.dao;

import com.ddol.jwtd9.product.model.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    ProductDTO selectProduct(String productCode);
    int countAllProduct();

    int insertProduct(ProductDTO product);

    List<ProductDTO> selectAllRangedProductList(Map<String, String> idxMap);

    int updateProduct(ProductDTO product);
}

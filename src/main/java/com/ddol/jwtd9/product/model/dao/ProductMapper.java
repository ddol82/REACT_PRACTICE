package com.ddol.jwtd9.product.model.dao;

import com.ddol.jwtd9.product.model.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    int countAllProduct();

    int insertProduct(ProductDTO product);
}

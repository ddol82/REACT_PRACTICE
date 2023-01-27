package com.ddol.jwtd9.product.model.service;

import com.ddol.jwtd9.product.model.dao.ProductMapper;
import com.ddol.jwtd9.product.model.dto.ProductDTO;
import com.ddol.jwtd9.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {
    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${image.image-url}")
    private String IMAGE_URL;
    private final ProductMapper productMapper;

    public int countAllProduct() {
        return productMapper.countAllProduct();
    }

    public List<ProductDTO> selectAllRangedProductList(Map<String, String> idxMap) {
        return productMapper.selectAllRangedProductList(idxMap);
    }

    @Transactional
    public int insertProduct(ProductDTO product) {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;

        int result = 0;
        try{
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, product.getProductImage());
            product.setProductImageUrl(replaceFileName);
            System.out.println(product);
            result = productMapper.insertProduct(product);

        } catch (Exception e) {
            try {
                FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return result;
    }

    @Transactional
    public int updateProduct(ProductDTO product) {
        String replaceFileName = null;
        int result = 0;
        try{
            String originalImage = productMapper.selectProduct(product.getProductCode()).getProductImageUrl();
            if(product.getProductImage() != null) {
                String imgName = UUID.randomUUID().toString().replace("-", "");
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imgName, product.getProductImage());

                FileUploadUtils.deleteFile(IMAGE_DIR, originalImage);
            }
            product.setProductImageUrl(replaceFileName);
            result = productMapper.updateProduct(product);
        } catch (IOException e) {
            try {
                FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return result;
    }
}

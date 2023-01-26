package com.ddol.jwtd9.product.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {
    private String productCode;
    private String categoryCode;
    private String categoryName;
    private String productName;
    private String productPrice;
    private String productDescription;
    private String productOrderable;
    private String productStock;
    private MultipartFile productImage;
    private String productImageUrl;
}

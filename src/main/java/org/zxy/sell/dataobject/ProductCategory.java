package org.zxy.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 类目
 */
@Entity
@Data
@DynamicUpdate
public class ProductCategory {

    // 类目id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    // 类目名
    private String categoryName;

    // 类目类型
    private Integer categoryType;
}

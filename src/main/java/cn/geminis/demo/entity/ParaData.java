package cn.geminis.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

/**
 * 参数数据
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ParaData {
    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 数据名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 数据描述
     */
    @Column(nullable = true)
    private String description="参数当前还没有任何描述";

    /**
     * 接口地址
     */
    @Column(nullable = false)
    private String accessUrl;

    /**
     * 数据来源
     */
    @Column(nullable = false)
    private String dataSource;

    /**
     * 请求内容
     */
    @Column(nullable = true)
    private String requestContent;

    /**
     * 定义与评价指标的多对多关系
     */
    @ManyToMany(mappedBy = "paraDatas",fetch = FetchType.LAZY)
    private List<EvalIndicator> evalIndicators = new ArrayList<>();

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
    }
}

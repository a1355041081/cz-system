package cn.geminis.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import javax.persistence.*;
import java.util.*;

/**
 * 评价指标
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EvalIndicator {

    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 指标名称
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 指标单位
     */
    @Column(nullable = false)
    private String unit;

    /**
     * 所属一级指标
     */
    @Column(nullable = false)
    private String subord;

    /**
     * 描述
     */
    @Column(nullable = true)
    private String description="指标当前还没有任何描述";

    /**
     * 适用方法
     */
    @Column(nullable = false)
    private String suitMethod;

    /**
     * 权重
     */
    @Column(nullable = true)
    private int weight=-1;

    /**
     * 计算方法
     */
    @Column(nullable = false)
    private String calMethod;

    /**
     * 数据来源
     */
    @Column(nullable = false)
    private String dataSource;

    /**
     * 定义与评价对象类别的多对多关系
     */
    @ManyToMany(mappedBy = "evalIndicatorsByCategory", fetch = FetchType.LAZY)
    private List<EvalObjectCategory> evalObjectCategories = new ArrayList<>();

    /**
     * 定义与物资的多对多关系
     */
    @ManyToMany(mappedBy = "evalIndicatorsByMaterial", fetch = FetchType.LAZY)
    private List<Material> materials = new ArrayList<>();

    /**
     * 定义与参数数据的多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evalIndicatorAndParaData", inverseJoinColumns = @JoinColumn(name = "paraDataId")
            ,joinColumns = @JoinColumn(name = "evalIndicatorId"))
    private List<ParaData> paraDatas = new ArrayList<>();

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
    }
}

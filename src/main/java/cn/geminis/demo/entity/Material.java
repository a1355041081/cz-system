package cn.geminis.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

/**
 * 物资
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Material {

    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 物资名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 物资类别
     */
    @Column(nullable = false)
    private String category;

    /**
     * 定义与评价指标的多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "materialAndEvalIndicator", inverseJoinColumns = @JoinColumn(name = "evalIndicatorId")
            ,joinColumns = @JoinColumn(name = "materialId"))
    private List<EvalIndicator> evalIndicatorsByMaterial = new ArrayList<>();

    /**
     * 定义与评价对象的多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "materialAndEvalObject", inverseJoinColumns = @JoinColumn(name = "evalObjectId")
            ,joinColumns = @JoinColumn(name = "materialId"))
    private List<EvalObject> evalObjectsByMaterial = new ArrayList<>();

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
    }
}

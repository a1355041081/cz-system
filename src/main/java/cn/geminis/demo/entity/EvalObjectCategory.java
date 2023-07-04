package cn.geminis.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

/**
 * 评价对象类别
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EvalObjectCategory {

    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 类别名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 定义与评价指标的多对多关系
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evalObjectCategoryAndEvalIndicator", inverseJoinColumns = @JoinColumn(name = "evalIndicatorId")
            ,joinColumns=@JoinColumn(name = "evalObjectCategoryId"))
    private List<EvalIndicator> evalIndicatorsByCategory = new ArrayList<>();

    /**
     * 定义与评价对象的一对多关系
     */
    @OneToMany(mappedBy = "evalObjectCategory", fetch = FetchType.LAZY)
    private List<EvalObject> evalObjects = new ArrayList<>();

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
    }
}

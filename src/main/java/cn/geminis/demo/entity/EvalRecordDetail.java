package cn.geminis.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EvalRecordDetail {

    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 类型
     */
    @Column(nullable = false)
    private String type;

    /**
     * 名称
     */
    @Column(nullable = false, scale = 4)
    @Digits(integer = 12,fraction = 4)
    private BigDecimal value;

    /**
     * 时间：需要根据评价周期和评价时间范围计算得出
     */
    @Column(nullable = false)
    private String time;

    /**
     * 定义与评价记录的多对一关系
     */
    @ManyToOne
    @JoinColumn(name = "evalRecord")
    private EvalRecord evalRecord;

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
    }
}

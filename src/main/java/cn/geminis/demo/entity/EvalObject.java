package cn.geminis.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

/**
 * 评价对象
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EvalObject {

    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 评价对象名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 联系人
     */
    @Column(nullable = false)
    private String contact;

    /**
     * 联系电话
     */
    @Column(nullable = false)
    private String phone;

    /**
     * 定义评价对象与评价对象类别的一对多关系
     * 定义级联关系为： 创建新评价对象时，必须存在相应的评价对象类别
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalObjectCategory")
    private EvalObjectCategory evalObjectCategory;

    /**
     * 定义与评价记录的多对一关系
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalRecord")
    private EvalRecord evalRecord;

    /**
     * 定义评价对象与物资的多对多关系
     * 定义级联关系为： 创建新评价对象时，必须存在相应的物资
     */
    @ManyToMany(mappedBy = "evalObjectsByMaterial", fetch = FetchType.LAZY)
    private List<Material> materials = new ArrayList<>();

    /**
     * 定义与评价任务的多对多关系
     */
    @ManyToMany(mappedBy = "evalObjectsByEvalTask",fetch = FetchType.LAZY)
    private List<EvalTask> evalTasks = new ArrayList<>();

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
    }
}

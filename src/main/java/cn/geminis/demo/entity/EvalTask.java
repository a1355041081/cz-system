package cn.geminis.demo.entity;

import cn.geminis.demo.util.DateUtils;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 评价实体
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EvalTask {

    /**
     * 编号
     */
    @Id
    @Column(length = 36)
    private String id;

    /**
     * 评价名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public String getCreateTime() {
        return DateUtils.formatDate(createTime);
    }

    /**
     * 评价实体类型
     */
    @Column(nullable = false)
    private String type;

    /**
     * 定义与评价对象的多对多关系
     * 级联操作为：创建评价实体时必须有相应的评价对象
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evalTaskAndEvalObject", inverseJoinColumns = @JoinColumn(name = "evalObjectId")
    ,joinColumns = @JoinColumn(name = "evalTaskId"))
    private List<EvalObject> evalObjectsByEvalTask;

    /**
     * 定义与评价记录的一对多关系
     */
    @OneToMany(mappedBy = "evalTask", fetch = FetchType.LAZY)
    private List<EvalRecord> evalRecords;

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
        if(Objects.isNull(createTime)){
            createTime = new Date(System.currentTimeMillis());
        }
    }
}

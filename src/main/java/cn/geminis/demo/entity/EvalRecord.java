package cn.geminis.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

/**
 * 评价记录
 */
@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class EvalRecord {

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
     * 评价周期
     */
    @Column(nullable = false)
    private String cycle;


    /**
     * 记录时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordTime;

    /**
     * 评价方法
     */
    @Column(nullable = false)
    private String method;

    /**
     * 操作人员
     */
    @Column(nullable = false)
    private String operator;

    /**
     * 评价开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startTime;

    /**
     * 评价结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endTime;

    /**
     * 定义与评价记录详情一对多的关系
     */
    @OneToMany(mappedBy = "evalRecord", fetch = FetchType.LAZY)
    private List<EvalRecordDetail> evalRecordDetails = new ArrayList<>();

    /**
     * 与评价任务的多对一关系
     */
    @ManyToOne
    @JoinColumn(name = "evalTaskId")
    private EvalTask evalTask;


    @OneToOne(mappedBy = "evalHistoryRecord")
    private EvalRecord evalRecord;

    /**
     * 历史（上一步）评价结果id
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "evalHistoryRecord")
    private EvalRecord evalHistoryRecord;

    @PrePersist
    void setId() {
        if (Objects.isNull(id)) {
            id = UUID.randomUUID().toString();
        }
        if(Objects.isNull(recordTime)){
            recordTime = new Date(System.currentTimeMillis());
        }
    }
}

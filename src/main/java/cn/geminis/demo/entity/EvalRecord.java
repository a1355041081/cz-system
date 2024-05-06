package cn.geminis.demo.entity;

import cn.geminis.demo.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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

    @JsonIgnoreProperties({"evalRecord"})
    @OneToMany(mappedBy = "evalRecord", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EvalRecordDetail> evalRecordDetails = new ArrayList<>();

    /**
     * 与评价任务的多对一关系
     */
    @ManyToOne
    @JoinColumn(name = "evalObjectCategoryId")
    private EvalObjectCategory evalObjectCategory;

    @ElementCollection
    private List<String> objectIds = new ArrayList<>();

    /**
     * 与评价任务的多对一关系
     */
    @ManyToOne
    @JoinColumn(name = "evalTaskId")
    private EvalTask evalTask;

    @JsonIgnoreProperties({"evalHistoryRecord"})
    @OneToMany(mappedBy = "evalHistoryRecord",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<EvalRecord> childRecords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evalHistoryRecord")
    private EvalRecord evalHistoryRecord;



    public String getRecordTime() {
        return DateUtils.formatDate(recordTime);
    }

    public String getStartTime() {
        return DateUtils.formatDate(startTime);
    }

    public String getEndTime() {
        return DateUtils.formatDate(endTime);
    }

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

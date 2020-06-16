package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.enums.PostReportType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_post_reports")
public class PostReport extends DomainObject {
    @Id
    @Column(name = "post_report_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_report_seq")
    @SequenceGenerator(name = "post_report_seq", sequenceName = "post_report_seq", allocationSize = 1)
    private Long postReportId;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "report_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostReportType reportCategory;

    @Column(name = "report_text")
    private String reportText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    public Long getPostReportId() {
        return postReportId;
    }

    public void setPostReportId(Long postReportId) {
        this.postReportId = postReportId;
    }

    public PostReportType getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(PostReportType reportCategory) {
        this.reportCategory = reportCategory;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    @JsonIgnore
    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    @JsonIgnore
    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }
}

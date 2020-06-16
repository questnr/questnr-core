package com.questnr.requests;

import com.questnr.common.enums.PostReportType;

public class PostReportRequest {
    private PostReportType reportCategory;
    private String reportText;

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
}

package com.mind.loginregisterapps.Model;

public class OfficerSendReportModel {
    private String uId, report, reporterPhone, reportTitle, officerName;
    private long date;


    public OfficerSendReportModel() {
    }

    public OfficerSendReportModel(String uId, String report, String reporterPhone,
                                  String reportTitle, String officerName, long date) {
        this.uId = uId;
        this.report = report;
        this.reporterPhone = reporterPhone;
        this.reportTitle = reportTitle;
        this.officerName = officerName;
        this.date = date;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

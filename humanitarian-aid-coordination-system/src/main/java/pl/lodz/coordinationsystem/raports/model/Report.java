package pl.lodz.coordinationsystem.raports.model;

import java.util.List;

public class Report <T>{
    private Long id;
    private ReportType ReportType;
    private ReportFileType ReportFileType;
    private List<T> ReportData;
    private Long userId;

    public Report() {}

    public Report(Long id, ReportType reportType, ReportFileType reportFileType, List<T> reportData, Long userId) {
        this.id = id;
        ReportType = reportType;
        ReportFileType = reportFileType;
        ReportData = reportData;
        this.userId = userId;
    }

    public Report(ReportFileType reportFileType, ReportType reportType, Long userId) {
        ReportFileType = reportFileType;
        ReportType = reportType;
        this.userId = userId;
    }

    //TODO Create minimal version of Report constructor

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public pl.lodz.coordinationsystem.raports.model.ReportType getReportType() {
        return ReportType;
    }

    public void setReportType(pl.lodz.coordinationsystem.raports.model.ReportType reportType) {
        ReportType = reportType;
    }

    public pl.lodz.coordinationsystem.raports.model.ReportFileType getReportFileType() {
        return ReportFileType;
    }

    public void setReportFileType(pl.lodz.coordinationsystem.raports.model.ReportFileType reportFileType) {
        ReportFileType = reportFileType;
    }

    public List<T> getReportData() {
        return ReportData;
    }

    public void setReportData(List<T> reportData) {
        ReportData = reportData;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

}

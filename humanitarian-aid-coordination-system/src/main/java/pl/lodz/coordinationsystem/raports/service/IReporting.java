package pl.lodz.coordinationsystem.raports.service;

import pl.lodz.coordinationsystem.raports.model.Report;

import java.time.LocalDateTime;

public interface IReporting {
    Report generateReportVolunteer(Long userId);
    Report generateReportDonations(LocalDateTime startDate, LocalDateTime EndDate, Long userId);
    Report generateReportResources(Long userId);
    Report generateReportNotifications(LocalDateTime startDate, LocalDateTime endDate, Long userId);
    Report generateReportForDonor(Long userId);
}

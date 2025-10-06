package com.example.patterns.structural.proxy;

class RealReportingService implements ReportingService {
    @Override
    public String generateReport(String user) {
        return "Report for " + user;
    }
}

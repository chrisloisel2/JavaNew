package com.example.patterns.structural.proxy;

public class SecurityProxy implements ReportingService {

    private final ReportingService delegate = new RealReportingService();

    @Override
    public String generateReport(String user) {
        if (!user.startsWith("admin")) {
            return "Accès refusé";
        }
        return delegate.generateReport(user);
    }
}

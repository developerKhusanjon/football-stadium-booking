package org.stadium.adminapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.stadium.adminapi.mapper.AdminSessionMapper;
import org.stadium.adminapi.service.AdminSessionService;
import org.stadium.adminapi.service.dto.AdminSessionDto;
import org.stadium.corelib.domain.Admin;
import org.stadium.corelib.domain.AdminSession;
import org.stadium.corelib.repo.admin.AdminRepository;
import org.stadium.corelib.repo.admin.AdminSessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class AdminSessionServiceImpl implements AdminSessionService {

    private final AdminSessionMapper adminSessionMapper;
    private final AdminSessionRepository adminSessionRepository;
    private final AdminRepository adminRepository;

    public AdminSessionServiceImpl(AdminSessionMapper adminSessionMapper, AdminSessionRepository adminSessionRepository, AdminRepository adminRepository) {
        this.adminSessionMapper = adminSessionMapper;
        this.adminSessionRepository = adminSessionRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Page<AdminSessionDto> findAll(Pageable pageable, Admin admin_id) {
        Page<AdminSessionDto> adminSessions = adminSessionRepository.findAllByAdminId(admin_id.getId(), pageable).map(adminSessionMapper::toDto);
        return adminSessions;
    }

    @Override
    public void save(HttpServletRequest request, Admin admin, String token) {
        AdminSession adminSession = new AdminSession();
        adminSession.setAdmin(admin);
        adminSession.setToken(token);
        adminSession.setDeviceIp(getClientIpAddress(request));
        adminSession.setDeviceModel(getDeviceModel(request));
        adminSession.setDeviceOsVersion(getDeviceOsVersion(request));
        adminSessionRepository.save(adminSession);
    }

    private String getDeviceModel(HttpServletRequest request) {
        return request.getHeader("device-model");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String LOCALHOST_IPV4 = "127.0.0.1";
        String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!StringUtils.isEmpty(ipAddress)
                && ipAddress.length() > 15
                && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }

    private String getDeviceOsVersion(HttpServletRequest request) {
        return request.getHeader("device-os");
    }
}

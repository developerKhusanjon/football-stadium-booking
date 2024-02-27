package org.stadium.adminapi.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.stadium.corelib.domain.Admin;
import org.stadium.corelib.domain.Role;
import org.stadium.corelib.domain.enums.RoleName;
import org.stadium.corelib.repo.admin.AdminRepository;
import org.stadium.corelib.repo.admin.RoleRepository;

import java.util.HashSet;


@Component
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String modeInitial;

    public DataLoader(AdminRepository adminRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(String... args) {

        if(modeInitial.equals("create")) {
            Role.RoleBuilder roleBuilder = Role.builder().roleName(RoleName.ADMIN);
            roleRepository.save(roleBuilder.build());
            roleBuilder = Role.builder().roleName(RoleName.FIELD_MANAGER);
            roleRepository.save(roleBuilder.build());


            HashSet<Role> roles = new HashSet<>(roleRepository.findAll());

            Admin admins = new Admin();
            admins.setFio("Husanboy");
            admins.setUsername("husan");
            admins.setPassword(passwordEncoder.encode("1234"));
            admins.setPhone("999053244");
            admins.setRoles(roles);
            adminRepository.save(admins);
        }
    }
}

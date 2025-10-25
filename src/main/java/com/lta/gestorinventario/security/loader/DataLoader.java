package com.lta.gestorinventario.security.loader;

import com.lta.gestorinventario.entity.Rol;
import com.lta.gestorinventario.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Autowired
    private RolRepository rolRepository;

    @Bean
    public CommandLineRunner initData(){
        return args -> {
          cargarRolesPorDefecto();
        };
    }

    private void cargarRolesPorDefecto(){
        if(!rolRepository.existsByNombre("ROLE_USER")){
            Rol userRol = new Rol();
            userRol.setNombre("ROLE_USER");
            rolRepository.save(userRol);
        }

        if(!rolRepository.existsByNombre("ROLE_ADMIN")){
            Rol adminRol = new Rol();
            adminRol.setNombre("ROLE_ADMIN");
            rolRepository.save(adminRol);
        }
    }
}

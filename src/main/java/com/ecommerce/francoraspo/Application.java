package com.ecommerce.francoraspo;

import com.ecommerce.francoraspo.models.authJwtModels.RolSeguridad;
import com.ecommerce.francoraspo.models.enums.ERolSeguridad;
import com.ecommerce.francoraspo.repositories.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent> {
	private final Logger logger = LogManager.getLogger(Application.class);
	@Autowired RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		//Si no existen roles los inicializo
		if (roleRepository.count() == 0 ) {
			logger.info("Inicializando repositorios...");
			logger.info("Cargando roles...");
			roleRepository.save( new RolSeguridad(ERolSeguridad.ADMINISTRADOR));
			roleRepository.save( new RolSeguridad(ERolSeguridad.INVITADO));
			roleRepository.save( new RolSeguridad(ERolSeguridad.USUARIO));
		}
	}
}


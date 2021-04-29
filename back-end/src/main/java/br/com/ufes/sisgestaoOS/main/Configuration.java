package br.com.ufes.sisgestaoOS.main;

import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.repository.EquipeRepository;
import br.com.ufes.sisgestaoOS.repository.EquipeRepositoryPersistence;
import br.com.ufes.sisgestaoOS.repository.OrdemRepository;
import br.com.ufes.sisgestaoOS.repository.OrdemRepositoryPersistence;
import br.com.ufes.sisgestaoOS.repository.PrioridadeRepository;
import br.com.ufes.sisgestaoOS.repository.PrioridadeRepositoryPersistence;
import br.com.ufes.sisgestaoOS.repository.RequisitoRepository;
import br.com.ufes.sisgestaoOS.repository.RequisitoRepositoryPersistence;
import br.com.ufes.sisgestaoOS.repository.StatusRepository;
import br.com.ufes.sisgestaoOS.repository.StatusRepositoryPersistence;
import br.com.ufes.sisgestaoOS.repository.UserRepository;
import br.com.ufes.sisgestaoOS.repository.UserRepositoryPersistence;
import br.com.ufes.sisgestaoOS.service.EquipeService;
import br.com.ufes.sisgestaoOS.service.OrdemService;
import br.com.ufes.sisgestaoOS.service.PrioridadeService;
import br.com.ufes.sisgestaoOS.service.RequisitoService;
import br.com.ufes.sisgestaoOS.service.StatusService;
import br.com.ufes.sisgestaoOS.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserRepository USER_REPOSITORY = new UserRepositoryPersistence();
    private static final EquipeRepository EQUIPE_REPOSITORY = new EquipeRepositoryPersistence();
    private static final StatusRepository STATUS_REPOSITORY = new StatusRepositoryPersistence();
    private static final PrioridadeRepository PRIORIDADE_REPOSITORY = new PrioridadeRepositoryPersistence();
    private static final RequisitoRepository REQUISITO_REPOSITORY = new RequisitoRepositoryPersistence();
    private static final OrdemRepository ORDEM_REPOSITORY = new OrdemRepositoryPersistence();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    private static final EquipeService EQUIPE_SERVICE = new EquipeService(EQUIPE_REPOSITORY);
    private static final StatusService STATUS_SERVICE = new StatusService(STATUS_REPOSITORY);
    private static final PrioridadeService PRIORIDADE_SERVICE = new PrioridadeService(PRIORIDADE_REPOSITORY);
    private static final RequisitoService REQUISITO_SERVICE = new RequisitoService(REQUISITO_REPOSITORY);
    private static final OrdemService ORDEM_SERVICE = new OrdemService(ORDEM_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static UserService getUserService() {
        return USER_SERVICE;
    }

    static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    static EquipeRepository getEquipeRepository() {
		return EQUIPE_REPOSITORY;
	}    

	public static StatusRepository getStatusRepository() {
		return STATUS_REPOSITORY;
	}

	public static PrioridadeRepository getPrioridadeRepository() {
		return PRIORIDADE_REPOSITORY;
	}

	public static RequisitoRepository getRequisitoRepository() {
		return REQUISITO_REPOSITORY;
	}

	public static OrdemRepository getOrdemRepository() {
		return ORDEM_REPOSITORY;
	}

	public static EquipeService getEquipeService() {
		return EQUIPE_SERVICE;
	}

	public static StatusService getStatusService() {
		return STATUS_SERVICE;
	}

	public static PrioridadeService getPrioridadeService() {
		return PRIORIDADE_SERVICE;
	}

	public static RequisitoService getRequisitoService() {
		return REQUISITO_SERVICE;
	}

	public static OrdemService getOrdemService() {
		return ORDEM_SERVICE;
	}

	public static GlobalExceptionHandler getGlobalErrorHandler() {
		return GLOBAL_ERROR_HANDLER;
	}

	public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}

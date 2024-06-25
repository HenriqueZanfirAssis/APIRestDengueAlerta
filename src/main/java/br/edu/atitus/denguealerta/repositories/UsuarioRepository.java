package br.edu.atitus.denguealerta.repositories;

import java.util.Optional;
import java.util.UUID;

import br.edu.atitus.denguealerta.entities.UsuarioEntity;

public interface UsuarioRepository extends GenericRepository<UsuarioEntity> {

	boolean existsByEmail(String email);
	
	boolean existsByCpf(String cpf);
	
	boolean existsByEmailAndIdNot(String email, UUID id);
	
	boolean existsByCpfAndIdNot(String cpf, UUID id);
	
	Optional<UsuarioEntity> findByEmail(String email);
}

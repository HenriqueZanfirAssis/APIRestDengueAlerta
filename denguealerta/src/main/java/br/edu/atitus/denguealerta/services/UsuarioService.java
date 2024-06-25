package br.edu.atitus.denguealerta.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.atitus.denguealerta.components.Validador;
import br.edu.atitus.denguealerta.entities.UsuarioEntity;
import br.edu.atitus.denguealerta.repositories.GenericRepository;
import br.edu.atitus.denguealerta.repositories.UsuarioRepository;

@Service
public class UsuarioService extends GenericService<UsuarioEntity> implements UserDetailsService {
	
	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected GenericRepository<UsuarioEntity> getRepository() {
		return this.usuarioRepository;
	}

	@Override
	protected void validate(UsuarioEntity usuario) throws Exception {
		if (usuario.getNome() == null || usuario.getNome().isEmpty())
			throw new Exception("Campo Nome inválido");
		if (usuario.getEmail() == null || usuario.getEmail().isEmpty())
			throw new Exception("Campo Email inválido");
		if (usuario.getCpf() == null || usuario.getCpf().isEmpty())
			throw new Exception("Campo CPF inválido");
		if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty())
			throw new Exception("Campo Endereço inválido");
		if (usuario.getSenha() == null || usuario.getSenha().isEmpty())
			throw new Exception("Campo Senha inválido");
		
		if(!Validador.validaCPF(usuario.getCpf()))
			throw new Exception("CPF inválido");
		if(!Validador.validaEmail(usuario.getEmail()))
			throw new Exception("Email inválido");
		
		if (usuario.getId() == null) {
			if (this.usuarioRepository.existsByCpf(usuario.getCpf()))
				throw new Exception("Já existe usuário cadastrado com este CPF");
			if (this.usuarioRepository.existsByEmail(usuario.getEmail()))
				throw new Exception("Já existe usuário cadastrado com este Email");
		} else {
			if (this.usuarioRepository.existsByCpfAndIdNot(usuario.getCpf(), usuario.getId()))
				throw new Exception("Já existe usuário cadastrado com este CPF");
			if (this.usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), usuario.getId()))
				throw new Exception("Já existe usuário cadastrado com este Email");
		}
		
		String hashSenha = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(hashSenha);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UsuarioEntity usuario = this.usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email"));
		return usuario;
	}

}

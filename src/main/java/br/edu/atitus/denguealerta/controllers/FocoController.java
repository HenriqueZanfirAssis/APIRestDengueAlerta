package br.edu.atitus.denguealerta.controllers;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.denguealerta.dtos.FocoDTO;
import br.edu.atitus.denguealerta.entities.FocoEntity;
import br.edu.atitus.denguealerta.entities.UsuarioEntity;
import br.edu.atitus.denguealerta.services.FocoService;

@RestController
@RequestMapping("/ws/foco")
public class FocoController {
	
	private final FocoService focoService;

	public FocoController(FocoService focoService) {
		super();
		this.focoService = focoService;
	}

	
	@PostMapping
	public ResponseEntity<FocoEntity> postMethod(@RequestBody FocoDTO dto) throws Exception {
		FocoEntity novoFoco = new FocoEntity();
		BeanUtils.copyProperties(dto, novoFoco);
		
		UsuarioEntity usuarioAutenticado = (UsuarioEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		novoFoco.setUsuario(usuarioAutenticado);
		novoFoco.setData(LocalDate.now());
		novoFoco.setVerificado(false);
		
		this.focoService.save(novoFoco);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoFoco);
	}
	
	@GetMapping
	public ResponseEntity<Page<FocoEntity>> getMethod(
			@PageableDefault(sort = "data", direction = Direction.DESC, page = 0, size = 5)
			Pageable pageable) throws Exception {
		var listaFocos = this.focoService.findAll(pageable);
		return ResponseEntity.ok(listaFocos);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionHandler(Exception e) {
		String cleanMessage = e.getMessage().replaceAll("[\\r\\n]", " ");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cleanMessage);
	}
}

package com.appforfiles.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.appforfiles.model.Role;
import com.appforfiles.repository.RoleRepository;
import com.appforfiles.service.RoleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public List<Role> getAll() {
		return roleRepository.findAll();
	}

	@Override
	public Optional<Role> getById(Integer id) {
		return roleRepository.findById(id);
	}

	@Override
	public void saveOrUpdate(Role role) {
		roleRepository.save(role);
	}

	@Override
	public void deleteById(Integer id) {
		roleRepository.deleteById(id);
	}

	@Override
	public void delete(Role role) {
		roleRepository.delete(role);
	}

}

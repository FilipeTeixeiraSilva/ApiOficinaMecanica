package br.com.apioficina.repository;

import br.com.apioficina.model.PecaEntity;
import br.com.apioficina.model.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PecaRepository extends JpaRepository<PecaEntity, Long> {
}

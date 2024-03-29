package com.controleestoquensgio.repositories;

import com.controleestoquensgio.models.TipoAcessoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAcessoRepository extends JpaRepository<TipoAcessoModel, Integer>{
    Page<TipoAcessoModel> findAll(Pageable pageable);
    Page<TipoAcessoModel> findAllByAtivo(Pageable pageable, String ativo);
}

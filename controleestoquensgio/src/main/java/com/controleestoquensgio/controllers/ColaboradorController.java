package com.controleestoquensgio.controllers;

import java.util.Optional;


import javax.validation.Valid;
import com.controleestoquensgio.models.ColaboradorModel;
import com.controleestoquensgio.dtos.ColaboradorDto;
import com.controleestoquensgio.services.ColaboradorService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin (origins = "*", maxAge = 3600)
@RequestMapping(value = {"/controle-estoque/colaborador"})
public class ColaboradorController extends ControllerFather{

    @Autowired
    ColaboradorService colaboradorSvc;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ColaboradorDto colaboradorDto){
        var colaboradorModel = new ColaboradorModel();
        BeanUtils.copyProperties(colaboradorDto, colaboradorModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorSvc.save(colaboradorModel));
    }

    @GetMapping
    public ResponseEntity<Page<ColaboradorModel>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(colaboradorSvc.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") int id){
        Optional<ColaboradorModel> colaboradorModelOptional = colaboradorSvc.findById(id);
        if(!colaboradorModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(colaboradorModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id){
        Optional<ColaboradorModel> colaboradorModelOptional = colaboradorSvc.findById(id);
        if(!colaboradorModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador não encontrado");
        }
        colaboradorSvc.delete(colaboradorModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Colaborador deletado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id,
                                         @RequestBody @Valid ColaboradorDto colaboradorDto){
        Optional<ColaboradorModel> colaboradorModelOptional = colaboradorSvc.findById(id);
        if(!colaboradorModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador não encontrado");
        }

        var colaboradorModel = colaboradorModelOptional.get();
        BeanUtils.copyProperties(colaboradorDto, colaboradorModel);
        return ResponseEntity.status(HttpStatus.OK).body(colaboradorSvc.save(colaboradorModel));
    }
}

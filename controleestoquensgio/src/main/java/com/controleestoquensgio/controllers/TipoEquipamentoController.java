package com.controleestoquensgio.controllers;


import java.util.Optional;


import javax.validation.Valid;
import com.controleestoquensgio.models.TipoEquipamentoModel;
import com.controleestoquensgio.dtos.TipoEquipamentoDto;
import com.controleestoquensgio.services.TipoEquipamentoService;
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
@RequestMapping(value = {"/controle-estoque/tipoEquipamento"})
public class TipoEquipamentoController extends ControllerFather{

    @Autowired
    TipoEquipamentoService tipoEquipamentoSvc;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid TipoEquipamentoDto tipoEquipamentoDto){
        var tipoEquipamentoModel = new TipoEquipamentoModel();
        BeanUtils.copyProperties(tipoEquipamentoDto, tipoEquipamentoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoEquipamentoSvc.save(tipoEquipamentoModel));
    }

    @GetMapping
    public ResponseEntity<Page<TipoEquipamentoModel>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(tipoEquipamentoSvc.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") int id){
        Optional<TipoEquipamentoModel> tipoEquipamentoModelOptional = tipoEquipamentoSvc.findById(id);
       
        if(!tipoEquipamentoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de equipamento não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(tipoEquipamentoModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id){
       
        Optional<TipoEquipamentoModel> tipoEquipamentoModelOptional = tipoEquipamentoSvc.findById(id);
        
        if(!tipoEquipamentoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de equipamento não encontrado");
        }

        tipoEquipamentoSvc.delete(tipoEquipamentoModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Equipamento deletado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id,
                                         @RequestBody @Valid TipoEquipamentoDto tipoEquipamentoDto){
        Optional<TipoEquipamentoModel> tipoEquipamentoModelOptional = tipoEquipamentoSvc.findById(id);
        if(!tipoEquipamentoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de equipamento não encontrado");
        }

        var tipoEquipamentoModel = tipoEquipamentoModelOptional.get();
        BeanUtils.copyProperties(tipoEquipamentoDto, tipoEquipamentoModel);

        return ResponseEntity.status(HttpStatus.OK).body(tipoEquipamentoSvc.save(tipoEquipamentoModel));
    }
}

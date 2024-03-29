package com.controleestoquensgio.controllers;

import com.controleestoquensgio.dtos.tipoAcesso.ListarTipoAcessoDto;
import com.controleestoquensgio.dtos.tipoAcesso.TipoAcessoDto;
import com.controleestoquensgio.dtos.tipoAcesso.VisualizarTipoAcessoDto;
import com.controleestoquensgio.dtos.tipoAcesso.FiltrarTipoAcessoDto;
import com.controleestoquensgio.models.TipoAcessoModel;
import com.controleestoquensgio.services.TipoAcessoService;
import com.controleestoquensgio.util.ErroOuSucesso;
import com.controleestoquensgio.util.Mensagens;
import com.controleestoquensgio.util.SimOuNao;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping(value = {"/tiposDeAcesso"})
public class TipoAcessoController {

    @Autowired
    TipoAcessoService tipoAcessoSvc;

    @PostMapping
    public String save(@Valid TipoAcessoDto tipoAcessoDto, BindingResult result, Model model, Pageable pageable, RedirectAttributes redirectAttributes){
        
        if (result.hasErrors()) {
            model.addAttribute("tipoAcessoDto", tipoAcessoDto);
            model.addAttribute("listaDeTiposDeAcesso", tipoAcessoSvc.findAllAtivo(pageable, SimOuNao.SIM.name()).map(ListarTipoAcessoDto::new));
            model.addAttribute("filtrarTipoAcessoDto", new FiltrarTipoAcessoDto());
            return "tipoAcesso/cadastrarTipoAcesso";
        }
        
        var tipoAcessoModel = new TipoAcessoModel();
        
        BeanUtils.copyProperties(tipoAcessoDto, tipoAcessoModel);

        var resultado = tipoAcessoSvc.save(tipoAcessoModel);

        redirectAttributes.addFlashAttribute(resultado.getErroOuSucesso(), resultado.getMensagem());
        
        return "redirect:/tiposDeAcesso";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes) {

        var resultado = tipoAcessoSvc.deleteById(id);

        redirectAttributes.addFlashAttribute(resultado.getErroOuSucesso(), resultado.getMensagem());

        return "redirect:/tiposDeAcesso";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(value = "id") int id, @Valid TipoAcessoDto tipoAcessoDto, BindingResult result, Model model, Pageable pageable, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("tipoAcessoDto", tipoAcessoDto);
            model.addAttribute("listaDeTiposDeAcesso", tipoAcessoSvc.findAllAtivo(pageable, SimOuNao.SIM.name()).map(ListarTipoAcessoDto::new));
            return "tipoAcesso/atualizarTipoAcesso";
        }

        Optional<TipoAcessoModel> tipoAcessoModelOptional = tipoAcessoSvc.findById(id);

        if (tipoAcessoModelOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.tipoDeAcessoNaoEncontrado()
            );

            return "redirect:/tiposDeAcesso";
        }

        var tipoAcessoModel = tipoAcessoModelOptional.get();

        BeanUtils.copyProperties(tipoAcessoDto, tipoAcessoModel);

        var resultado = tipoAcessoSvc.update(tipoAcessoModel);

        redirectAttributes.addFlashAttribute(resultado.getErroOuSucesso(), resultado.getMensagem());

        return "redirect:/tiposDeAcesso";
    }

    @GetMapping
    public String getAll(Pageable pageable, Model model) {

        model.addAttribute("listaDeTiposDeAcesso", tipoAcessoSvc.findAllAtivo(pageable, SimOuNao.SIM.name()).map(ListarTipoAcessoDto::new));
        model.addAttribute("tipoAcessoDto", new TipoAcessoDto());
        model.addAttribute("filtrarTipoAcessoDto", new FiltrarTipoAcessoDto());

        return "tipoAcesso/cadastrarTipoAcesso";
    }

    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable(value = "id") int id, Model model, RedirectAttributes redirectAttributes) {

        Optional<TipoAcessoModel> tipoAcessoModelOptional = tipoAcessoSvc.findById(id);

        if (tipoAcessoModelOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.tipoDeEquipamentoNaoEncontrado()
            );

            return "redirect:/tiposDeAcesso";
        }

        model.addAttribute("tipoAcessoDto", new VisualizarTipoAcessoDto(tipoAcessoModelOptional.get()));

        return "tipoAcesso/atualizarTipoAcesso";
    }
    @PostMapping("/filtrar")
    public String filter(Pageable pageable, Model model, FiltrarTipoAcessoDto filtrarTipoAcessoDto) {

        model.addAttribute("listaDeTiposDeAcesso", tipoAcessoSvc.findAllByFilter(pageable, filtrarTipoAcessoDto).map(ListarTipoAcessoDto::new));
        model.addAttribute("tipoAcessoDto", new TipoAcessoDto());
        model.addAttribute("filtrarTipoAcessoDto", new FiltrarTipoAcessoDto());

        return "tipoAcesso/cadastrarTipoAcesso";
    }

}

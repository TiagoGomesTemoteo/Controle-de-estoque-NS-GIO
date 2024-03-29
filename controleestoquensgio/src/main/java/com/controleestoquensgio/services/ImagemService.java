package com.controleestoquensgio.services;

import java.util.Optional;

import com.controleestoquensgio.dtos.imagem.FiltrarImagemDto;
import com.controleestoquensgio.models.*;
import com.controleestoquensgio.repositories.ImagemQueryRepository;
import com.controleestoquensgio.util.SimOuNao;
import jakarta.transaction.Transactional;

import com.controleestoquensgio.models.ImagemModel;
import com.controleestoquensgio.util.ErroOuSucesso;
import com.controleestoquensgio.util.Mensagens;
import com.controleestoquensgio.util.Resultado;
import com.controleestoquensgio.repositories.ImagemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImagemService {

    @Autowired
    ProgramaService programaSvc;

    final ImagemRepository imagemRpt;

    final ImagemQueryRepository imagemQueryRpt;

    public ImagemService (ImagemRepository imagemRpt, final ImagemQueryRepository imagemQueryRpt){
        this.imagemRpt = imagemRpt;
        this.imagemQueryRpt = imagemQueryRpt;
    }

    @Transactional
    public Resultado save(ImagemModel imagemMdl){

        imagemRpt.save(imagemMdl);

        return new Resultado(
                ErroOuSucesso.SUCESSO.name(),
                Mensagens.operacaoBemSucedida()
        );
    }

    public Page<ImagemModel> findAll(Pageable pageable) {
        return imagemRpt.findAll(pageable);
    }

    public Page<ImagemModel> findAllAtivo(Pageable pageable, String ativo) {
        return imagemRpt.findAllByAtivo(pageable, ativo);
    }

    public Page<ImagemModel> findAllByFilter(Pageable pageable, FiltrarImagemDto filtrarImagemDto) {
        var imagens = imagemQueryRpt.customQuery(filtrarImagemDto);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), imagens.size());

        return new PageImpl<>(imagens.subList(start, end), pageable, imagens.size());
    }

    public Optional<ImagemModel> findById(Integer id) {
        return imagemRpt.findById(id);
    }

    @Transactional
    public Resultado deleteById(int id) {

        Optional<ImagemModel> imagemModelOptional = findById(id);

        if (imagemModelOptional.isEmpty()) {
            return new Resultado(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.imagemNaoEncontrada()
            );
        }

        ImagemModel imagemModel = imagemModelOptional.get();
        imagemModel.setAtivo(SimOuNao.NAO.name());

        imagemRpt.save(imagemModel);

        return new Resultado(
                ErroOuSucesso.SUCESSO.name(),
                Mensagens.operacaoBemSucedida()
        );
    }

    @Transactional
    public Resultado update(ImagemModel imagemMdl){

        imagemRpt.save(imagemMdl);

        return new Resultado(
                ErroOuSucesso.SUCESSO.name(),
                Mensagens.operacaoBemSucedida()
        );
    }

    @Transactional
    public Resultado addPrograma(int imagemId, int programaId){

        Optional<ImagemModel> imagemModelOptional = findById(imagemId);

        if (imagemModelOptional.isEmpty()) {
            return new Resultado(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.imagemNaoEncontrada()
            );
        }

        Optional<ProgramaModel> programaModelOptional = programaSvc.findById(programaId);

        if (programaModelOptional.isEmpty()) {
            return new Resultado(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.programaNaoEncontrado()
            );
        }

        ImagemModel imagemModel = imagemModelOptional.get();

        if (imagemModel.getProgramas().contains(programaModelOptional.get())) {
            return new Resultado(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.programaJaAssociadoAImagem()
            );
        }

        imagemModel.addPrograma(programaModelOptional.get());

        imagemRpt.save(imagemModel);

        return new Resultado(
                ErroOuSucesso.SUCESSO.name(),
                Mensagens.operacaoBemSucedida()
        );
    }

    @Transactional
    public Resultado removePrograma(int imagemId, int programaId) {
        Optional<ImagemModel> imagemModelOptional = findById(imagemId);

        if (imagemModelOptional.isEmpty()) {
            return new Resultado(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.imagemNaoEncontrada()
            );
        }

        Optional<ProgramaModel> programaModelOptional = programaSvc.findById(programaId);

        if (programaModelOptional.isEmpty()) {
            return new Resultado(
                    ErroOuSucesso.ERRO.name(),
                    Mensagens.programaNaoEncontrado()
            );
        }

        ImagemModel imagemModel = imagemModelOptional.get();

        imagemModel.removePrograma(programaModelOptional.get());

        imagemRpt.save(imagemModel);

        return new Resultado(
                ErroOuSucesso.SUCESSO.name(),
                Mensagens.operacaoBemSucedida()
        );
    }
}

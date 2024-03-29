package com.controleestoquensgio.dtos.tipoColaborador;

import com.controleestoquensgio.models.TipoColaboradorModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListarTipoColaboradorDto {

    private int id;
    private String descricao;
    private String ativo;
    public ListarTipoColaboradorDto (TipoColaboradorModel tipoColaboradorModel) {
        this.id = tipoColaboradorModel.getId();
        this.descricao = tipoColaboradorModel.getDescricao();
        this.ativo = tipoColaboradorModel.getAtivo();
    }
}

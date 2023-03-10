package med.voll.api.service.validacoes.cancelamento;

import med.voll.api.domain.consulta.DadosCancelamentoConsulta;

public interface ValidadorCancelamentoDeConsulta {

    void validar(DadosCancelamentoConsulta dados);

}

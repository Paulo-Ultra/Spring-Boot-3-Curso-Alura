package med.voll.api.service.validacoes.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta {

    void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta);
}

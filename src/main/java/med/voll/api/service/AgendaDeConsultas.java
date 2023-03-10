package med.voll.api.service;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.repository.ConsultaRepository;
import med.voll.api.repository.MedicoRepository;
import med.voll.api.repository.PacienteRepository;
import med.voll.api.service.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    //Forma de injetar todas validações de uma só vez
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dadosAgendamentoConsulta){

        if(!pacienteRepository.existsById(dadosAgendamentoConsulta.idPaciente())){
            throw new ValidacaoException("Id do paciente informado não existe");
        }
        if (dadosAgendamentoConsulta.idMedico() != null && !medicoRepository.existsById(dadosAgendamentoConsulta.idMedico())){
            throw new ValidacaoException("Id do médico informado não existe");
        }

        //Parecido com Design Patterns Strategy mas não é
        //SOD aplicado
        //Single Responsability Principle
        //Open-Closed Principle
        //Dependency Inversion Principle
        validadores.forEach(v -> v.validar(dadosAgendamentoConsulta));

        //Como utiliza um método que é um Optional é necessário usar o .get() para pegar ou usa o getReferenceById do repository
        var paciente = pacienteRepository.findById(dadosAgendamentoConsulta.idPaciente()).get();
        var medico = escolherMedico(dadosAgendamentoConsulta);
        if(medico == null) {
            throw new ValidacaoException("Não existe médico disponível nesta data!");
        }
        var consulta = new Consulta(null, medico, paciente, dadosAgendamentoConsulta.data(), null);
        consultaRepository.save(consulta);
        return new DadosDetalhamentoConsulta(consulta);
    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        if(dadosAgendamentoConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(dadosAgendamentoConsulta.idMedico());
        }
        if(dadosAgendamentoConsulta.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dadosAgendamentoConsulta.especialidade(), dadosAgendamentoConsulta.data());
    }

}

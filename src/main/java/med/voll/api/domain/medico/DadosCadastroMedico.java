package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
//        @NotBlank(message = "Nome é obrigatório") Sem usar o properties
        @NotBlank(message = "{nome.obrigatorio}")
        String nome,
//        @NotBlank(message = "Email é obrigatório")
//        @Email(message = "Formato do email é inválido")
        @NotBlank(message = "{email.obrigatorio}")
        @Email(message = "{email.invalido}")
        String email,
//        @NotBlank(message = "Telefone é obrigatório")
        @NotBlank(message = "{telefone.obrigatorio}")
        String telefone,
//        @NotBlank(message = "CRM é obrigatório")
//        @Pattern(regexp = "\\d{4,6}", message = "Formato do CRM é inválido")
        @NotBlank(message = "{crm.obrigatorio}")
        @Pattern(regexp = "\\d{4,6}", message = "{crm.invalido}")
        String crm,
//        @NotNull(message = "Especialidade é obrigatória")
        @NotNull(message = "{especialidade.obrigatoria}")
        Especialidade especialidade,
//        @NotNull(message = "Dados do endereço são obrigatórios")
        @NotNull(message = "{endereco.obrigatorio}")
        @Valid DadosEndereco endereco) {
}

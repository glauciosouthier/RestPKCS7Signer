package br.gs.signer.api;

import br.gov.frameworkdemoiselle.certificate.extension.DefaultExtension;
import br.gov.frameworkdemoiselle.certificate.extension.DefaultExtensionType;
import br.gov.frameworkdemoiselle.certificate.extension.ICPBrasilExtension;
import br.gov.frameworkdemoiselle.certificate.extension.ICPBrasilExtensionType;
import java.util.Date;
import java.util.List;

/**
 * Contem as informacoes do certificado. Essa classe he carrgada pelo Security
 * Certificate
 *
 */
public class ICPBrasilCertificate {

    @ICPBrasilExtension(type = ICPBrasilExtensionType.CPF)
    private String cpf;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NOME)
    private String nome;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.CEI_PESSOA_FISICA)
    private String ceiPessoaFisica;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NIS)
    private String pisPasep;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.EMAIL)
    private String email;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.DATA_NASCIMENTO)
    private String dataNascimento;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NUMERO_IDENTIDADE)
    private String numeroIdentidade;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.ORGAO_EXPEDIDOR_IDENTIDADE)
    private String orgaoExpedidorIdentidade;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.UF_ORGAO_EXPEDIDOR_IDENTIDADE)
    private String UfExpedidorIdentidade;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NUMERO_TITULO_ELEITOR)
    private String numeroTituloEleitor;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.ZONA_TITULO_ELEITOR)
    private String zonaTituloEleitor;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.SECAO_TITULO_ELEITOR)
    private String secaoTituloEleitor;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.MUNICIPIO_TITULO_ELEITOR)
    private String municipioTituloEleitor;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.UF_TITULO_ELEITOR)
    private String ufTituloEleitor;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.CNPJ)
    private String cnpj;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.CEI_PESSOA_JURIDICA)
    private String ceiPessoaJuridica;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NOME_RESPONSAVEL_PESSOA_JURIDICA)
    private String nomeResponsavelPessoaJuridica;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NOME_EMPRESARIAL)
    private String nomeEmpresarial;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.NIVEL_CERTIFICADO)
    private String nivelCertificado;
    @ICPBrasilExtension(type = ICPBrasilExtensionType.TIPO_CERTIFICADO)
    private String tipoCertificado;

    @DefaultExtension(type = DefaultExtensionType.CRL_URL)
    private List<String> crlURL;
    @DefaultExtension(type = DefaultExtensionType.AFTER_DATE)
    private Date afterDate;
    @DefaultExtension(type = DefaultExtensionType.BEFORE_DATE)
    private Date beforeDate;
    @DefaultExtension(type = DefaultExtensionType.CERTIFICATION_AUTHORITY)
    private Boolean certificationAuthority;
    @DefaultExtension(type = DefaultExtensionType.SERIAL_NUMBER)
    private String serialNumber;
    @DefaultExtension(type = DefaultExtensionType.ISSUER_DN)
    private String issuerDN;
    @DefaultExtension(type = DefaultExtensionType.SUBJECT_DN)
    private String subjectDN;

    @DefaultExtension(type = DefaultExtensionType.PATH_LENGTH)
    private int pathLength;
    @DefaultExtension(type = DefaultExtensionType.AUTHORITY_KEY_IDENTIFIER)
    private String authorityKeyIdentifier;
    @DefaultExtension(type = DefaultExtensionType.SUBJECT_KEY_IDENTIFIER)
    private String subjectKeyIdentifier;

    public List<String> getCrlURL() {
        return crlURL;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCeiPessoaFisica() {
        return ceiPessoaFisica;
    }

    public String getPisPasep() {
        return pisPasep;
    }

    public String getEmail() {
        return email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getNumeroIdentidade() {
        return numeroIdentidade;
    }

    public String getOrgaoExpedidorIdentidade() {
        return orgaoExpedidorIdentidade;
    }

    public String getUfExpedidorIdentidade() {
        return UfExpedidorIdentidade;
    }

    public String getNumeroTituloEleitor() {
        return numeroTituloEleitor;
    }

    public String getZonaTituloEleitor() {
        return zonaTituloEleitor;
    }

    public String getSecaoTituloEleitor() {
        return secaoTituloEleitor;
    }

    public String getMunicipioTituloEleitor() {
        return municipioTituloEleitor;
    }

    public String getUfTituloEleitor() {
        return ufTituloEleitor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCeiPessoaJuridica() {
        return ceiPessoaJuridica;
    }

    public String getNomeResponsavelPessoaJuridica() {
        return nomeResponsavelPessoaJuridica;
    }

    public String getNomeEmpresarial() {
        return nomeEmpresarial;
    }

    public String getNivelCertificado() {
        return nivelCertificado;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public Date getAfterDate() {
        return afterDate;
    }

    public Date getBeforeDate() {
        return beforeDate;
    }

    public Boolean getCertificationAuthority() {
        return certificationAuthority;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getIssuerDN() {
        return issuerDN;
    }

    public String getSubjectDN() {
        return subjectDN;
    }

    public int getPathLength() {
        return pathLength;
    }

    public String getAuthorityKeyIdentifier() {
        return authorityKeyIdentifier;
    }

    public String getSubjectKeyIdentifier() {
        return subjectKeyIdentifier;
    }
}

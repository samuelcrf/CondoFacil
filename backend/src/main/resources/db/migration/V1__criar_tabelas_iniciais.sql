CREATE TABLE condominio (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    endereco   VARCHAR(255) NOT NULL,
    criado_em  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);


CREATE TABLE usuario (
    id                   BIGSERIAL    PRIMARY KEY,
    condominio_id        BIGINT       NOT NULL REFERENCES condominio (id),
    nome                 VARCHAR(150) NOT NULL,
    email                VARCHAR(150) NOT NULL,
 	senha_criptografada  VARCHAR(100) NOT NULL,
    perfil               VARCHAR(20)  NOT NULL,
    telefone             VARCHAR(20),
	unidade              VARCHAR(50),
	especialidade        VARCHAR(100),
    ativo                BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT ck_usuario_perfil CHECK (perfil IN ('MORADOR', 'SINDICO', 'PRESTADOR'))
);

CREATE UNIQUE INDEX uk_usuario_email ON usuario (LOWER(email));

CREATE INDEX idx_usuario_condominio ON usuario (condominio_id);

CREATE TABLE chamado (
    id                 BIGSERIAL    PRIMARY KEY,
	condominio_id      BIGINT       NOT NULL REFERENCES condominio (id),
	morador_id         BIGINT       NOT NULL REFERENCES usuario (id),
	prestador_id       BIGINT       REFERENCES usuario (id),
    titulo             VARCHAR(150) NOT NULL,
    descricao          TEXT         NOT NULL,
    local_do_problema  VARCHAR(150),

    status             VARCHAR(30)  NOT NULL DEFAULT 'ABERTO',

    prioridade         VARCHAR(20),
    prazo              DATE,
    criado_em          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    atualizado_em      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_chamado_condominio ON chamado (condominio_id);
CREATE INDEX idx_chamado_morador    ON chamado (morador_id);
CREATE INDEX idx_chamado_prestador  ON chamado (prestador_id);
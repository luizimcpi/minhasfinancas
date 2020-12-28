CREATE TABLE public.usuario
(
  id bigserial NOT NULL PRIMARY KEY,
  nome character varying(150),
  email character varying(100),
  senha character varying(200),
  data_cadastro timestamp without time zone NOT NULL
);

CREATE TABLE public.lancamento
(
  id bigserial NOT NULL PRIMARY KEY ,
  descricao character varying(100) NOT NULL,
  mes integer NOT NULL,
  ano integer NOT NULL,
  valor numeric(16,2) NOT NULL,
  tipo character varying(20) CHECK ( tipo in ('RECEITA', 'DESPESA') ) NOT NULL,
  status character varying(20) CHECK ( status IN ('PENDENTE', 'CANCELADO', 'EFETIVADO') ) NOT NULL,
  id_usuario bigint REFERENCES public.usuario (id) NOT NULL,
  data_cadastro timestamp without time zone NOT NULL,
  data_alteracao timestamp without time zone NOT NULL
);
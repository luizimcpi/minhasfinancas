CREATE TABLE public.controle_pin
(
  id uuid NOT NULL PRIMARY KEY,
  email character varying(100),
  pin character varying(7),
  data_cadastro timestamp without time zone NOT NULL,
  data_expiracao timestamp without time zone NOT NULL
);

CREATE INDEX IF NOT EXISTS controle_pin_email_idx ON public.controle_pin (email);
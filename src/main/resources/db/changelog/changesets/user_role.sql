CREATE TABLE public.user_role (
	"id" serial4 NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT user_role_pk PRIMARY KEY (id),
	CONSTRAINT user_role_unique UNIQUE (id, name)
);

INSERT INTO public.user_role (name) VALUES ('ADMIN'), ('USER');